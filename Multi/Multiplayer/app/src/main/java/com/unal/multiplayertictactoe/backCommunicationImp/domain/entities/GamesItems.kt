package com.unal.multiplayertictactoe.backCommunicationImp.domain.entities

import java.io.Serializable

data class GamesItems (
    var name: String,
    var token: String
): Serializable