package com.unal.multiplayertictactoe.game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.unal.multiplayertictactoe.backCommunicationImp.RestEngine
import com.unal.multiplayertictactoe.backCommunicationImp.domain.entities.GamesItems
import com.unal.multiplayertictactoe.backCommunicationImp.domain.interfaces.GamesService
import com.unal.multiplayertictactoe.game.adapter.AdapterRecyclerVertical
import com.unal.multiplayertictactoe.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class JoinGame : AppCompatActivity() {
    var main_item_H: RecyclerView? = null
    var adaptador_V: AdapterRecyclerVertical? = null
    var layoutmanager: RecyclerView.LayoutManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_game)
        main_item_H = findViewById(R.id.recycler_vertical)
        listGames()
    }

    fun listGames(){
        val gamesService : GamesService = RestEngine.getRestEngine().create(GamesService::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            val response = gamesService.listGames()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    var games : GamesItems ? = null
                    var items_blog: ArrayList<GamesItems> = ArrayList()
                    for(y in response.body()!!){
                        System.out.println(y.name+"----"+y.token)
                         var name = y.name
                         var token = y.token

                        var item = GamesItems(name,token)
                         items_blog!!.add(item)
                    }
                    v(items_blog!!)
                } else {

                    Log.e("RETROFIT_ERROR", response.code().toString())

                }
            }
        }
    }

    fun v(items_blog: ArrayList<GamesItems>){
        //Vertical recycler
        adaptador_V = AdapterRecyclerVertical(items_blog, "")
        layoutmanager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        main_item_H?.layoutManager = layoutmanager
        main_item_H?.adapter = adaptador_V

    }
}