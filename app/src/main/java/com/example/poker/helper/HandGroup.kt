package com.example.poker.helper

import com.example.poker.ACE
import com.example.poker.Card
import com.example.poker.EIGHT
import com.example.poker.FIVE
import com.example.poker.FOUR
import com.example.poker.JACK
import com.example.poker.KING
import com.example.poker.NINE
import com.example.poker.QUEEN
import com.example.poker.SEVEN
import com.example.poker.SIX
import com.example.poker.TEN
import com.example.poker.THREE
import com.example.poker.TWO

data class HandGroup(val playerCards: List<Card>) {
    var group: Int = getHandGroup(playerCards)

    private fun getHandGroup(playerCards: List<Card>): Int {
        
        val cards = playerCards.sortedByDescending { it.rank }.toList()
        val card1Rank = cards.first().rank
        val card1Suit = cards.first().suit
        val card2Rank = cards.last().rank
        val card2Suit = cards.last().suit


        val suited = card1Suit == card2Suit
        val pair = card1Rank == card2Rank

        // --- group 1

        // AA or KK
        if (pair && (card1Rank == ACE || card1Rank == KING)) {
            return 1
        }

        // --- group 2

        // QQ or JJ
        if (pair && (card1Rank == QUEEN || card1Rank == JACK)) {
            return 2
        }

        //  AKs or AKo
        if (card2Rank == ACE && card1Rank == KING) {
            return 2
        }

        // --- group 3

        // 10 10 or 9 9
        if (pair && (card1Rank == TEN || card1Rank == NINE)) {
            return 3
        }

        // AQs or AQo
        if (card2Rank == ACE && card1Rank == QUEEN) {
            return 3
        }

        // --- group 4

        // 8 8 or 7 7
        if (pair && (card1Rank == EIGHT || card1Rank == SEVEN)) {
            return 4
        }

        // AJs or KQs
        if (suited && ((card2Rank == ACE && card1Rank == JACK) || (card1Rank == KING && card2Rank == QUEEN))) {
            return  4
        }

        // --- group 5

        // 6 6 or 5 5
        if (pair && (card1Rank == SIX || card1Rank == FIVE)) {
            return 5
        }

        // AJo or KQo
        if (!suited && ((card2Rank == ACE && card1Rank == JACK)) || (card1Rank == KING && card2Rank == QUEEN)) {
            return 5
        }

        // A10
        if (card2Rank == ACE && card1Rank == TEN) {
            return 5
        }

        // KJs
        if (suited && card1Rank == KING && card2Rank == JACK) {
            return 5
        }

        // --- group 6

        // 4 4, 3 3 or 2 2
        if (pair && (card1Rank == FOUR || card1Rank == THREE || card1Rank == TWO)) {
            return 6
        }

        // A9s to A2s
        if (suited && (card2Rank == ACE && (card1Rank in TWO..NINE))) {
            return 6
        }

        // KJo
        if (!suited && card1Rank == KING && card2Rank == JACK) {
            return 6
        }

        // K10s
        if (suited && card1Rank == KING && card2Rank == TEN) {
            return 6
        }

        // QJs or Q10s
        if (suited && card1Rank == QUEEN && (card2Rank == JACK || card2Rank == TEN)) {
            return 6
        }

        // J10s
        if (suited && card1Rank == JACK && card2Rank == TEN) {
            return 6
        }

        // --- group 7

        // A9o to A2o
        if (!suited && (card2Rank == ACE && (card1Rank in TWO..NINE))) {
            return 7
        }

        // K10o
        if (!suited && card1Rank == KING && card2Rank == TEN) {
            return 7
        }

        // QJo or Q10o
        if (!suited && card1Rank == QUEEN && (card2Rank == JACK || card2Rank == TEN)) {
            return 7
        }

        // J10o
        if (!suited && card1Rank == JACK && card2Rank == TEN) {
            return 7
        }

        // 10 9s
        if (suited && card1Rank == TEN && card2Rank == NINE) {
            return 7
        }

        // 9 8s
        if (suited && card1Rank == NINE && card2Rank == EIGHT) {
            return 7
        }

        // 8 7s
        if (suited && card1Rank == EIGHT && card2Rank == SEVEN) {
            return 7
        }

        // 7 6s
        if (suited && card1Rank == SEVEN && card2Rank == SIX) {
            return 7
        }

        // 6 5s
        if (suited && card1Rank == SIX && card2Rank == FIVE) {
            return 7
        }

        // 5 4s
        if (suited && card1Rank == FIVE && card2Rank == FOUR) {
            return 7
        }

        // --- group 8

        // K9 or k8
        if (card1Rank == KING && (card2Rank == NINE || card2Rank == EIGHT)) {
            return 8
        }

        // Q9s or Q8s
        if (suited && card1Rank == QUEEN && (card2Rank == NINE || card2Rank == EIGHT)) {
            return 8
        }

        // J9s
        if (suited && card1Rank == JACK && card2Rank == NINE) {
            return 8
        }

        // 10 8s
        if (suited && card1Rank == TEN && card2Rank == EIGHT) {
            return 8
        }

        // 10 9o
        if (!suited && card1Rank == TEN && card2Rank == NINE) {
            return 8
        }

        // 9 7s
        if (suited && card1Rank == NINE && card2Rank == SEVEN) {
            return 8
        }

        // 9 8o
        if (!suited && card1Rank == NINE && card2Rank == EIGHT) {
            return 8
        }

        // 8 6s
        if (suited && card1Rank == EIGHT && card2Rank == SIX) {
            return 8
        }

        // 8 7o
        if (!suited && card1Rank == EIGHT && card2Rank == SEVEN) {
            return 8
        }

        // 7 5s
        if (suited && card1Rank == SEVEN && card2Rank == FIVE) {
            return 8
        }

        // 7 6o
        if (!suited && card1Rank == SEVEN && card2Rank == SIX) {
            return 8
        }

        // 6 4s
        if (suited && card1Rank == SIX && card2Rank == FOUR) {
            return 8
        }

        return 9
    }
}
