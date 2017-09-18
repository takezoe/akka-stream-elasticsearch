package com.github.takezoe.akka.stream.elasticsearch.javadsl

import java.util.concurrent.CompletionStage

import akka.stream.javadsl._
import akka.{Done, NotUsed}
import org.elasticsearch.client.RestClient
import com.github.takezoe.akka.stream.elasticsearch._

object ElasticsearchSink {

  /**
   * Java API: creates a sink based on [[ElasticsearchFlowStage]] that accepts as JsObject
   */
  def create(
      indexName: String,
      typeName: String,
      settings: ElasticsearchSinkSettings,
      client: RestClient
  ): akka.stream.javadsl.Sink[IncomingMessage[java.util.Map[String, Object]], CompletionStage[Done]] =
    ElasticsearchFlow
      .create(indexName, typeName, settings, client)
      .toMat(Sink.ignore, Keep.right[NotUsed, CompletionStage[Done]])

  /**
   * Java API: creates a sink based on [[ElasticsearchFlowStage]] that accepts as specific type
   */
  def typed[T](
    indexName: String,
    typeName: String,
    settings: ElasticsearchSinkSettings,
    client: RestClient
  ): akka.stream.javadsl.Sink[IncomingMessage[T], CompletionStage[Done]] =
    ElasticsearchFlow
      .typed(indexName, typeName, settings, client)
      .toMat(Sink.ignore, Keep.right[NotUsed, CompletionStage[Done]])

}
