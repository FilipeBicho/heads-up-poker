package com.example.poker

class PreFlopOdds(private var playerCards: List<Card>) {
    fun getOdds(): Int {

        /* A A */
        if (playerCards[0].rank == 0 && playerCards[1].rank == 0) {
            return 85
        }

        /* A K suit */
        if (playerCards[0].rank == 0 && playerCards[1].rank == 12 && playerCards[0].suit == playerCards[1].suit
            || playerCards[1].rank == 0 && playerCards[0].rank == 12 && playerCards[0].suit == playerCards[1].suit
        ) {
            return 67
        }

        /* A K out suit */
        if (playerCards[0].rank == 0 && playerCards[1].rank == 12 && playerCards[0].suit != playerCards[1].suit
            || playerCards[1].rank == 0 && playerCards[0].rank == 12 && playerCards[0].suit != playerCards[1].suit
        ) {
            return 65
        }

        /* A Q suit */
        if (playerCards[0].rank == 0 && playerCards[1].rank == 11 && playerCards[0].suit == playerCards[1].suit
            || playerCards[1].rank == 0 && playerCards[0].rank == 11 && playerCards[0].suit == playerCards[1].suit
        ) {
            return 66
        }

        /* A Q out suit */
        if (playerCards[0].rank == 0 && playerCards[1].rank == 11 && playerCards[0].suit != playerCards[1].suit
            || playerCards[1].rank == 0 && playerCards[0].rank == 11 && playerCards[0].suit != playerCards[1].suit
        ) {
            return 66
        }

        /* A J suit */
        if (playerCards[0].rank == 0 && playerCards[1].rank == 10 && playerCards[0].suit == playerCards[1].suit
            || playerCards[1].rank == 0 && playerCards[0].rank == 10 && playerCards[0].suit == playerCards[1].suit
        ) {
            return 65
        }

        /* A J out suit */
        if (playerCards[0].rank == 0 && playerCards[1].rank == 10 && playerCards[0].suit != playerCards[1].suit
            || playerCards[1].rank == 0 && playerCards[0].rank == 10 && playerCards[0].suit != playerCards[1].suit
        ) {
            return 63
        }
        /* A 10 suit */
        if (playerCards[0].rank == 0 && playerCards[1].rank == 9 && playerCards[0].suit == playerCards[1].suit
            || playerCards[1].rank == 0 && playerCards[0].rank == 9 && playerCards[0].suit == playerCards[1].suit
        ) {
            return 64
        }

        /* A 10 out suit */
        if (playerCards[0].rank == 0 && playerCards[1].rank == 9 && playerCards[0].suit != playerCards[1].suit
            || playerCards[1].rank == 0 && playerCards[0].rank == 9 && playerCards[0].suit != playerCards[1].suit
        ) {
            return 62
        }

        /* A 9 suit */
        if (playerCards[0].rank == 0 && playerCards[1].rank == 8 && playerCards[0].suit == playerCards[1].suit
            || playerCards[1].rank == 0 && playerCards[0].rank == 8 && playerCards[0].suit == playerCards[1].suit
        ) {
            return 63
        }
        /* A 9 out suit */
        if (playerCards[0].rank == 0 && playerCards[1].rank == 8 && playerCards[0].suit != playerCards[1].suit
            || playerCards[1].rank == 0 && playerCards[0].rank == 8 && playerCards[0].suit != playerCards[1].suit
        ) {
            return 60
        }

        /* A 8 suit */
        if (playerCards[0].rank == 0 && playerCards[1].rank == 7 && playerCards[0].suit == playerCards[1].suit
            || playerCards[1].rank == 0 && playerCards[0].rank == 7 && playerCards[0].suit == playerCards[1].suit
        ) {
            return 62
        }

        /* A 8 out suit */
        if (playerCards[0].rank == 0 && playerCards[1].rank == 7 && playerCards[0].suit != playerCards[1].suit
            || playerCards[1].rank == 0 && playerCards[0].rank == 7 && playerCards[0].suit != playerCards[1].suit
        ) {
            return 60
        }

        /* A 7 suit */
        if (playerCards[0].rank == 0 && playerCards[1].rank == 6 && playerCards[0].suit == playerCards[1].suit
            || playerCards[1].rank == 0 && playerCards[0].rank == 6 && playerCards[0].suit == playerCards[1].suit
        ) {
            return 61
        }

        /* A 7 out suit */
        if (playerCards[0].rank == 0 && playerCards[1].rank == 6 && playerCards[0].suit != playerCards[1].suit
            || playerCards[1].rank == 0 && playerCards[0].rank == 6 && playerCards[0].suit != playerCards[1].suit
        ) {
            return 59
        }

        /* A 6 suit */
        if (playerCards[0].rank == 0 && playerCards[1].rank == 5 && playerCards[0].suit == playerCards[1].suit
            || playerCards[1].rank == 0 && playerCards[0].rank == 5 && playerCards[0].suit == playerCards[1].suit
        ) {
            return 60
        }

        /* A 6 out suit */
        if (playerCards[0].rank == 0 && playerCards[1].rank == 5 && playerCards[0].suit != playerCards[1].suit
            || playerCards[1].rank == 0 && playerCards[0].rank == 5 && playerCards[0].suit != playerCards[1].suit
        ) {
            return 57
        }

        /* A 5 suit */
        if (playerCards[0].rank == 0 && playerCards[1].rank == 4 && playerCards[0].suit == playerCards[1].suit
            || playerCards[1].rank == 0 && playerCards[0].rank == 4 && playerCards[0].suit == playerCards[1].suit
        ) {
            return 59
        }

        /* A 5 out suit */
        if (playerCards[0].rank == 0 && playerCards[1].rank == 4 && playerCards[0].suit != playerCards[1].suit
            || playerCards[1].rank == 0 && playerCards[0].rank == 4 && playerCards[0].suit != playerCards[1].suit
        ) {
            return 57
        }
        /* A 4 suit */
        if (playerCards[0].rank == 0 && playerCards[1].rank == 3 && playerCards[0].suit == playerCards[1].suit
            || playerCards[1].rank == 0 && playerCards[0].rank == 3 && playerCards[0].suit == playerCards[1].suit
        ) {
            return 58
        }

        /* A 4 out suit */
        if (playerCards[0].rank == 0 && playerCards[1].rank == 3 && playerCards[0].suit != playerCards[1].suit
            || playerCards[1].rank == 0 && playerCards[0].rank == 3 && playerCards[0].suit != playerCards[1].suit
        ) {
            return 56
        }

        /* A 3 suit */
        if (playerCards[0].rank == 0 && playerCards[1].rank == 2 && playerCards[0].suit == playerCards[1].suit
            || playerCards[1].rank == 0 && playerCards[0].rank == 2 && playerCards[0].suit == playerCards[1].suit
        ) {
            return 58
        }

        /* A 3 out suit */
        if (playerCards[0].rank == 0 && playerCards[1].rank == 2 && playerCards[0].suit != playerCards[1].suit
            || playerCards[1].rank == 0 && playerCards[0].rank == 2 && playerCards[0].suit != playerCards[1].suit
        ) {
            return 55
        }

        /* A 2 suit */
        if (playerCards[0].rank == 0 && playerCards[1].rank == 1 && playerCards[0].suit == playerCards[1].suit
            || playerCards[1].rank == 0 && playerCards[0].rank == 1 && playerCards[0].suit == playerCards[1].suit
        ) {
            return 57
        }

        /* A 2 out suit */
        if (playerCards[0].rank == 0 && playerCards[1].rank == 1 && playerCards[0].suit != playerCards[1].suit
            || playerCards[1].rank == 0 && playerCards[0].rank == 1 && playerCards[0].suit != playerCards[1].suit
        ) {
            return 54
        }

        /* K K */
        if (playerCards[0].rank == 12 && playerCards[1].rank == 12) {
            return 82
        }

        /* K Q suit */
        if (playerCards[0].rank == 12 && playerCards[1].rank == 11 && playerCards[0].suit == playerCards[1].suit
            || playerCards[1].rank == 12 && playerCards[0].rank == 11 && playerCards[0].suit == playerCards[1].suit
        ) {
            return 63
        }

        /* K Q out suit */
        if (playerCards[0].rank == 12 && playerCards[1].rank == 11 && playerCards[0].suit != playerCards[1].suit
            || playerCards[1].rank == 12 && playerCards[0].rank == 11 && playerCards[0].suit != playerCards[1].suit
        ) {
            return 61
        }

        /* K J suit */
        if (playerCards[0].rank == 12 && playerCards[1].rank == 10 && playerCards[0].suit == playerCards[1].suit
            || playerCards[1].rank == 12 && playerCards[0].rank == 10 && playerCards[0].suit == playerCards[1].suit
        ) {
            return 62
        }

        /* K J out suit */
        if (playerCards[0].rank == 12 && playerCards[1].rank == 10 && playerCards[0].suit != playerCards[1].suit
            || playerCards[1].rank == 12 && playerCards[0].rank == 10 && playerCards[0].suit != playerCards[1].suit
        ) {
            return 60
        }

        /* K 10 suit */
        if (playerCards[0].rank == 12 && playerCards[1].rank == 9 && playerCards[0].suit == playerCards[1].suit
            || playerCards[1].rank == 12 && playerCards[0].rank == 9 && playerCards[0].suit == playerCards[1].suit
        ) {
            return 61
        }

        /* K 10 out suit */
        if (playerCards[0].rank == 12 && playerCards[1].rank == 9 && playerCards[0].suit != playerCards[1].suit
            || playerCards[1].rank == 12 && playerCards[0].rank == 9 && playerCards[0].suit != playerCards[1].suit
        ) {
            return 59
        }

        /* K 9 suit */
        if (playerCards[0].rank == 12 && playerCards[1].rank == 8 && playerCards[0].suit == playerCards[1].suit
            || playerCards[1].rank == 12 && playerCards[0]
                .rank == 8 && playerCards[0].suit == playerCards[1].suit
        ) {
            return 60
        }

        /* K 9 out suit */
        if (playerCards[0].rank == 12 && playerCards[1].rank == 8 && playerCards[0].suit != playerCards[1].suit
            || playerCards[1].rank == 12 && playerCards[0].rank == 8 && playerCards[0].suit != playerCards[1].suit
        ) {
            return 58
        }
        /* K 8 suit */
        if (playerCards[0].rank == 12 && playerCards[1].rank == 7 && playerCards[0].suit == playerCards[1].suit
            || playerCards[1].rank == 12 && playerCards[0].rank == 7 && playerCards[0].suit == playerCards[1].suit
        ) {
            return 58
        }

        /* K 8 out suit */
        if (playerCards[0].rank == 12 && playerCards[1].rank == 7 && playerCards[0].suit != playerCards[1].suit
            || playerCards[1].rank == 12 && playerCards[0].rank == 7 && playerCards[0].suit != playerCards[1].suit
        ) {
            return 56
        }

        /* K 7 suit */
        if (playerCards[0].rank == 12 && playerCards[1].rank == 6 && playerCards[0].suit == playerCards[1].suit
            || playerCards[1].rank == 12 && playerCards[0].rank == 6 && playerCards[0].suit == playerCards[1].suit
        ) {
            return 57
        }

        /* K 7 out suit */
        if (playerCards[0].rank == 12 && playerCards[1].rank == 6 && playerCards[0].suit != playerCards[1].suit
            || playerCards[1].rank == 12 && playerCards[0].rank == 6 && playerCards[0].suit != playerCards[1].suit
        ) {
            return 55
        }

        /* K 6 suit */
        if (playerCards[0].rank == 12 && playerCards[1].rank == 5 && playerCards[0].suit == playerCards[1].suit
            || playerCards[1].rank == 12 && playerCards[0].rank == 5 && playerCards[0].suit == playerCards[1].suit
        ) {
            return 56
        }

        /* K 6 out suit */
        if (playerCards[0].rank == 12 && playerCards[1].rank == 5 && playerCards[0].suit != playerCards[1].suit
            || playerCards[1].rank == 12 && playerCards[0].rank == 5 && playerCards[0].suit != playerCards[1].suit
        ) {
            return 54
        }

        /* K 5 suit */
        if (playerCards[0].rank == 12 && playerCards[1].rank == 4 && playerCards[0].suit == playerCards[1].suit
            || playerCards[1].rank == 12 && playerCards[0].rank == 4 && playerCards[0].suit == playerCards[1].suit
        ) {
            return 55
        }
        /* K 5 out suit */
        if (playerCards[0].rank == 12 && playerCards[1].rank == 4 && playerCards[0].suit != playerCards[1].suit
            || playerCards[1].rank == 12 && playerCards[0].rank == 4 && playerCards[0].suit != playerCards[1].suit
        ) {
            return 53
        }

        /* K 4 suit */
        if (playerCards[0].rank == 12 && playerCards[1].rank == 3 && playerCards[0].suit == playerCards[1].suit
            || playerCards[1].rank == 12 && playerCards[0].rank == 3 && playerCards[0].suit == playerCards[1].suit
        ) {
            return 54
        }

        /* K 4 out suit */
        if (playerCards[0].rank == 12 && playerCards[1].rank == 3 && playerCards[0].suit != playerCards[1].suit
            || playerCards[1].rank == 12 && playerCards[0].rank == 3 && playerCards[0].suit != playerCards[1].suit
        ) {
            return 52
        }

        /* K 3 suit */
        if (playerCards[0].rank == 12 && playerCards[1].rank == 2 && playerCards[0].suit == playerCards[1].suit
            || playerCards[1].rank == 12 && playerCards[0].rank == 2 && playerCards[0].suit == playerCards[1].suit
        ) {
            return 53
        }

        /* K 3 out suit */
        if (playerCards[0].rank == 12 && playerCards[1].rank == 2 && playerCards[0].suit != playerCards[1].suit
            || playerCards[1].rank == 12 && playerCards[0].rank == 2 && playerCards[0].suit != playerCards[1].suit
        ) {
            return 51
        }

        /* K 2 suit */
        if (playerCards[0].rank == 12 && playerCards[1].rank == 1 && playerCards[0].suit == playerCards[1].suit
            || playerCards[1].rank == 12 && playerCards[0].rank == 1 && playerCards[0].suit == playerCards[1].suit
        ) return 52

        /* K 2 out suit */
        if (playerCards[0].rank == 12 && playerCards[1].rank == 1 && playerCards[0].suit != playerCards[1].suit
            || playerCards[1].rank == 12 && playerCards[0].rank == 1 && playerCards[0].suit != playerCards[1].suit
        ) {
            return 50
        }

        /* Q Q */
        if (playerCards[0].rank == 11 && playerCards[1].rank == 11) {
            return 79
        }

        /* Q J suit */
        if (playerCards[0].rank == 11 && playerCards[1].rank == 10 && playerCards[0].suit == playerCards[1].suit
            || playerCards[1].rank == 11 && playerCards[0].rank == 10 && playerCards[0].suit == playerCards[1].suit
        ) {
            return 60
        }
        /* Q J out suit */
        if (playerCards[0].rank == 11 && playerCards[1].rank == 10 && playerCards[0].suit != playerCards[1].suit
            || playerCards[1].rank == 11 && playerCards[0].rank == 10 && playerCards[0].suit != playerCards[1].suit
        ) {
            return 58
        }

        /* Q 10 suit */
        if (playerCards[0].rank == 11 && playerCards[1].rank == 9 && playerCards[0].suit == playerCards[1].suit
            || playerCards[1].rank == 11 && playerCards[0].rank == 9 && playerCards[0].suit == playerCards[1].suit
        ) {
            return 59
        }

        /* Q 10 out suit */
        if (playerCards[0].rank == 11 && playerCards[1].rank == 9 && playerCards[0].suit != playerCards[1].suit
            || playerCards[1].rank == 11 && playerCards[0].rank == 9 && playerCards[0].suit != playerCards[1].suit
        ) {
            return 57
        }

        /* Q 9 suit */
        if (playerCards[0].rank == 11 && playerCards[1].rank == 8 && playerCards[0].suit == playerCards[1].suit
            || playerCards[1].rank == 11 && playerCards[0].rank == 8 && playerCards[0].suit == playerCards[1].suit
        ) {
            return 57
        }

        /* Q 9 out suit */
        if (playerCards[0].rank == 11 && playerCards[1].rank == 8 && playerCards[0].suit != playerCards[1].suit
            || playerCards[1].rank == 11 && playerCards[0].rank == 8 && playerCards[0].suit != playerCards[1].suit
        ) {
            return 55
        }

        /* Q 8 suit */
        if (playerCards[0].rank == 11 && playerCards[1].rank == 7 && playerCards[0].suit == playerCards[1].suit
            || playerCards[1].rank == 11 && playerCards[0].rank == 7 && playerCards[0].suit == playerCards[1].suit
        ) {
            return 56
        }

        /* Q 8 out suit */
        if (playerCards[0].rank == 11 && playerCards[1].rank == 7 && playerCards[0].suit != playerCards[1].suit
            || playerCards[1].rank == 11 && playerCards[0].rank == 7 && playerCards[0].suit != playerCards[1].suit
        ) {
            return 53
        }

        /* Q 7 suit */
        if (playerCards[0].rank == 11 && playerCards[1].rank == 6 && playerCards[0].suit == playerCards[1].suit
            || playerCards[1].rank == 11 && playerCards[0].rank == 6 && playerCards[0].suit == playerCards[1].suit
        ) {
            return 54
        }

        /* Q 7 out suit */
        if (playerCards[0].rank == 11 && playerCards[1].rank == 6 && playerCards[0].suit != playerCards[1].suit
            || playerCards[1].rank == 11 && playerCards[0].rank == 6 && playerCards[0].suit != playerCards[1].suit
        ) {
            return 51
        }

        /* Q 6 suit */
        if (playerCards[0].rank == 11 && playerCards[1].rank == 5 && playerCards[0].suit == playerCards[1].suit
            || playerCards[1].rank == 11 && playerCards[0].rank == 5 && playerCards[0].suit == playerCards[1].suit
        ) {
            return 53
        }

        /* Q 6 out suit */
        if (playerCards[0].rank == 11 && playerCards[1].rank == 5 && playerCards[0].suit != playerCards[1].suit
            || playerCards[1].rank == 11 && playerCards[0].rank == 5 && playerCards[0].suit != playerCards[1].suit
        ) {
            return 51
        }

        /* Q 5 suit */
        if (playerCards[0].rank == 11 && playerCards[1].rank == 4 && playerCards[0].suit == playerCards[1].suit
            || playerCards[1].rank == 11 && playerCards[0].rank == 4 && playerCards[0].suit == playerCards[1].suit
        ) {
            return 52
        }

        /* Q 5 out suit */
        if (playerCards[0].rank == 11 && playerCards[1].rank == 4 && playerCards[0].suit != playerCards[1].suit
            || playerCards[1].rank == 11 && playerCards[0].rank == 4 && playerCards[0].suit != playerCards[1].suit
        ) {
            return 50
        }

        /* Q 4 suit */
        if (playerCards[0].rank == 11 && playerCards[1].rank == 3 && playerCards[0].suit == playerCards[1].suit
            || playerCards[1].rank == 11 && playerCards[0].rank == 3 && playerCards[0].suit == playerCards[1].suit
        ) {
            return 51
        }

        /* Q 4 out suit */
        if (playerCards[0].rank == 11 && playerCards[1].rank == 3 && playerCards[0].suit != playerCards[1].suit
            || playerCards[1].rank == 11 && playerCards[0].rank == 3 && playerCards[0].suit != playerCards[1].suit
        ) {
            return 49
        }

        /* Q 3 suit */

        /* Q 3 suit */if (playerCards[0].rank == 11 && playerCards[1].rank == 2 && playerCards[0]
                .suit == playerCards[1].suit || playerCards[1].rank == 11 && playerCards[0]
                .rank == 2 && playerCards[0].suit == playerCards[1].suit
        ) return 50

        /* Q 3 out suit */

        /* Q 3 out suit */if (playerCards[0].rank == 11 && playerCards[1]
                .rank == 2 && playerCards[0].suit != playerCards[1].suit || playerCards[1]
                .rank == 11 && playerCards[0].rank == 2 && playerCards[0]
                .suit != playerCards[1].suit
        ) return 47

        /* Q 2 suit */

        /* Q 2 suit */if (playerCards[0].rank == 11 && playerCards[1].rank == 1 && playerCards[0]
                .suit == playerCards[1].suit || playerCards[1].rank == 11 && playerCards[0]
                .rank == 1 && playerCards[0].suit == playerCards[1].suit
        ) return 49

        /* Q 2 out suit */

        /* Q 2 out suit */if (playerCards[0].rank == 11 && playerCards[1]
                .rank == 1 && playerCards[0].suit != playerCards[1].suit || playerCards[1]
                .rank == 11 && playerCards[0].rank == 1 && playerCards[0]
                .suit != playerCards[1].suit
        ) return 47

        /*-------------------------------------------------------------------------------------*/

        /* J J */

        /*-------------------------------------------------------------------------------------*/

        /* J J */if (playerCards[0].rank == 10 && playerCards[1].rank == 10) return 77

        /* J 10 suit */

        /* J 10 suit */if (playerCards[0].rank == 10 && playerCards[1]
                .rank == 9 && playerCards[0].suit == playerCards[1].suit || playerCards[1]
                .rank == 10 && playerCards[0].rank == 9 && playerCards[0]
                .suit == playerCards[1].suit
        ) return 57

        /* J 10 out suit */

        /* J 10 out suit */if (playerCards[0].rank == 10 && playerCards[1]
                .rank == 9 && playerCards[0].suit != playerCards[1].suit || playerCards[1]
                .rank == 10 && playerCards[0].rank == 9 && playerCards[0]
                .suit != playerCards[1].suit
        ) return 55

        /* J 9 suit */

        /* J 9 suit */if (playerCards[0].rank == 10 && playerCards[1].rank == 8 && playerCards[0]
                .suit == playerCards[1].suit || playerCards[1].rank == 10 && playerCards[0]
                .rank == 8 && playerCards[0].suit == playerCards[1].suit
        ) return 55

        /* J 9 out suit */

        /* J 9 out suit */if (playerCards[0].rank == 10 && playerCards[1]
                .rank == 8 && playerCards[0].suit != playerCards[1].suit || playerCards[1]
                .rank == 10 && playerCards[0].rank == 8 && playerCards[0]
                .suit != playerCards[1].suit
        ) return 53

        /* J 8 suit */

        /* J 8 suit */if (playerCards[0].rank == 10 && playerCards[1].rank == 7 && playerCards[0]
                .suit == playerCards[1].suit || playerCards[1].rank == 10 && playerCards[0]
                .rank == 7 && playerCards[0].suit == playerCards[1].suit
        ) return 54

        /* J 8 out suit */

        /* J 8 out suit */if (playerCards[0].rank == 10 && playerCards[1]
                .rank == 7 && playerCards[0].suit != playerCards[1].suit || playerCards[1]
                .rank == 10 && playerCards[0].rank == 7 && playerCards[0]
                .suit != playerCards[1].suit
        ) return 51

        /* J 7 suit */

        /* J 7 suit */if (playerCards[0].rank == 10 && playerCards[1].rank == 6 && playerCards[0]
                .suit == playerCards[1].suit || playerCards[1].rank == 10 && playerCards[0]
                .rank == 6 && playerCards[0].suit == playerCards[1].suit
        ) return 52

        /* J 7 out suit */

        /* J 7 out suit */if (playerCards[0].rank == 10 && playerCards[1]
                .rank == 6 && playerCards[0].suit != playerCards[1].suit || playerCards[1]
                .rank == 10 && playerCards[0].rank == 6 && playerCards[0]
                .suit != playerCards[1].suit
        ) return 49

        /* J 6 suit */

        /* J 6 suit */if (playerCards[0].rank == 10 && playerCards[1].rank == 5 && playerCards[0]
                .suit == playerCards[1].suit || playerCards[1].rank == 10 && playerCards[0]
                .rank == 5 && playerCards[0].suit == playerCards[1].suit
        ) return 50

        /* J 6 out suit */

        /* J 6 out suit */if (playerCards[0].rank == 10 && playerCards[1]
                .rank == 5 && playerCards[0].suit != playerCards[1].suit || playerCards[1]
                .rank == 10 && playerCards[0].rank == 5 && playerCards[0]
                .suit != playerCards[1].suit
        ) return 47

        /* J 5 suit */

        /* J 5 suit */if (playerCards[0].rank == 10 && playerCards[1].rank == 4 && playerCards[0]
                .suit == playerCards[1].suit || playerCards[1].rank == 10 && playerCards[0]
                .rank == 4 && playerCards[0].suit == playerCards[1].suit
        ) return 50

        /* J 5 out suit */

        /* J 5 out suit */if (playerCards[0].rank == 10 && playerCards[1]
                .rank == 4 && playerCards[0].suit != playerCards[1].suit || playerCards[1]
                .rank == 10 && playerCards[0].rank == 4 && playerCards[0]
                .suit != playerCards[1].suit
        ) return 47

        /* J 4 suit */

        /* J 4 suit */if (playerCards[0].rank == 10 && playerCards[1].rank == 3 && playerCards[0]
                .suit == playerCards[1].suit || playerCards[1].rank == 10 && playerCards[0]
                .rank == 3 && playerCards[0].suit == playerCards[1].suit
        ) return 49

        /* J 4 out suit */

        /* J 4 out suit */if (playerCards[0].rank == 10 && playerCards[1]
                .rank == 3 && playerCards[0].suit != playerCards[1].suit || playerCards[1]
                .rank == 10 && playerCards[0].rank == 3 && playerCards[0]
                .suit != playerCards[1].suit
        ) return 46

        /* J 3 suit */

        /* J 3 suit */if (playerCards[0].rank == 10 && playerCards[1].rank == 2 && playerCards[0]
                .suit == playerCards[1].suit || playerCards[1].rank == 10 && playerCards[0]
                .rank == 2 && playerCards[0].suit == playerCards[1].suit
        ) return 47

        /* J 3 out suit */

        /* J 3 out suit */if (playerCards[0].rank == 10 && playerCards[1]
                .rank == 2 && playerCards[0].suit != playerCards[1].suit || playerCards[1]
                .rank == 10 && playerCards[0].rank == 2 && playerCards[0]
                .suit != playerCards[1].suit
        ) return 45

        /* J 2 suit */

        /* J 2 suit */if (playerCards[0].rank == 10 && playerCards[1].rank == 1 && playerCards[0]
                .suit == playerCards[1].suit || playerCards[1].rank == 10 && playerCards[0]
                .rank == 1 && playerCards[0].suit == playerCards[1].suit
        ) return 47

        /* J 2 out suit */

        /* J 2 out suit */if (playerCards[0].rank == 10 && playerCards[1]
                .rank == 1 && playerCards[0].suit != playerCards[1].suit || playerCards[1]
                .rank == 10 && playerCards[0].rank == 1 && playerCards[0]
                .suit != playerCards[1].suit
        ) return 44

        /*-------------------------------------------------------------------------------------*/

        /* 10 10 */

        /*-------------------------------------------------------------------------------------*/

        /* 10 10 */if (playerCards[0].rank == 9 && playerCards[1].rank == 9) return 75

        /* 10 9 suit */

        /* 10 9 suit */if (playerCards[0].rank == 9 && playerCards[1].rank == 8 && playerCards[0]
                .suit == playerCards[1].suit || playerCards[1].rank == 9 && playerCards[0]
                .rank == 8 && playerCards[0].suit == playerCards[1].suit
        ) return 54

        /* 10 9 out suit */

        /* 10 9 out suit */if (playerCards[0].rank == 9 && playerCards[1]
                .rank == 8 && playerCards[0].suit != playerCards[1].suit || playerCards[1]
                .rank == 9 && playerCards[0].rank == 8 && playerCards[0]
                .suit != playerCards[1].suit
        ) return 51

        /* 10 8 suit */

        /* 10 8 suit */if (playerCards[0].rank == 9 && playerCards[1].rank == 7 && playerCards[0]
                .suit == playerCards[1].suit || playerCards[1].rank == 9 && playerCards[0]
                .rank == 7 && playerCards[0].suit == playerCards[1].suit
        ) return 52

        /* 10 8 out suit */

        /* 10 8 out suit */if (playerCards[0].rank == 9 && playerCards[1]
                .rank == 7 && playerCards[0].suit != playerCards[1].suit || playerCards[1]
                .rank == 9 && playerCards[0].rank == 7 && playerCards[0]
                .suit != playerCards[1].suit
        ) return 50

        /* 10 7 suit */

        /* 10 7 suit */if (playerCards[0].rank == 9 && playerCards[1].rank == 6 && playerCards[0]
                .suit == playerCards[1].suit || playerCards[1].rank == 9 && playerCards[0]
                .rank == 6 && playerCards[0].suit == playerCards[1].suit
        ) return 51

        /* 10 7 out suit */

        /* 10 7 out suit */if (playerCards[0].rank == 9 && playerCards[1]
                .rank == 6 && playerCards[0].suit != playerCards[1].suit || playerCards[1]
                .rank == 9 && playerCards[0].rank == 6 && playerCards[0]
                .suit != playerCards[1].suit
        ) return 48

        /* 10 6 suit */

        /* 10 6 suit */if (playerCards[0].rank == 9 && playerCards[1].rank == 5 && playerCards[0]
                .suit == playerCards[1].suit || playerCards[1].rank == 9 && playerCards[0]
                .rank == 5 && playerCards[0].suit == playerCards[1].suit
        ) return 49

        /* 10 6 out suit */

        /* 10 6 out suit */if (playerCards[0].rank == 9 && playerCards[1]
                .rank == 5 && playerCards[0].suit != playerCards[1].suit || playerCards[1]
                .rank == 9 && playerCards[0].rank == 5 && playerCards[0]
                .suit != playerCards[1].suit
        ) return 46

        /* 10 5 suit */

        /* 10 5 suit */if (playerCards[0].rank == 9 && playerCards[1].rank == 4 && playerCards[0]
                .suit == playerCards[1].suit || playerCards[1].rank == 9 && playerCards[0]
                .rank == 4 && playerCards[0].suit == playerCards[1].suit
        ) return 47

        /* 10 5 out suit */

        /* 10 5 out suit */if (playerCards[0].rank == 9 && playerCards[1]
                .rank == 4 && playerCards[0].suit != playerCards[1].suit || playerCards[1]
                .rank == 9 && playerCards[0].rank == 4 && playerCards[0]
                .suit != playerCards[1].suit
        ) return 44

        /* 10 4 suit */

        /* 10 4 suit */if (playerCards[0].rank == 9 && playerCards[1].rank == 3 && playerCards[0]
                .suit == playerCards[1].suit || playerCards[1].rank == 9 && playerCards[0]
                .rank == 3 && playerCards[0].suit == playerCards[1].suit
        ) return 46

        /* 10 4 out suit */

        /* 10 4 out suit */if (playerCards[0].rank == 9 && playerCards[1]
                .rank == 3 && playerCards[0].suit != playerCards[1].suit || playerCards[1]
                .rank == 9 && playerCards[0].rank == 3 && playerCards[0]
                .suit != playerCards[1].suit
        ) return 43

        /* 10 3 suit */

        /* 10 3 suit */if (playerCards[0].rank == 9 && playerCards[1].rank == 2 && playerCards[0]
                .suit == playerCards[1].suit || playerCards[1].rank == 9 && playerCards[0]
                .rank == 2 && playerCards[0].suit == playerCards[1].suit
        ) return 45

        /* 10 3 out suit */

        /* 10 3 out suit */if (playerCards[0].rank == 9 && playerCards[1]
                .rank == 2 && playerCards[0].suit != playerCards[1].suit || playerCards[1]
                .rank == 9 && playerCards[0].rank == 2 && playerCards[0]
                .suit != playerCards[1].suit
        ) return 42

        /* 10 2 suit */

        /* 10 2 suit */if (playerCards[0].rank == 9 && playerCards[1].rank == 1 && playerCards[0]
                .suit == playerCards[1].suit || playerCards[1].rank == 9 && playerCards[0]
                .rank == 1 && playerCards[0].suit == playerCards[1].suit
        ) return 44

        /* 10 2 out suit */

        /* 10 2 out suit */if (playerCards[0].rank == 9 && playerCards[1]
                .rank == 1 && playerCards[0].suit != playerCards[1].suit || playerCards[1]
                .rank == 9 && playerCards[0].rank == 1 && playerCards[0]
                .suit != playerCards[1].suit
        ) return 41

        /*-----------------------------------------------------------------------------------*/

        /* 9 9 */

        /*-----------------------------------------------------------------------------------*/

        /* 9 9 */if (playerCards[0].rank == 8 && playerCards[1].rank == 8) return 72

        /* 9 8 suit */

        /* 9 8 suit */if (playerCards[0].rank == 8 && playerCards[1].rank == 7 && playerCards[0]
                .suit == playerCards[1].suit || playerCards[1].rank == 8 && playerCards[0]
                .rank == 7 && playerCards[0].suit == playerCards[1].suit
        ) return 51

        /* 9 8 out suit */

        /* 9 8 out suit */if (playerCards[0].rank == 8 && playerCards[1]
                .rank == 7 && playerCards[0].suit != playerCards[1].suit || playerCards[1]
                .rank == 8 && playerCards[0].rank == 7 && playerCards[0]
                .suit != playerCards[1].suit
        ) return 48

        /* 9 7 suit */

        /* 9 7 suit */if (playerCards[0].rank == 8 && playerCards[1].rank == 6 && playerCards[0]
                .suit == playerCards[1].suit || playerCards[1].rank == 8 && playerCards[0]
                .rank == 6 && playerCards[0].suit == playerCards[1].suit
        ) return 49

        /* 9 7 out suit */

        /* 9 7 out suit */if (playerCards[0].rank == 8 && playerCards[1]
                .rank == 6 && playerCards[0].suit != playerCards[1].suit || playerCards[1]
                .rank == 8 && playerCards[0].rank == 6 && playerCards[0]
                .suit != playerCards[1].suit
        ) return 46

        /* 9 6 suit */

        /* 9 6 suit */if (playerCards[0].rank == 8 && playerCards[1].rank == 5 && playerCards[0]
                .suit == playerCards[1].suit || playerCards[1].rank == 8 && playerCards[0]
                .rank == 5 && playerCards[0].suit == playerCards[1].suit
        ) return 47

        /* 9 6 out suit */

        /* 9 6 out suit */if (playerCards[0].rank == 8 && playerCards[1]
                .rank == 5 && playerCards[0].suit != playerCards[1].suit || playerCards[1]
                .rank == 8 && playerCards[0].rank == 5 && playerCards[0]
                .suit != playerCards[1].suit
        ) return 44

        /* 9 5 suit */

        /* 9 5 suit */if (playerCards[0].rank == 8 && playerCards[1].rank == 4 && playerCards[0]
                .suit == playerCards[1].suit || playerCards[1].rank == 8 && playerCards[0]
                .rank == 4 && playerCards[0].suit == playerCards[1].suit
        ) return 45

        /* 9 5 out suit */

        /* 9 5 out suit */if (playerCards[0].rank == 8 && playerCards[1]
                .rank == 4 && playerCards[0].suit != playerCards[1].suit || playerCards[1]
                .rank == 8 && playerCards[0].rank == 4 && playerCards[0]
                .suit != playerCards[1].suit
        ) return 42

        /* 9 4 suit */

        /* 9 4 suit */if (playerCards[0].rank == 8 && playerCards[1].rank == 3 && playerCards[0]
                .suit == playerCards[1].suit || playerCards[1].rank == 8 && playerCards[0]
                .rank == 3 && playerCards[0].suit == playerCards[1].suit
        ) return 43

        /* 9 4 out suit */

        /* 9 4 out suit */if (playerCards[0].rank == 8 && playerCards[1]
                .rank == 3 && playerCards[0].suit != playerCards[1].suit || playerCards[1]
                .rank == 8 && playerCards[0].rank == 3 && playerCards[0]
                .suit != playerCards[1].suit
        ) return 40

        /* 9 3 suit */

        /* 9 3 suit */if (playerCards[0].rank == 8 && playerCards[1].rank == 2 && playerCards[0]
                .suit == playerCards[1].suit || playerCards[1].rank == 8 && playerCards[0]
                .rank == 2 && playerCards[0].suit == playerCards[1].suit
        ) return 43

        /* 9 3 out suit */

        /* 9 3 out suit */if (playerCards[0].rank == 8 && playerCards[1]
                .rank == 2 && playerCards[0].suit != playerCards[1].suit || playerCards[1]
                .rank == 8 && playerCards[0].rank == 2 && playerCards[0]
                .suit != playerCards[1].suit
        ) return 39

        /* 9 2 suit */

        /* 9 2 suit */if (playerCards[0].rank == 8 && playerCards[1].rank == 1 && playerCards[0]
                .suit == playerCards[1].suit || playerCards[1].rank == 8 && playerCards[0]
                .rank == 1 && playerCards[0].suit == playerCards[1].suit
        ) return 42

        /* 9 2 out suit */

        /* 9 2 out suit */if (playerCards[0].rank == 8 && playerCards[1]
                .rank == 1 && playerCards[0].suit != playerCards[1].suit || playerCards[1]
                .rank == 8 && playerCards[0].rank == 1 && playerCards[0]
                .suit != playerCards[1].suit
        ) return 38

        /*-------------------------------------------------------------------------------------*/

        /* 8 8 */

        /*-------------------------------------------------------------------------------------*/

        /* 8 8 */if (playerCards[0].rank == 7 && playerCards[1].rank == 7) return 69

        /* 8 7 suit */

        /* 8 7 suit */if (playerCards[0].rank == 7 && playerCards[1].rank == 6 && playerCards[0]
                .suit == playerCards[1].suit || playerCards[1].rank == 7 && playerCards[0]
                .rank == 6 && playerCards[0].suit == playerCards[1].suit
        ) return 48

        /* 8 7 out suit */

        /* 8 7 out suit */if (playerCards[0].rank == 7 && playerCards[1]
                .rank == 6 && playerCards[0].suit != playerCards[1].suit || playerCards[1]
                .rank == 7 && playerCards[0].rank == 6 && playerCards[0]
                .suit != playerCards[1].suit
        ) return 45

        /* 8 6 suit */

        /* 8 6 suit */if (playerCards[0].rank == 7 && playerCards[1].rank == 5 && playerCards[0]
                .suit == playerCards[1].suit || playerCards[1].rank == 7 && playerCards[0]
                .rank == 5 && playerCards[0].suit == playerCards[1].suit
        ) return 46

        /* 8 6 out suit */

        /* 8 6 out suit */if (playerCards[0].rank == 7 && playerCards[1]
                .rank == 5 && playerCards[0].suit != playerCards[1].suit || playerCards[1]
                .rank == 7 && playerCards[0].rank == 5 && playerCards[0]
                .suit != playerCards[1].suit
        ) return 43

        /* 8 5 suit */

        /* 8 5 suit */if (playerCards[0].rank == 7 && playerCards[1].rank == 4 && playerCards[0]
                .suit == playerCards[1].suit || playerCards[1].rank == 7 && playerCards[0]
                .rank == 4 && playerCards[0].suit == playerCards[1].suit
        ) return 44

        /* 8 5 out suit */

        /* 8 5 out suit */if (playerCards[0].rank == 7 && playerCards[1]
                .rank == 4 && playerCards[0].suit != playerCards[1].suit || playerCards[1]
                .rank == 7 && playerCards[0].rank == 4 && playerCards[0]
                .suit != playerCards[1].suit
        ) return 41

        /* 8 4 suit */

        /* 8 4 suit */if (playerCards[0].rank == 7 && playerCards[1].rank == 3 && playerCards[0]
                .suit == playerCards[1].suit || playerCards[1].rank == 7 && playerCards[0]
                .rank == 3 && playerCards[0].suit == playerCards[1].suit
        ) return 42

        /* 8 4 out suit */

        /* 8 4 out suit */if (playerCards[0].rank == 7 && playerCards[1]
                .rank == 3 && playerCards[0].suit != playerCards[1].suit || playerCards[1]
                .rank == 7 && playerCards[0].rank == 3 && playerCards[0]
                .suit != playerCards[1].suit
        ) return 39

        /* 8 3 suit */

        /* 8 3 suit */if (playerCards[0].rank == 7 && playerCards[1].rank == 2 && playerCards[0]
                .suit == playerCards[1].suit || playerCards[1].rank == 7 && playerCards[0]
                .rank == 2 && playerCards[0].suit == playerCards[1].suit
        ) return 40

        /* 8 3 out suit */

        /* 8 3 out suit */if (playerCards[0].rank == 7 && playerCards[1]
                .rank == 2 && playerCards[0].suit != playerCards[1].suit || playerCards[1]
                .rank == 7 && playerCards[0].rank == 2 && playerCards[0]
                .suit != playerCards[1].suit
        ) return 37

        /* 8 2 suit */

        /* 8 2 suit */if (playerCards[0].rank == 7 && playerCards[1].rank == 1 && playerCards[0]
                .suit == playerCards[1].suit || playerCards[1].rank == 7 && playerCards[0]
                .rank == 1 && playerCards[0].suit == playerCards[1].suit
        ) return 40

        /* 8 2 out suit */

        /* 8 2 out suit */if (playerCards[0].rank == 7 && playerCards[1]
                .rank == 1 && playerCards[0].suit != playerCards[1].suit || playerCards[1]
                .rank == 7 && playerCards[0].rank == 1 && playerCards[0]
                .suit != playerCards[1].suit
        ) return 36

        /*-------------------------------------------------------------------------------------*/

        /* 7 7 */

        /*-------------------------------------------------------------------------------------*/

        /* 7 7 */if (playerCards[0].rank == 6 && playerCards[1].rank == 6) return 66

        /* 7 6 suit */

        /* 7 6 suit */if (playerCards[0].rank == 6 && playerCards[1].rank == 5 && playerCards[0]
                .suit == playerCards[1].suit || playerCards[1].rank == 6 && playerCards[0]
                .rank == 5 && playerCards[0].suit == playerCards[1].suit
        ) return 45

        /* 7 6 out suit */

        /* 7 6 out suit */if (playerCards[0].rank == 6 && playerCards[1]
                .rank == 5 && playerCards[0].suit != playerCards[1].suit || playerCards[1]
                .rank == 6 && playerCards[0].rank == 5 && playerCards[0]
                .suit != playerCards[1].suit
        ) return 42

        /* 7 5 suit */

        /* 7 5 suit */if (playerCards[0].rank == 6 && playerCards[1].rank == 4 && playerCards[0]
                .suit == playerCards[1].suit || playerCards[1].rank == 6 && playerCards[0]
                .rank == 4 && playerCards[0].suit == playerCards[1].suit
        ) return 43

        /* 7 5 out suit */

        /* 7 5 out suit */if (playerCards[0].rank == 6 && playerCards[1]
                .rank == 4 && playerCards[0].suit != playerCards[1].suit || playerCards[1]
                .rank == 6 && playerCards[0].rank == 4 && playerCards[0]
                .suit != playerCards[1].suit
        ) return 40

        /* 7 4 suit */

        /* 7 4 suit */if (playerCards[0].rank == 6 && playerCards[1].rank == 3 && playerCards[0]
                .suit == playerCards[1].suit || playerCards[1].rank == 6 && playerCards[0]
                .rank == 3 && playerCards[0].suit == playerCards[1].suit
        ) return 41

        /* 7 4 out suit */

        /* 7 4 out suit */if (playerCards[0].rank == 6 && playerCards[1]
                .rank == 3 && playerCards[0].suit != playerCards[1].suit || playerCards[1]
                .rank == 6 && playerCards[0].rank == 3 && playerCards[0]
                .suit != playerCards[1].suit
        ) return 38

        /* 7 3 suit */

        /* 7 3 suit */if (playerCards[0].rank == 6 && playerCards[1].rank == 2 && playerCards[0]
                .suit == playerCards[1].suit || playerCards[1].rank == 6 && playerCards[0]
                .rank == 2 && playerCards[0].suit == playerCards[1].suit
        ) return 40

        /* 7 3 out suit */

        /* 7 3 out suit */if (playerCards[0].rank == 6 && playerCards[1]
                .rank == 2 && playerCards[0].suit != playerCards[1].suit || playerCards[1]
                .rank == 6 && playerCards[0].rank == 2 && playerCards[0]
                .suit != playerCards[1].suit
        ) return 36

        /* 7 2 suit */

        /* 7 2 suit */if (playerCards[0].rank == 6 && playerCards[1].rank == 1 && playerCards[0]
                .suit == playerCards[1].suit || playerCards[1].rank == 6 && playerCards[0]
                .rank == 1 && playerCards[0].suit == playerCards[1].suit
        ) return 38

        /* 7 2 out suit */

        /* 7 2 out suit */if (playerCards[0].rank == 6 && playerCards[1]
                .rank == 1 && playerCards[0].suit != playerCards[1].suit || playerCards[1]
                .rank == 6 && playerCards[0].rank == 1 && playerCards[0]
                .suit != playerCards[1].suit
        ) return 34

        /*-------------------------------------------------------------------------------------*/

        /* 6 6 */

        /*-------------------------------------------------------------------------------------*/

        /* 6 6 */if (playerCards[0].rank == 5 && playerCards[1].rank == 5) return 63

        /* 6 5 suit */

        /* 6 5 suit */if (playerCards[0].rank == 5 && playerCards[1].rank == 4 && playerCards[0]
                .suit == playerCards[1].suit || playerCards[1].rank == 5 && playerCards[0]
                .rank == 4 && playerCards[0].suit == playerCards[1].suit
        ) return 43

        /* 6 5 out suit */

        /* 6 5 out suit */if (playerCards[0].rank == 5 && playerCards[1]
                .rank == 4 && playerCards[0].suit != playerCards[1].suit || playerCards[1]
                .rank == 5 && playerCards[0].rank == 4 && playerCards[0]
                .suit != playerCards[1].suit
        ) return 40

        /* 6 4 suit */

        /* 6 4 suit */if (playerCards[0].rank == 5 && playerCards[1].rank == 3 && playerCards[0]
                .suit == playerCards[1].suit || playerCards[1].rank == 5 && playerCards[0]
                .rank == 3 && playerCards[0].suit == playerCards[1].suit
        ) return 41

        /* 6 4 out suit */

        /* 6 4 out suit */if (playerCards[0].rank == 5 && playerCards[1]
                .rank == 3 && playerCards[0].suit != playerCards[1].suit || playerCards[1]
                .rank == 5 && playerCards[0].rank == 3 && playerCards[0]
                .suit != playerCards[1].suit
        ) return 38

        /* 6 3 suit */

        /* 6 3 suit */if (playerCards[0].rank == 5 && playerCards[1].rank == 2 && playerCards[0]
                .suit == playerCards[1].suit || playerCards[1].rank == 5 && playerCards[0]
                .rank == 2 && playerCards[0].suit == playerCards[1].suit
        ) return 39

        /* 6 3 out suit */

        /* 6 3 out suit */if (playerCards[0].rank == 5 && playerCards[1]
                .rank == 2 && playerCards[0].suit != playerCards[1].suit || playerCards[1]
                .rank == 5 && playerCards[0].rank == 2 && playerCards[0]
                .suit != playerCards[1].suit
        ) return 35

        /* 6 2 suit */

        /* 6 2 suit */if (playerCards[0].rank == 5 && playerCards[1].rank == 1 && playerCards[0]
                .suit == playerCards[1].suit || playerCards[1].rank == 5 && playerCards[0]
                .rank == 1 && playerCards[0].suit == playerCards[1].suit
        ) return 37

        /* 6 2 out suit */

        /* 6 2 out suit */if (playerCards[0].rank == 5 && playerCards[1]
                .rank == 1 && playerCards[0].suit != playerCards[1].suit || playerCards[1]
                .rank == 5 && playerCards[0].rank == 1 && playerCards[0]
                .suit != playerCards[1].suit
        ) return 34

        /*-------------------------------------------------------------------------------------*/

        /* 5 5 */

        /*-------------------------------------------------------------------------------------*/

        /* 5 5 */if (playerCards[0].rank == 4 && playerCards[1].rank == 4) return 60

        /* 5 4 suit */

        /* 5 4 suit */if (playerCards[0].rank == 4 && playerCards[1].rank == 3 && playerCards[0]
                .suit == playerCards[1].suit || playerCards[1].rank == 4 && playerCards[0]
                .rank == 3 && playerCards[0].suit == playerCards[1].suit
        ) return 41

        /* 5 4 out suit */

        /* 5 4 out suit */if (playerCards[0].rank == 4 && playerCards[1]
                .rank == 3 && playerCards[0].suit != playerCards[1].suit || playerCards[1]
                .rank == 4 && playerCards[0].rank == 3 && playerCards[0]
                .suit != playerCards[1].suit
        ) return 37

        /* 5 3 suit */

        /* 5 3 suit */if (playerCards[0].rank == 4 && playerCards[1].rank == 2 && playerCards[0]
                .suit == playerCards[1].suit || playerCards[1].rank == 4 && playerCards[0]
                .rank == 2 && playerCards[0].suit == playerCards[1].suit
        ) return 39

        /* 5 3 out suit */

        /* 5 3 out suit */if (playerCards[0].rank == 4 && playerCards[1]
                .rank == 2 && playerCards[0].suit != playerCards[1].suit || playerCards[1]
                .rank == 4 && playerCards[0].rank == 2 && playerCards[0]
                .suit != playerCards[1].suit
        ) return 35

        /* 5 2 suit */

        /* 5 2 suit */if (playerCards[0].rank == 4 && playerCards[1].rank == 1 && playerCards[0]
                .suit == playerCards[1].suit || playerCards[1].rank == 4 && playerCards[0]
                .rank == 1 && playerCards[0].suit == playerCards[1].suit
        ) return 37

        /* 5 2 out suit */

        /* 5 2 out suit */if (playerCards[0].rank == 4 && playerCards[1]
                .rank == 1 && playerCards[0].suit != playerCards[1].suit || playerCards[1]
                .rank == 4 && playerCards[0].rank == 1 && playerCards[0]
                .suit != playerCards[1].suit
        ) return 33

        /*-------------------------------------------------------------------------------------*/

        /* 4 4 */

        /*-------------------------------------------------------------------------------------*/

        /* 4 4 */if (playerCards[0].rank == 3 && playerCards[1].rank == 3) return 57

        /* 4 3 suit */

        /* 4 3 suit */if (playerCards[0].rank == 3 && playerCards[1].rank == 2 && playerCards[0]
                .suit == playerCards[1].suit || playerCards[1].rank == 3 && playerCards[0]
                .rank == 2 && playerCards[0].suit == playerCards[1].suit
        ) return 38

        /* 4 3 out suit */

        /* 4 3 out suit */if (playerCards[0].rank == 3 && playerCards[1]
                .rank == 2 && playerCards[0].suit != playerCards[1].suit || playerCards[1]
                .rank == 3 && playerCards[0].rank == 2 && playerCards[0]
                .suit != playerCards[1].suit
        ) return 34

        /* 4 2 suit */

        /* 4 2 suit */if (playerCards[0].rank == 3 && playerCards[1].rank == 1 && playerCards[0]
                .suit == playerCards[1].suit || playerCards[1].rank == 3 && playerCards[0]
                .rank == 1 && playerCards[0].suit == playerCards[1].suit
        ) return 36

        /* 4 2 out suit */

        /* 4 2 out suit */if (playerCards[0].rank == 3 && playerCards[1]
                .rank == 1 && playerCards[0].suit != playerCards[1].suit || playerCards[1]
                .rank == 3 && playerCards[0].rank == 1 && playerCards[0]
                .suit != playerCards[1].suit
        ) return 32

        /*-------------------------------------------------------------------------------------*/

        /* 3 3 */

        /*-------------------------------------------------------------------------------------*/

        /* 3 3 */if (playerCards[0].rank == 2 && playerCards[1].rank == 2) return 53

        /* 3 2 suit */

        /* 3 2 suit */if (playerCards[0].rank == 2 && playerCards[1].rank == 1 && playerCards[0]
                .suit == playerCards[1].suit || playerCards[1].rank == 2 && playerCards[0]
                .rank == 1 && playerCards[0].suit == playerCards[1].suit
        ) return 35

        /* 3 2 out suit */

        /* 3 2 out suit */if (playerCards[0].rank == 2 && playerCards[1]
                .rank == 1 && playerCards[0].suit != playerCards[1].suit || playerCards[1]
                .rank == 2 && playerCards[0].rank == 1 && playerCards[0]
                .suit != playerCards[1].suit
        ) return 31

        /*-------------------------------------------------------------------------------------*/

        /* 2 2 */

        /*-------------------------------------------------------------------------------------*/

        /* 2 2 */return if (playerCards[0].rank == 1 && playerCards[1].rank == 1) 50 else 0

    }

}