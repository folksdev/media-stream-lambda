package com.folksdev.mediastream.webhooks.discord.model

case class Embed(author: Author,
                 title: String,
                 url: String,
                 description: String,
                 color: Int,
                 thumbnail: UrlField,
                 fields: Option[Field])
