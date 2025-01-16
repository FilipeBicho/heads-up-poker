package com.filipe.poker.bot

data class ChatRequest(
    val model: String,
    val messages: List<Message>,
    val response_format: ResponseFormat
)

data class Message(
    val role: String,
    val content: String
)

data class ChatResponse(
    val id: String,
    val choices: List<Choice>
)

data class Choice(
    val message: MessageContent
)

data class MessageContent(
    val role: String,
    val content: String
)

data class ResponseFormat(
    val type: String
)


