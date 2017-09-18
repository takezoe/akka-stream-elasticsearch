package com.github.takezoe.akka.stream.elasticsearch.scaladsl

import akka.Done
import akka.stream.scaladsl.{Keep, Sink}
import org.elasticsearch.client.RestClient
import spray.json.{JsObject, JsonWriter}
import com.github.takezoe.akka.stream.elasticsearch._

import scala.concurrent.Future

object ElasticsearchSink {

  /**
   * Scala API: creates a sink based on [[ElasticsearchFlowStage]] that accepts as JsObject
   */
  def apply(indexName: String, typeName: String, settings: ElasticsearchSinkSettings)(
    implicit client: RestClient
  ): Sink[IncomingMessage[JsObject], Future[Done]] =
    ElasticsearchFlow.apply(indexName, typeName, settings).toMat(Sink.ignore)(Keep.right)

  /**
   * Scala API: creates a sink based on [[ElasticsearchFlowStage]] that accepts as specific type
   */
  def typed[T](indexName: String, typeName: String, settings: ElasticsearchSinkSettings)(
    implicit client: RestClient,
    writer: JsonWriter[T]
  ): Sink[IncomingMessage[T], Future[Done]] =
    ElasticsearchFlow.typed[T](indexName, typeName, settings).toMat(Sink.ignore)(Keep.right)

}
