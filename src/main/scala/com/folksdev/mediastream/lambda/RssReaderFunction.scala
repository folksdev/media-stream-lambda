package com.folksdev.mediastream.lambda

import java.time.Instant
import java.time.temporal.ChronoUnit

import com.amazonaws.services.lambda.runtime.events.ScheduledEvent
import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}
import com.folksdev.mediastream.core.feed.RssFeedBuilder
import com.folksdev.mediastream.core.model.{Article, Feed, Source}
import com.folksdev.mediastream.core.reader.ReaderServiceImpl
import com.folksdev.mediastream.core.source.CsvSourceService
import com.folksdev.mediastream.webhooks.discord.model.{Author, Embed, Message, UrlField}
import com.folksdev.mediastream.webhooks.discord.{Hook, HookConfig}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.util.{Failure, Success}
class RssReaderFunction extends RequestHandler[ScheduledEvent, Unit]{
  override def handleRequest(input: ScheduledEvent, context: Context): Unit = {

    val res = io.Source.fromResource("Sources.csv")
    val sourceService = new CsvSourceService(res)

    val reader = new ReaderServiceImpl(RssFeedBuilder, (i: Instant) => i.isAfter(Instant.now().minus(1, ChronoUnit.DAYS).minus(1, ChronoUnit.HALF_DAYS)) )
    
    val feeds = reader.read(sourceService.sources)

    val hook = new Hook(HookConfig(
      "https://discord.com/api/webhooks/852869626643611678/tK-7iDfZTgALYlFeaJxdC3GpF7wD9-DNMOCxhl64w534baFlZegwZOf_gJnxR3k17lS3",
      "Kukla Abiniz",
      "https://pbs.twimg.com/profile_images/1392559592693972993/8iFt8wVG_400x400.jpg"
    ))

    val responses = Future.sequence(feeds.map(feedToMessageBuilder).map(hook.post))

    responses.onComplete {
      case Success(_) => println("Hook called")
      case Failure(exception) => println("Exception: " + exception.getMessage)
    }

    val result = Await.result(responses, 3.minutes)

    println("Lamda execution completed")
    ()
  }

  private def feedToMessageBuilder(feed: Feed): (String, String) => Message =
    Message(_, _ , "", feed.articles.map(article => Embed(
      author = Author(
        name = feed.source.name,
        url = feed.source.url,
        thumbnailUrl = feed.source.thumbnailUrl),
      title = article.title,
      url = article.url,
      description = article.description,
      thumbnail = UrlField(article.thumbnailUrl),
      color = 15258703,
      fields = None
    )))
}
