package com.unal.multiplayertictactoe.backCommunicationImp.domain.entities

class GamesDataCollection : ArrayList<GamesDataCollectionItem>()

data class GamesDataCollectionItem(
    val __v: Int,
    val _id: String,
    val matrix: String,
    val name: String,
    val player1: String,
    val player2: String,
    val status: String,
    val token: String,
    val winner: String
)

