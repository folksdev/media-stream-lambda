package com.folksdev.mediastream.webhooks.json

import com.fasterxml.jackson.databind.json.{JsonMapper => FJsonMapper}
import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper, PropertyNamingStrategies}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.folksdev.mediastream.webhooks.discord.model.{Author, Embed, Message, UrlField}

object JsonMapper {
  val mapper = FJsonMapper.builder()
    .addModule(DefaultScalaModule)
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
    .build()

  def toJson(value: Map[Symbol, Any]): String = {
    toJson(value map { case (k,v) => k.name -> v})
  }

  def toJson(value: Any): String = {
    mapper.writeValueAsString(value)
  }

  def toMap[V](json:String)(implicit m: Manifest[V]) = fromJson[Map[String,V]](json)

  def fromJson[T](json: String)(implicit m : Manifest[T]): T = {
    mapper.readValue[T](json, m.runtimeClass.asInstanceOf[Class[T]])
  }
}
