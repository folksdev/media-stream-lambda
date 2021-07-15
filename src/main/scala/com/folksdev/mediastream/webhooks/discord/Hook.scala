package com.folksdev.mediastream.webhooks.discord

import com.folksdev.mediastream.webhooks.discord.model.Message
import scalaj.http.{Http, HttpOptions}

import scala.concurrent.{ExecutionContext, Future}

import  com.folksdev.mediastream.webhooks.json.JsonMapper._

sealed trait HookResponse

case object Ok extends HookResponse
case class Rejected(code: Int) extends HookResponse

class Hook(hookConfig: HookConfig)(implicit val ec: ExecutionContext) {

  def post(message: (String, String) => Message): Future[HookResponse] = Future {
    val body = toJson(message(hookConfig.name, hookConfig.avatarUrl))
    val response = Http(hookConfig.url)
      .postData(body)
      .header("Content-Type", "application/json")
      .header("Charset", "UTF-8")
      .option(HttpOptions.readTimeout(10000)).asBytes
    convertToHookResponse(response.code)
  }

  private def convertToHookResponse(code: Int): HookResponse =
    if (code >= 200 && code < 300) {
      Ok
    } else {
      Rejected(code)
    }
}
