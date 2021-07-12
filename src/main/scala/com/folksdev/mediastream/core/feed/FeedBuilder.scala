package com.folksdev.mediastream.core.feed

import com.folksdev.mediastream.core.model.{Feed, Source}

import scala.util.Try

trait FeedBuilder {
  def getFeed(source: Source): Try[Feed]
}
