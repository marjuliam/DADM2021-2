package com.unal.multiplayertictactoe.game

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.unal.multiplayertictactoe.backCommunicationImp.BackCommunication
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
import kotlin.concurrent.schedule
import java.util.*
import kotlin.collections.ArrayList

class MainGame : AppCompatActivity() {

    private var btn_11: CardView? = null
    private var btn_21: CardView? = null
    private var btn_31: CardView? = null
    private var btn_12: CardView? = null
    private var btn_22: CardView? = null
    private var btn_32: CardView? = null
    private var btn_13: CardView? = null
    private var btn_23: CardView? = null
    private var btn_33: CardView? = null


    private var txt_11: TextView? = null
    private var txt_21: TextView? = null
    private var txt_31: TextView? = null
    private var txt_12: TextView? = null
    private var txt_22: TextView? = null
    private var txt_32: TextView? = null
    private var txt_13: TextView? = null
    private var txt_23: TextView? = null
    private var txt_33: TextView? = null

    var ownGame = ArrayList<String>()
    var otherGame = ArrayList<String>()
    var token = ""

    var back = BackCommunication()

    var ownLetter: String? = null
    var otherLetter: String? = null
    var gammer: Int? = null

    private var txtOwnPoints: TextView? = null
    private var txtOtherPoints: TextView? = null
    private var txtTiePoints: TextView? = null
    private var txtGammer: TextView? = null

    var stringMatrix = "[[0,0,0],[0,0,0],[0,0,0]]"
    val matrix = arrayOf(intArrayOf(0, 0, 0), intArrayOf(0, 0, 0), intArrayOf(0, 0, 0))
    val matrix2 = arrayOf(intArrayOf(0, 0, 0), intArrayOf(0, 0, 0), intArrayOf(0, 0, 0))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_game)

        ownLetter = intent.getStringExtra("letter")
        gammer = Integer.parseInt(intent.getStringExtra("gammer").toString())
        token = intent.getStringExtra("token").toString()
        if (ownLetter.equals("X")) {
            otherLetter = "O"
        } else {
            otherLetter = "X"
        }

        txtOwnPoints = findViewById(R.id.txt_own_points)
        txtOtherPoints = findViewById(R.id.txt_other_points)
        txtTiePoints = findViewById(R.id.txt_tie_points)

        txtGammer = findViewById(R.id.txt_player)

        btn_11 = findViewById(R.id.btn11)
        btn_21 = findViewById(R.id.btn21)
        btn_31 = findViewById(R.id.btn31)
        btn_12 = findViewById(R.id.btn12)
        btn_22 = findViewById(R.id.btn22)
        btn_32 = findViewById(R.id.btn32)
        btn_13 = findViewById(R.id.btn13)
        btn_23 = findViewById(R.id.btn23)
        btn_33 = findViewById(R.id.btn33)


        txt_11 = findViewById(R.id.txt11)
        txt_21 = findViewById(R.id.txt21)
        txt_31 = findViewById(R.id.txt31)
        txt_12 = findViewById(R.id.txt12)
        txt_22 = findViewById(R.id.txt22)
        txt_32 = findViewById(R.id.txt32)
        txt_13 = findViewById(R.id.txt13)
        txt_23 = findViewById(R.id.txt23)
        txt_33 = findViewById(R.id.txt33)
        block(false)
        startGame(gammer!!)

    }

    fun startGame(gamer: Int) {
        if (gamer == 1) {
            txtGammer!!.text = "Juegas TU!!"
            block(true)
            listen()
        } else {
            txtGammer!!.text = "Esperando..."
            block(false)
            getGame()
        }
    }

    fun dogameOwn(letter: String, position_x: Int, position_y: Int, btn: CardView) {

        var position: String = (position_x.toString() + "," + position_y.toString())
        System.out.println("Mi Juego -------------->" + position)

        //Storage game
        gameToMatrix(position_x - 1, position_y - 1)
        display(matrix)
        matrixtoString()

        //block card
        btn.isEnabled = false

        //change color card
        changeLetter(letter, btn)
        changeColorOwn(btn!!)

        //storage own game
        ownGame.add(position)

        //Check winner
        var response = checkWinner(ownGame, "Yo")


        if (response == "Ganaste") {
            block(false)
            //postGame
            postGame("GANADOR")
        } else {
            //postGame
            postGame("NN")
            //other game send changes
            startGame(0)
        }
    }

    fun listen() {
        btn_11!!.setOnClickListener {
            dogameOwn(ownLetter!!, 1, 1, btn_11!!)
        }
        btn_21!!.setOnClickListener {
            dogameOwn(ownLetter!!, 2, 1, btn_21!!)
        }
        btn_31!!.setOnClickListener {
            dogameOwn(ownLetter!!, 3, 1, btn_31!!)
        }
        btn_12!!.setOnClickListener {
            dogameOwn(ownLetter!!, 1, 2, btn_12!!)
        }
        btn_22!!.setOnClickListener {
            dogameOwn(ownLetter!!, 2, 2, btn_22!!)
        }
        btn_32!!.setOnClickListener {
            dogameOwn(ownLetter!!, 3, 2, btn_32!!)
        }
        btn_13!!.setOnClickListener {
            dogameOwn(ownLetter!!, 1, 3, btn_13!!)
        }
        btn_23!!.setOnClickListener {
            dogameOwn(ownLetter!!, 2, 3, btn_23!!)
        }
        btn_33!!.setOnClickListener {
            dogameOwn(ownLetter!!, 3, 3, btn_33!!)
        }
    }

    fun gameToMatrix(position_x: Int, position_y: Int) {
        if (ownLetter.equals("O")) {
            matrix[position_y][position_x] = 2
        } else if (ownLetter.equals("X")) {
            matrix[position_y][position_x] = 1
        }
    }

    fun changeLetter(letter: String, position: CardView) {
        var txtToChange: TextView? = null
        when (position) {
            btn_11 -> txtToChange = txt_11
            btn_21 -> txtToChange = txt_21
            btn_31 -> txtToChange = txt_31
            btn_12 -> txtToChange = txt_12
            btn_22 -> txtToChange = txt_22
            btn_32 -> txtToChange = txt_32
            btn_13 -> txtToChange = txt_13
            btn_23 -> txtToChange = txt_23
            btn_33 -> txtToChange = txt_33
        }
        txtToChange!!.text = letter
    }

    fun getGame() {
        // Create JSON using JSONObject
        val jsonObject = JSONObject()
        System.out.println("TOKEN--------------------------------------->"+token)
        jsonObject.put("token", token)
        jsonObject.put("player_request", "jugador1")
        // Convert JSONObject to String
        val jsonObjectString = jsonObject.toString()

        val gamesService: GamesService = RestEngine.getRestEngine().create(GamesService::class.java)
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        CoroutineScope(Dispatchers.IO).launch {
            val response = gamesService.getGame(requestBody)
            withContext(Dispatchers.Main) {

                if (response.isSuccessful) {
                    var matrixStringOther = response.body()!!.matrix
                    System.out.println("--------------------------------------->"+matrixStringOther)
                    dogameOther(matrixStringOther)
                    //-----> next(tockenCreate)
                } else {
                    Log.e("RETROFIT_ERROR", response.code().toString())
                }
            }
        }
    }

    fun postGame(winner: String) {
        // Create JSON using JSONObject
        val jsonObject = JSONObject()
        jsonObject.put("token", token)
        jsonObject.put("player1", "jugador1")
        jsonObject.put("player2", "jugador2")
        jsonObject.put("matrix", stringMatrix)
        jsonObject.put("winner", winner)
        jsonObject.put("status", "OnGame")
        // Convert JSONObject to String
        val jsonObjectString = jsonObject.toString()

        val gamesService: GamesService = RestEngine.getRestEngine().create(GamesService::class.java)
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
        System.out.println(requestBody)

        CoroutineScope(Dispatchers.IO).launch {

            val response = gamesService.postGame(requestBody)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    var tockenCreate = response.body()
                    System.out.println(tockenCreate)
                    //-----> next(tockenCreate)
                } else {
                    Log.e("RETROFIT_ERROR", response.code().toString())
                }
            }
        }
    }

    fun dogameOther(othermatrix:String) {

        System.out.println("LLEGA--------------------------------------->"+othermatrix)
        System.out.println("MIA--------------------------------------->"+stringMatrix)
        if(othermatrix.equals(stringMatrix)){
            System.out.println("Consultando")
            Timer().schedule(5000) {
                getGame()
            }
        }else if (!othermatrix.equals(stringMatrix)){
            var otherPossition = stringtomatrix(othermatrix)
            var btn: CardView? = null
           if(!otherPossition.equals("")){
               when (otherPossition) {
                   "1,1" -> btn = btn_11
                   "1,2" -> btn = btn_12
                   "1,3" -> btn = btn_13
                   "2,1" -> btn = btn_21
                   "2,2" -> btn = btn_22
                   "2,3" -> btn = btn_23
                   "3,1" -> btn = btn_31
                   "3,2" -> btn = btn_32
                   "3,3" -> btn = btn_33
                   "" -> btn = null
               }
               btn!!.isEnabled = false
               changeLetter(otherLetter!!, btn)
               changeColorOther(btn!!)
               System.out.println("Otro Jugador -------------->" + otherPossition)
               //storage own game
               otherGame.add(otherPossition)
               var response = checkWinner(otherGame, "Yo")
               if (response == "Gano Tu Oponente") {
                   block(false)
               } else {
                   startGame(1)
               }
           }
        }
    }

    fun changeColorOwn(btn: CardView) {
        btn.setBackgroundColor(Color.rgb(255, 55, 0))
    }

    fun changeColorOther(btn: CardView) {
        btn.setBackgroundColor(Color.rgb(107, 255, 51))
    }

    fun checkWinner(listGame: ArrayList<String>, gamer: String): String {
        if (listGame.contains("1,1") && listGame.contains("2,2") && listGame.contains("3,3")) {
            System.out.println("Ganaste" + gamer)
            return ("Ganaste")
        } else if (listGame.contains("3,1") && listGame.contains("2,2") && listGame.contains("1,3")) {
            System.out.println("Ganaste" + gamer)
            return ("Ganaste")
        } else if (listGame.contains("1,1") && listGame.contains("2,1") && listGame.contains("3,1")) {
            System.out.println("Ganaste" + gamer)
            return ("Ganaste")
        } else if (listGame.contains("1,2") && listGame.contains("2,2") && listGame.contains("3,2")) {
            System.out.println("Ganaste" + gamer)
            return ("Ganaste")
        } else if (listGame.contains("1,3") && listGame.contains("2,3") && listGame.contains("3,3")) {
            System.out.println("Ganaste" + gamer)
            return ("Ganaste")
        } else if (listGame.contains("1,1") && listGame.contains("1,2") && listGame.contains("1,3")) {
            System.out.println("Ganaste" + gamer)
            return ("Ganaste")
        } else if (listGame.contains("2,1") && listGame.contains("2,2") && listGame.contains("2,3")) {
            System.out.println("Ganaste" + gamer)
            return ("Ganaste")
        } else if (listGame.contains("3,1") && listGame.contains("3,2") && listGame.contains("3,3")) {
            System.out.println("Ganaste" + gamer)
            return ("Ganaste")
        }

        return ("")
    }

    fun block(state: Boolean) {
        if (ownGame.contains("1,1") || otherGame!!.contains("1,1")) {
            btn_11!!.isEnabled = false
        } else {
            btn_11!!.isEnabled = state
        }
        if (ownGame.contains("2,1") || otherGame!!.contains("2,1")) {

            btn_21!!.isEnabled = false
        } else {
            btn_21!!.isEnabled = state
        }
        if (ownGame.contains("3,1") || otherGame!!.contains("3,1")) {
            btn_31!!.isEnabled = false
        } else {
            btn_31!!.isEnabled = state
        }
        if (ownGame.contains("1,2") || otherGame!!.contains("1,2")) {
            btn_12!!.isEnabled = false
        } else {
            btn_12!!.isEnabled = state
        }
        if (ownGame.contains("2,2") || otherGame!!.contains("2,2")) {
            btn_22!!.isEnabled = false
        } else {
            btn_22!!.isEnabled = state
        }
        if (ownGame.contains("3,2") || otherGame!!.contains("3,2")) {
            btn_32!!.isEnabled = false
        } else {
            btn_32!!.isEnabled = state
        }
        if (ownGame.contains("1,3") || otherGame!!.contains("1,3")) {
            btn_13!!.isEnabled = false
        } else {
            btn_13!!.isEnabled = state
        }
        if (ownGame.contains("2,3") || otherGame!!.contains("2,3")) {
            btn_23!!.isEnabled = false
        } else {
            btn_23!!.isEnabled = state
        }
        if (ownGame.contains("3,3") || otherGame!!.contains("3,3")) {
            btn_33!!.isEnabled = false
        } else {
            btn_33!!.isEnabled = state
        }
    }


    fun stringtomatrix(stringMatrix: String): String {
        val lstValues: List<String> = stringMatrix.split("[").map { it -> it.trim() }
        val lstValues2: List<String> = lstValues.get(2).split("]").map { it -> it.trim() }
        val lstValues3: List<String> = lstValues.get(3).split("]").map { it -> it.trim() }
        val lstValues4: List<String> = lstValues.get(4).split("]").map { it -> it.trim() }

        val row1: List<String> = lstValues2.get(0).split(",").map { it -> it.trim() }
        val row2: List<String> = lstValues3.get(0).split(",").map { it -> it.trim() }
        val row3: List<String> = lstValues4.get(0).split(",").map { it -> it.trim() }

        System.out.println("0,0-"+row1.get(0)+"  1,0,"+row1.get(1)+" 2,0,"+row1.get(2))
        matrix2[0][0] = Integer.parseInt(row1.get(0))
        matrix2[1][0] = Integer.parseInt(row1.get(1))
        matrix2[2][0] = Integer.parseInt(row1.get(2))


        System.out.println("0,1-"+row2.get(0)+"  1,1,"+row2.get(1)+"  2,1,"+row2.get(2))
        matrix2[0][1] = Integer.parseInt(row2.get(0))
        matrix2[1][1] = Integer.parseInt(row2.get(1))
        matrix2[2][1] = Integer.parseInt(row2.get(2))


        System.out.println("0,2-"+row3.get(0)+"   1,2,"+row3.get(1)+"  2,2,"+row3.get(2))
        matrix2[0][2] = Integer.parseInt(row3.get(0))
        matrix2[1][2] = Integer.parseInt(row3.get(1))
        matrix2[2][2] = Integer.parseInt(row3.get(2))

        var position = ""
        var number = 0
        if(ownLetter=="X"){
            number = 2
        }else{
            number = 1
        }
        System.out.println("number-->"+number)
        for (i in 0..2) {
            for (j in 0..2) {
                System.out.println("i-->"+i+"j-->"+j+" matrixn-->"+matrix2[i][j])
                if (matrix2[i][j] == number &&  matrix[i][j] != matrix2[i][j]) {
                    position = ((i + 1).toString() + "," + (j + 1).toString())
                    System.out.println("position->"+position)
                    matrix[i][j] = matrix2[i][j]
                }
            }
        }

        display(matrix)
        System.out.println("position->"+position)
        return position
    }

    fun matrixtoString() {
        var row1 = "[[" + matrix[0][0] + "," + matrix[0][1] + "," + matrix[0][2] + "],"
        var row2 = "[" + matrix[1][0] + "," + matrix[1][1] + "," + matrix[1][2] + "],"
        var row3 = "[" + matrix[2][0] + "," + matrix[2][1] + "," + matrix[2][2] + "]]"

        stringMatrix = row1 + row2 + row3

        System.out.println(stringMatrix)

    }

    fun display(matrix: Array<IntArray>) {
        println("The matrix is: ")
        for (row in matrix) {
            for (column in row) {
                print("$column    ")
            }
            println()
        }
    }


}