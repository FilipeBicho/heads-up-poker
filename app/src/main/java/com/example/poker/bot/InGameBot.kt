package com.example.poker.bot

class InGameBot: Bot() {

    init {
        initValues()
    }

    override fun initValues() {
        super.initValues()
    }

    override fun calculateAction(): Int {


        return FOLD
    }
}