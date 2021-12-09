package com.unal.multiplayertictactoe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.unal.multiplayertictactoe.backCommunicationImp.BackCommunication
import com.unal.multiplayertictactoe.game.CreateGame
import com.unal.multiplayertictactoe.game.JoinGame

class MainActivity : AppCompatActivity() {
    private var back = BackCommunication()
    private var btnNewGame: Button? = null
    private var btnJoinGame: Button? = null
    private var btnExit: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnNewGame = findViewById(R.id.btn_newgame)
        btnJoinGame = findViewById(R.id.btn_joingame)
        btnExit = findViewById(R.id.btn_exit)


        btnNewGame!!.setOnClickListener {
            val intent = Intent(this, CreateGame::class.java)
            startActivity(intent)
        }
        btnJoinGame!!.setOnClickListener {
            val intent = Intent(this, JoinGame::class.java)
            startActivity(intent)
            //joinGame()
          //  listGames()
        }
        btnExit!!.setOnClickListener {
           /* moveTaskToBack(true)
            exitProcess(-1)*/
        }
    }



}

