# NEIL-MYTH（Make Your Transactional Happen）

利用MQ实现分布式事务（最终一致）

## 模块

* neil-myth-annotation: 常用注解
* neil-myth-common: 公共类
* neil-myth-core: 核心包
* neil-myth-spring-boot-starter: 启动neil-myth

## 功能

* 支持spring-boot-starter启动
* 支持Mysql、MongoDB作为存储数据库
* 支持RocketMQ实现分布式事务（后续会支持Kafka等其他Mq）

## 使用

### producer:

1、引入jar

```
      <dependency>
        <groupId>neil-project-myth</groupId>
        <artifactId>neil-myth-spring-boot-starter</artifactId>
        <version>${neil.myth.version}</version>
      </dependency>
```

2、添加配置

```yaml
neil:
  myth:
    ## 开启neil-myth
    enabled: true
    ## myth表名，若未配置，会有默认规则
#    repositorySuffix: order
    ## 数据库默认Mysql
    ## repository-support: mongodb/db
    ## mysql
    myth-db-config:
      url: jdbc:mysql://localhost:3306/neil?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=false&zeroDateTimeBehavior=convertToNull
      username: root
      password: 12345678
    ## mongodb
#    myth-mongo-db-config:
#      mongo-db-name: neil_myth
#      mongo-url: mongodb://neil_myth:123456@localhost:27017/neil_myth
    mq:
      rocketmq:
        ## 开启producer
        producer-enabled: true
        ## producer group
        producer-group: MYTH-ORDER
```

3、demo

```java

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final UserGateway userGateway;

    private final GoodsGateway goodsGateway;

    @Override
    /**
     * 开启事务
     */
    @Myth
    public OrderDTO createOrder(OrderSaveDTO orderSaveDTO) {
        goodsGateway.changeInventory(new GoodsChangeDTO().setGoodsId("1122"));
        userGateway.registerUser(new UserSaveDTO().setUserName("neil"));
        return null;
    }
}

public interface GoodsFacade {

    @ApiOperation("变更库存")
    @PostMapping("changeInventory")
    /**
     * 事务参与者，destination=topic
     */
    @Myth(destination = "MYTH-GOODS")
    void changeInventory(@RequestBody GoodsChangeDTO goodsChangeDTO);

}

public interface UserFacade {

    @ApiOperation("注册用户")
    @PostMapping("registerUser")
    /**
     * 事务参与者，destination=topic
     */
    @Myth(destination = "MYTH-USER")
    UserDTO registerUser(@RequestBody UserSaveDTO userSaveDTO);
}
    
```

### consumer

1、引入jar

```
      <dependency>
        <groupId>neil-project-myth</groupId>
        <artifactId>neil-myth-spring-boot-starter</artifactId>
        <version>${neil.myth.version}</version>
      </dependency>
```

2、添加配置

```yaml

neil:
  myth:
    ## 开启neil-myth
    enabled: true
    ## myth表名，若未配置，会有默认规则
#    repositorySuffix: order
    ## 数据库默认Mysql
    ## repository-support: mongodb/db
    ## mysql
    myth-db-config:
      url: jdbc:mysql://localhost:3306/neil?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=false&zeroDateTimeBehavior=convertToNull
      username: root
      password: 12345678
    ## mongodb
#    myth-mongo-db-config:
#      mongo-db-name: neil_myth
#      mongo-url: mongodb://neil_myth:123456@localhost:27017/neil_myth
    mq:
      rocketmq:
        ## 开启consumer
        consumer-enabled: true
        ## topic
        consumer-topic: MYTH-GOODS
        ## consumer group
        consumer-group: MYTH-GOODS-GROUP

```

3、demo

```java
@RestController
@RequiredArgsConstructor
public class GoodsController implements GoodsFacade {

    @Override
    /**
     * consumer
     */
    @Myth
    public void changeInventory(@RequestBody GoodsChangeDTO goodsChangeDTO) {
        throw new RuntimeException("error le");

//        log.info("changeInventory success param: {}", JSONUtil.toJsonStr(goodsChangeDTO));
    }
}
```

## 原理

### 启动原理

#### 1、配置：neil.myth.enabled=true，自动装配**MythBootstrap**。

```java
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableConfigurationProperties
@ComponentScan(basePackages = {"com.neil.myth"})
@RequiredArgsConstructor
public class MythAutoConfiguration {

    private final MythConfigProperties mythConfigProperties;

    @Bean
    @ConditionalOnProperty(prefix = "neil.myth", name = "enabled", havingValue = "true")
    public MythBootstrap mythBootstrap(MythInitService mythInitService) {
        return new MythBootstrap(mythInitService, this.mythConfigProperties);
    }
}
```

#### 2、initMyth

* 利用SPI动态创建**Serializer**和**MythRepository**。
* `mythCoordinatorServiceImpl.init()`

```java
@Service
@RequiredArgsConstructor
public class MythInitServiceImpl implements MythInitService {


    private final MythCoordinatorService mythCoordinatorService;

    @Override
    public void initMyth(MythConfig mythConfig) {
        loadSpiRepositorySupport(mythConfig);
        mythCoordinatorService.init(mythConfig);
    }

    private void loadSpiRepositorySupport(MythConfig mythConfig) {
        // spi serializer and register
        final Serializer serializer = MythSPIClassLoader.getMythSPIClassLoader(Serializer.class)
                .getActivateMythSPIClazz(mythConfig.getSerializer());
        SpringUtil.registerBean(Serializer.class.getName(), serializer);

        // spi repository and register
        final MythRepository mythRepository = MythSPIClassLoader.getMythSPIClassLoader(MythRepository.class)
                .getActivateMythSPIClazz(mythConfig.getRepositorySupport());
        mythRepository.setSerializer(serializer);
        SpringUtil.registerBean(MythRepository.class.getName(), mythRepository);
    }

}

@Service
@RequiredArgsConstructor
public class MythCoordinatorServiceImpl implements MythCoordinatorService {

    private final MythApplicationService mythApplicationService;

    private MythRepository mythRepository;

    @Override
    public void init(MythConfig mythConfig) throws MythException {
        mythRepository = SpringUtil.getBean(MythRepository.class);
        final String repositorySuffix = getRepositorySuffix(mythConfig.getRepositorySuffix());
        mythRepository.init(repositorySuffix, mythConfig);
    }
}
```

### 2、Producer和Consumer，AOP织入

#### 1、添加注解

```java
    @Myth
    public OrderDTO createOrder(OrderSaveDTO orderSaveDTO) {
        goodsGateway.changeInventory(new GoodsChangeDTO().setGoodsId("1122"));
        userGateway.registerUser(new UserSaveDTO().setUserName("neil"));
        return null;
    }
```

#### 2、AOP织入

```java
@Aspect
@Component
@RequiredArgsConstructor
public class MythTransactionAspect {

    private final MythTransactionInterceptor mythTransactionInterceptor;

    @Pointcut("@annotation(com.neil.myth.annotation.Myth)")
    public void mythInterceptor() {

    }

    @Around("mythInterceptor()")
    public Object interceptMythAnnotationMethod(ProceedingJoinPoint proceedingJoinPoint) throws  Throwable {
        return mythTransactionInterceptor.interceptor(proceedingJoinPoint);
    }
}

@Service
@RequiredArgsConstructor
public class MythTransactionInterceptorImpl implements MythTransactionInterceptor {


    private final MythTransactionAspectService mythTransactionAspectService;

    @Override
    public Object interceptor(ProceedingJoinPoint pjp) throws Throwable {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        MythTransactionContext mythTransactionContext;
        if (Objects.isNull(requestAttributes)) {
            // 通过消息反射执行, mock new MythTransactionContext
            mythTransactionContext = new MythTransactionContext();
        } else {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            // 获取请求头 MYTH_TRANSACTION_CONTEXT
            mythTransactionContext = RpcMediator.getInstance().acquire(request::getHeader);
        }
        return mythTransactionAspectService.invoke(mythTransactionContext, pjp);
    }

}

@Service
@RequiredArgsConstructor
public class MythTransactionAspectServiceImpl implements MythTransactionAspectService {

    private final MythTransactionFactoryService mythTransactionFactoryService;

    @Override
    public Object invoke(MythTransactionContext mythTransactionContext, ProceedingJoinPoint point) throws Throwable {
        Class clazz = mythTransactionFactoryService.factoryOf(mythTransactionContext);
        MythTransactionHandler mythTransactionHandler = (MythTransactionHandler) SpringUtil.getBean(clazz);
        // 执行不同事务handle
        return mythTransactionHandler.handler(point, mythTransactionContext);
    }
}

@Override
public Class factoryOf(MythTransactionContext context) throws Throwable {
    if (Objects.isNull(context)) {
        return mythTransactionEngine.isBegin()
                // 事务参与者
                ? ParticipantMythTransactionHandler.class
                // 事务开启者
                : StartMythTransactionHandler.class;
    } else {
        // 事务提供者：请求头不为null，说明被调用，或者通过消息反射执行, mock new MythTransactionContext
        return ActorMythTransactionHandler.class;
    }
}

/**
 * 事务开启者
 */
@Service
@RequiredArgsConstructor
public class StartMythTransactionHandler implements MythTransactionHandler {

    private final MythTransactionEngine mythTransactionEngine;

    @Override
    public Object handler(ProceedingJoinPoint point, MythTransactionContext mythTransactionContext) throws Throwable {
        try {
            mythTransactionEngine.begin(point);
            Object proceed = point.proceed();
            mythTransactionEngine.updateStatus(MythStatusEnum.COMMIT);
            return proceed;
        } catch (Throwable throwable) {
            mythTransactionEngine.failTransaction(throwable.getMessage());
            throw throwable;
        } finally {
            mythTransactionEngine.sendMessage();
            mythTransactionEngine.cleanThreadLocal();
            TransactionContextLocal.getInstance().remove();
        }
    }
}

/**
 * 事务参与者
 */
@Service
@RequiredArgsConstructor
public class ParticipantMythTransactionHandler implements MythTransactionHandler {

    private final MythTransactionEngine mythTransactionEngine;

    @Override
    public Object handler(ProceedingJoinPoint point, MythTransactionContext mythTransactionContext) throws Throwable {
        try {
            Object proceed = point.proceed();
            mythTransactionEngine.registerParticipant(point, MythStatusEnum.COMMIT, null);
            return proceed;
        } catch (Throwable throwable) {
            mythTransactionEngine.registerParticipant(point, MythStatusEnum.FAILURE, throwable.getMessage());
        }
        return null;
    }

}

/**
 * 事务提供者
 */
@Service
@RequiredArgsConstructor
public class ActorMythTransactionHandler implements MythTransactionHandler {

    private final MythTransactionEngine mythTransactionEngine;

    @Override
    public Object handler(ProceedingJoinPoint point, MythTransactionContext mythTransactionContext) throws Throwable {
        try {
            mythTransactionEngine.actorTransaction(point, mythTransactionContext);
            final Object proceed = point.proceed();
            mythTransactionEngine.updateStatus(MythStatusEnum.COMMIT);
            return proceed;
        } catch (Throwable throwable) {
            mythTransactionEngine.failTransaction(throwable.getMessage());
            throw throwable;
        } finally {
            TransactionContextLocal.getInstance().remove();
        }
    }
}
```

### producer入口

```java
@Configuration
@ConditionalOnProperty(prefix = "neil.myth.mq.rocketmq", name = "producer-enabled", havingValue = "true")
public class RocketmqConfig {

    @Value("${rocketmq.name-server}")
    private String nameServer;

    @Value("${neil.myth.mq.rocketmq.producer-group}")
    private String producerGroup;

    @Bean
    public RocketMQTemplate rocketMQTemplate() {
        RocketMQTemplate rocketMQTemplate = new RocketMQTemplate();
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer(producerGroup);
        defaultMQProducer.setNamesrvAddr(nameServer);
        rocketMQTemplate.setProducer(defaultMQProducer);
        return rocketMQTemplate;
    }

}
```

### consumer入口

```java
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "neil.myth.mq.rocketmq", name = "consumer-enabled", havingValue = "true")
public class RocketMqConsumerConfig implements ApplicationListener<ContextRefreshedEvent> {

    private final MythMqReceiveService mythMqReceiveService;

    private final MythConfig mythConfig;

    @Value("${rocketmq.name-server}")
    private String nameServer;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        startMythConsumer();
    }

    private void startMythConsumer() {
        MythConfig.Rocketmq rocketmq = mythConfig.getMq().getRocketmq();

        DefaultMQPushConsumer consumer =
                new DefaultMQPushConsumer(rocketmq.getConsumerGroup());
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.setNamesrvAddr(nameServer);
        consumer.setConsumeMessageBatchMaxSize(mythConfig.getConsumeMessageBatchMaxSize());
        consumer.setMaxReconsumeTimes(mythConfig.getRetryMax());
        /**
         * 订阅指定topic下tags
         */
        try {
            consumer.subscribe(rocketmq.getConsumerTopic(), rocketmq.getConsumerTag());
            consumer.registerMessageListener((List<MessageExt> msgList, ConsumeConcurrentlyContext context) -> {
                MessageExt msg = msgList.get(0);
                try {
                    log.info("rocketmq receive message : {}", JSONUtil.toJsonStr(msg));
                    final byte[] message = msg.getBody();
                    mythMqReceiveService.processMessage(message);
                } catch (Exception e) {
                    log.error("neil myth consumer process error");
                    e.printStackTrace();
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });
            consumer.start();
        } catch (MQClientException e) {
            throw new RuntimeException(e);
        }
        SpringUtil.registerBean(consumer.getClass().getName(), consumer);
    }

}
```

