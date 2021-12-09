package com.unal.multiplayertictactoe.backCommunicationImp.domain.interfaces


import com.unal.multiplayertictactoe.backCommunicationImp.domain.entities.CreateGame
import com.unal.multiplayertictactoe.backCommunicationImp.domain.entities.GameItem
import com.unal.multiplayertictactoe.backCommunicationImp.domain.entities.GamesDataCollectionItem
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface GamesService {

    @GET("/")
    suspend fun listGames(): Response<List<GamesDataCollectionItem>>

    @POST("/join_game")
    suspend fun JoinGame(@Body requestBody: RequestBody): Response<CreateGame>

    @POST("/create_game")
    suspend fun CreateGame(@Body requestBody: RequestBody): Response<CreateGame>

    @POST("/Game")
    suspend fun postGame(@Body requestBody: RequestBody): Response<ResponseBody>

    @POST("/getgame")
    suspend fun getGame(@Body requestBody: RequestBody): Response<GameItem>

}

//https://www.youtube.com/watch?v=7BRAB6CMlug&ab_channel=applikdos
//https://johncodeos.com/how-to-make-post-get-put-and-delete-requests-with-retrofit-using-kotlin/