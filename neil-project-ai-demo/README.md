# neil-project-ai-demo

## 前期准备

- jdk: >=17
- spring-boot: 3.3.2
- ollama: 下载安装，https://ollama.com/download   
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  启动命令 `ollama serve`
- 大模型：
    - llama: https://ollama.com/library/llama3.1
    - llava: https://ollama.com/library/llava
    - nomic-embed-text: https://ollama.com/library/nomic-embed-text

## 依赖

### neil-project-parent

```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-bom</artifactId>
    <!-- 依赖版本请查看spring官网 -->
    <version>1.0.0-SNAPSHOT</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>
```

### neil-project-ai-demo

```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-ollama-spring-boot-starter</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-chroma-store-spring-boot-starter</artifactId>
</dependency>
```

## 配置

```yaml
spring:
  application:
    name: neil-project-ai
  ai:
    ollama:
      chat:
        model: llama3.1
      embedding:
        model: nomic-embed-text
    vectorstore:
      chroma:
        client:
          host: http://localhost
          port: 8000
        initialize-schema: true
        collection-name: neil-ai
```

## 案例

### 聊天

#### 同步调用

```java
  public String chat(@RequestParam("text") String text) {
      Prompt prompt = new Prompt(new UserMessage(text));
      return this.chatClient.prompt(prompt).call().content();
  }
```

#### 流式调用

```java
  public Flux<String> streamChat(@RequestParam("text") String text) {
      return streamingChatModel.stream(text);
  }
```

#### 流式多模态调用

```java
  public Flux<String> streamMultimodal(@RequestBody NeilPromptDTO neilPromptDTO) {
      UserMessage userMessage;
      if (null == neilPromptDTO.getImage()) {
          userMessage = new UserMessage(neilPromptDTO.getText());
      } else {
          userMessage = new UserMessage(neilPromptDTO.getText(),
                  Lists.newArrayList(new Media(MimeTypeUtils.IMAGE_PNG, neilPromptDTO.getImage())));
      }
      Prompt prompt = new Prompt(userMessage);
      var ollamaApi = new OllamaApi();
      var chatModel = new OllamaChatModel(ollamaApi,
              OllamaOptions.create()
                      .withModel("llava")
                      .withTemperature(0.9f));
      return chatModel.stream(prompt).map(result -> result.getResult().getOutput().getContent());
  }
```

### RAG(RAG, Retrieval-Augmented Generation) 检索增强生成

#### 数据处理

```java
    public void saveEmbedData(@RequestParam("text") String text) {
        List<Document> documents = List.of(
                new Document(text));
        for (Document document : documents) {
            float[] embed = embeddingModel.embed(document);
            document.setEmbedding(embed);
        }
        vectorStore.add(documents);
    }
```

### 检索、增强、生成
```java
    public String embedChat(@RequestParam("text") String text) {
        List<Message> userMessages = Lists.newArrayList(new UserMessage(text));
        List<Document> documents = vectorStore.similaritySearch(
                 SearchRequest.query(text).withTopK(4));
        for (Document document : documents) {
            UserMessage userMessage = new UserMessage(document.getContent());
            userMessages.add(userMessage);
        }
        Prompt prompt = new Prompt(userMessages);
        // 需要到改为流式调用
        return this.chatClient.prompt(prompt).call().content();
    }
```

参考文档：
- https://spring.io/projects/spring-ai
- https://ollama.com/
