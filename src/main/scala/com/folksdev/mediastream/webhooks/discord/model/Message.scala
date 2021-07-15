package com.folksdev.mediastream.webhooks.discord.model

case class Message(username: String,
                   avatarUrl: String,
                   content: String,
                   embeds: List[Embed])
