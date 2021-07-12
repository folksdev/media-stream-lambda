package com.folksdev.mediastream.core.model

import java.time.Instant

case class Article(title: String,
                   description: String,
                   url: String,
                   links: List[String],
                   content: List[String],
                   categories: List[String],
                   thumbnailUrl: String,
                   created: Instant)
