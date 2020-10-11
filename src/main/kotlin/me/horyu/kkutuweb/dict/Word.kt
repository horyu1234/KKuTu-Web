package me.horyu.kkutuweb.dict

data class Word(
        val id: String,
        val type: String,
        val mean: String,
        val hit: Int,
        val flag: Int,
        val theme: String
)