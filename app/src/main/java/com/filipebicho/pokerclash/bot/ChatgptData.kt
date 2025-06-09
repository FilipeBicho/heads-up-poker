package com.filipebicho.pokerclash.bot

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.filipebicho.pokerclash.cards.Card

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

data class PokerRequest(
    val gameType: String = "Heads-up Texas hold'em",
    val yourHand: String,
    val tableCards: String,
    val round: String,
    val dealer: String,
    val smallBlind: Int,
    val bigBlind: Int,
    val yourStack: Int,
    val opponentStack: Int,
    val yourBetThisRound: Int,
    val opponentBetThisRound: Int,
    val potBeforeRound: Int,
    val currentPot: Int,
    val actionHistory: List<String> = emptyList(),
    val validActions: List<String>,
    val opponentStats: Map<String, Int> = emptyMap(),
) {
    fun toPrompt(): String {
        return buildString {
            appendLine("Game type: $gameType")
            appendLine("Your hand: $yourHand")
            appendLine("Table cards: $tableCards")
            appendLine("Round: $round")
            appendLine("Dealer: $dealer")
            appendLine("Small blind: $smallBlind")
            appendLine("Big blind: $bigBlind")
            appendLine("Your stack: $yourStack")
            appendLine("Opponent stack: $opponentStack")
            appendLine("Your bet this round: $yourBetThisRound")
            appendLine("Opponent bet this round: $opponentBetThisRound")
            appendLine("Pot before this round: $potBeforeRound")
            appendLine("Current pot: $currentPot")
            appendLine("Valid actions: ${validActions.joinToString(", ", prefix = "[", postfix = "]")}")
            appendLine("Opponent Stats: ${opponentStats.entries.joinToString(", ", prefix = "{", postfix = "}") { "${it.key}: ${it.value}" }}")
            appendLine("Output: JSON with keys \"action\" and \"bet\"")
            appendLine("Question: What should be my action and bet?")
        }
    }
}

