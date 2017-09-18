package com.github.takezoe.akka.stream.elasticsearch.javadsl

import com.github.takezoe.akka.stream.elasticsearch._
import scaladsl.{ElasticsearchSourceSettings => ScalaElasticsearchSourceSettings}

final class ElasticsearchSourceSettings(val bufferSize: Int) {

  def this() = this(10)

  def withBufferSize(bufferSize: Int): ElasticsearchSourceSettings =
    new ElasticsearchSourceSettings(bufferSize)

  private[javadsl] def asScala: ScalaElasticsearchSourceSettings =
    ScalaElasticsearchSourceSettings(
      bufferSize = this.bufferSize
    )
}
