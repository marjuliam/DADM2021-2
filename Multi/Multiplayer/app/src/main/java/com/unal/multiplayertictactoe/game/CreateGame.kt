package com.unal.multiplayertictactoe.game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.unal.multiplayertictactoe.backCommunicationImp.RestEngine
import com.unal.multiplayertictactoe.backCommunicationImp.domain.interfaces.GamesService
import com.unal.multiplayertictactoe.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class CreateGame : AppCompatActivity() {

    private var txtName : EditText? = null
    private var btn_new: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_game)

        txtName= findViewById(R.id.txt_nameNew)
        btn_new = findViewById(R.id.btn_newgame)

        btn_new!!.setOnClickListener {
            createGame()
        }
    }

    fun createGame(){
       var name = txtName!!.text

        // Create JSON using JSONObject
        val jsonObject = JSONObject()
        jsonObject.put("player1", "player1")
        jsonObject.put("name", name)

        // Convert JSONObject to String
        val jsonObjectString = jsonObject.toString()

        val gamesService :  GamesService = RestEngine.getRestEngine().create(GamesService::class.java)
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())


        CoroutineScope(Dispatchers.IO).launch {
            val response = gamesService.CreateGame(requestBody)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {

                    var tockenCreate=  response.body()!!.token
                    next(tockenCreate)

                }else {
                    Log.e("RETROFIT_ERROR", response.code().toString())
                }
            }
        }
    }


    fun next(token: String){
        val intent = Intent(this, MainGame::class.java)
        intent.putExtra("token", token)
        intent.putExtra("name", txtName!!.text)
        intent.putExtra("letter", "X")
        intent.putExtra("gammer", "1")
        startActivity(intent)
    }
}
