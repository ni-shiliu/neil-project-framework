package com.neil.myth.core.event;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.IgnoreExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.neil.myth.common.bean.entity.MythTransaction;
import com.neil.myth.common.bean.factory.MythTransactionThreadFactory;
import com.neil.myth.common.config.MythConfig;
import com.neil.myth.common.enums.EventTypeEnum;
import com.neil.myth.core.service.MythCoordinatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author nihao
 * @date 2024/6/11
 */
@Component
@RequiredArgsConstructor
public class MythTransactionEventPublisher implements DisposableBean, ApplicationListener<ContextRefreshedEvent> {

    private Disruptor<MythTransactionEvent> disruptor;

    private final MythConfig mythConfig;

    private final MythCoordinatorService mythCoordinatorService;

    private static final int MAX_THREAD = Runtime.getRuntime().availableProcessors() << 1;

    private static final AtomicLong INDEX = new AtomicLong(1);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        start(mythConfig.getBufferSize(), mythConfig.getConsumerThreads());
    }

    @Override
    public void destroy() throws Exception {
        disruptor.shutdown();
    }

    private void start(int bufferSize, int threadSize) {
        disruptor = new Disruptor<>(new MythTransactionEventFactory(), bufferSize, runnable -> {
            return new Thread(new ThreadGroup("myth-disruptor"), runnable,
                    "disruptor-thread-" + INDEX.getAndIncrement());
        }, ProducerType.MULTI, new BlockingWaitStrategy());
        final Executor executor = new ThreadPoolExecutor(MAX_THREAD, MAX_THREAD, 0, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                MythTransactionThreadFactory.create("myth-log-disruptor", false),
                new ThreadPoolExecutor.AbortPolicy());

        MythTransactionEventHandler[] consumers = new MythTransactionEventHandler[MAX_THREAD];
        for (int i = 0; i < threadSize; i++) {
            consumers[i] = new MythTransactionEventHandler(mythCoordinatorService, executor);
        }
        disruptor.handleEventsWithWorkerPool(consumers);
        disruptor.setDefaultExceptionHandler(new IgnoreExceptionHandler());
        disruptor.start();
    }

    public void publishEvent(MythTransaction mythTransaction, EventTypeEnum eventTypeEnum) {
        RingBuffer<MythTransactionEvent> ringBuffer = disruptor.getRingBuffer();
        ringBuffer.publishEvent(new MythTransactionEventTranslator(eventTypeEnum), mythTransaction);
    }
}
