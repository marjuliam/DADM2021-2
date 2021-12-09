package com.unal.multiplayertictactoe.game.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.unal.multiplayertictactoe.backCommunicationImp.domain.entities.GamesItems
import com.unal.multiplayertictactoe.game.MainGame
import com.unal.multiplayertictactoe.R

class AdapterRecyclerVertical(
    items_blog: ArrayList<GamesItems>,
    username: String
) : RecyclerView.Adapter<AdapterRecyclerVertical.ViewHolder>() {
    var items: ArrayList<GamesItems>? = null
    var username: String? = null

    init {
        this.username = username
        this.items = items_blog
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.vertical_item, parent, false)//create instance context
        var Holder = ViewHolder(vista)
        return Holder
    }

    override fun getItemCount(): Int {
        return this.items!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items?.get(position)
        holder.nameGame?.text = item!!.name
        holder.token?.text = item.token
        holder.namePlayer?.text = "Juegador1"


        holder.vista.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                var intent = Intent(v!!.context, MainGame::class.java)

                //Detail object blog
                intent.putExtra("token", holder.token!!.text)
                intent.putExtra("name", holder.nameGame!!.text)
                intent.putExtra("letter", "O")
                intent.putExtra("gammer", "2")

                v.context.startActivity(intent)
            }

        })
    }

    //ViewHolder inner class
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var vista = v
        var nameGame: TextView? = null
        var namePlayer: TextView? = null
        var token: TextView? = null

        init {
            nameGame = vista.findViewById(R.id.txt_name_game)
            namePlayer = vista.findViewById(R.id.txt_player)
            token = vista.findViewById(R.id.txt_token)
        }
    }
}