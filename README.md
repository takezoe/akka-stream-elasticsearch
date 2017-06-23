akka-stream-elasticsearch [![Build Status](https://travis-ci.org/takezoe/akka-stream-elasticsearch.svg?branch=master)](https://travis-ci.org/takezoe/akka-stream-elasticsearch)
========
The Elasticsearch connector provides Akka Stream sources and sinks for Elasticsearch.

For more information about Elasticsearch please visit the [official documentation](https://www.elastic.co/guide/index.html).

## Artifacts

- sbt:

  ```scala
  libraryDependencies += "com.github.takezoe" %% "akka-stream-elasticsearch" % "1.0.0"
  ```

- Maven:

  ```xml
  <dependency>
    <groupId>com.github.takezoe</groupId>
    <artifactId>akka-stream-elasticsearch_2.12</artifactId>
    <version>1.0.0</version>
  </dependency>
  ```

- Gradle:

  ```gradle
  dependencies {
    compile group: "com.github.takezoe", name: "akka-stream-elasticsearch_2.12", version: "1.0.0"
  }
  ```

## Usage

Sources, Flows and Sinks provided by this connector need a prepared `RestClient` to access to Elasticsearch.

- Scala:

  ```scala
  implicit val client = RestClient.builder(new HttpHost("localhost", 9201)).build()
  ```

- Java:

  ```java
  client = RestClient.builder(new HttpHost("localhost", 9211)).build();
  ```

We will also need an `ActorSystem` and an `ActorMaterializer`.

- Scala:

  ```scala
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  ```

- Java:

  ```java
  system = ActorSystem.create();
  materializer = ActorMaterializer.create(system);
  ```

This is all preparation that we are going to need.

### JsObject message

Now we can stream messages which contains spray-json's `JsObject` (in Scala) or `java.util.Map<String, Object>` (in Java) 
from or to Elasticsearch where we have access to by providing the `RestClient` to the `ElasticsearchSource` or the `ElasticsearchSink`.

- Scala:

  ```scala
  val f1 = ElasticsearchSource(
    "source",
    "book",
    """{"match_all": {}}""",
    ElasticsearchSourceSettings(5)
  )
  .map { message: OutgoingMessage[JsObject] =>
    IncomingMessage(Some(message.id), message.source)
  }
  .runWith(
    ElasticsearchSink(
      "sink1",
      "book",
      ElasticsearchSinkSettings(5)
    )
  )
  ```

- Java:

  ```java
  CompletionStage<Done> f1 = ElasticsearchSource.create(
      "source",
      "book",
      "{\"match_all\": {}}",
      new ElasticsearchSourceSettings(5),
      client)
      .map(m -> new IncomingMessage<>(new Some<String>(m.id()), m.source()))
      .runWith(
          ElasticsearchSink.create(
              "sink1",
              "book",
              new ElasticsearchSinkSettings(5),
              client),
          materializer);
  ```

### Typed messages

Also, it's possible to stream messages which contains any classes. In Scala, spray-json is used for JSON conversion, 
so defining the mapped class and `JsonFormat` for it is necessary. In Java, Jackson is used, so just define the mapped class.

- Scala:

  ```scala
  case class Book(title: String)
  implicit val format = jsonFormat1(Book)
  ```

- Java:

  ```java
  public static class Book {
    public String title;
  }
  ```

Use `ElasticsearchSource.typed` and `ElasticsearchSink.typed` to create source and sink instead.

- Scala:

  ```scala
  val f1 = ElasticsearchSource
    .typed[Book](
      "source",
      "book",
      """{"match_all": {}}""",
      ElasticsearchSourceSettings(5)
    )
    .map { message: OutgoingMessage[Book] =>
      IncomingMessage(Some(message.id), message.source)
    }
    .runWith(
      ElasticsearchSink.typed[Book](
        "sink2",
        "book",
        ElasticsearchSinkSettings(5)
      )
    )
  ```

- Java:

  ```java
  CompletionStage<Done> f1 = ElasticsearchSource.typed(
      "source",
      "book",
      "{\"match_all\": {}}",
      new ElasticsearchSourceSettings(5),
      client,
      Book.class)
      .map(m -> new IncomingMessage<>(new Some<String>(m.id()), m.source()))
      .runWith(
          ElasticsearchSink.typed(
              "sink2",
              "book",
              new ElasticsearchSinkSettings(5),
              client),
          materializer);
  ```

### Configuration

We can specify the buffer size for the source and the sink.

- Scala (source):

  ```scala
  final case class ElasticsearchSourceSettings(bufferSize: Int = 10)
  ```

- Scala (sink):

  ```scala
  final case class ElasticsearchSinkSettings(bufferSize: Int = 10)
  ```

`ElasticsearchSource` retrieves messages from Elasticsearch by scroll scan. This buffer size is used as the scroll size.
`ElasticsearchSink` puts messages by one bulk request per messages of this buffer size.

### Using Elasticsearch as a Flow

You can also build flow stages. The API is similar to creating Sinks.

- Scala (flow):

  ```scala
  val f1 = ElasticsearchSource
    .typed[Book](
      "source",
      "book",
      """{"match_all": {}}""",
      ElasticsearchSourceSettings(5)
    )
    .map { message: OutgoingMessage[Book] =>
      IncomingMessage(Some(message.id), message.source)
    }
    .via(
      ElasticsearchFlow.typed[Book](
        "sink3",
        "book",
        ElasticsearchSinkSettings(5)
      )
    )
    .runWith(Sink.seq)
  ```

- Java (flow):

  ```java
  CompletionStage<List<Response>> f1 = ElasticsearchSource.typed(
      "source",
      "book",
      "{\"match_all\": {}}",
      new ElasticsearchSourceSettings(5),
      client,
      Book.class)
      .map(m -> new IncomingMessage<>(new Some<String>(m.id()), m.source()))
      .via(ElasticsearchFlow.typed(
          "sink3",
          "book",
          new ElasticsearchSinkSettings(5),
          client))
      .runWith(Sink.seq(), materializer);
  ```

### Running the example code

The code in this guide is part of runnable tests of this project. You are welcome to edit the code and run it in sbt.

- Scala

  ```
  sbt
  > elasticsearch/testOnly *.ElasticsearchSpec
  ```

- Java

  ```
  sbt
  > elasticsearch/testOnly *.ElasticsearchTest
  ```
