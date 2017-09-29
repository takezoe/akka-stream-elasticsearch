package com.github.takezoe.akka.stream.elasticsearch.scaladsl

//#sink-settings
final case class ElasticsearchSinkSettings(bufferSize: Int = 10,
                                           retryInterval: Int = 5000,
                                           maxRetry: Int = 100,
                                           retryPartialFailure: Boolean = true)
//#sink-settings
