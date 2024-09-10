package com.example.spellbound

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Random

class MainActivity : AppCompatActivity() {
    var wordSearch: String? = null
    lateinit var answers: CharArray
    var errors = 0
    var difficulty = "Normal"
    var score = 0
    private val letters = ArrayList<String>()
    private var image: ImageView? = null
    private var wordTV: TextView? = null
    private var searchTV: TextView? = null
    private var scoreTV: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        image = findViewById(R.id.img)
        wordTV = findViewById(R.id.wordTV)
        searchTV = findViewById(R.id.searchTV)
        scoreTV = findViewById(R.id.scoreTV)
        newGame()
        }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuEasy -> {
                difficulty = "Easy"
                newGame()
            }
            R.id.menuNormal -> {
                difficulty = "Normal"
                newGame()
            }
            R.id.menuHard -> {
                difficulty = "Hard"
                newGame()
            }
            R.id.menuRandom ->{
                val difficulties = arrayOf("Easy", "Normal", "Hard")
                difficulty = difficulties[random.nextInt(difficulties.size)]
                newGame()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun chooseWord(): String {
        if (difficulty == "Easy") {
            return easyWords[random.nextInt(easyWords.size)]
        }
        return if (difficulty == "Hard") {
            hardWords[random.nextInt(hardWords.size)]

        } else normalWords[random.nextInt(normalWords.size)]
    }
    private fun updateImage(spellbound:Int){
        val resource = resources.getIdentifier("spellbound$spellbound", "drawable",
            packageName)
        image!!.setImageResource(resource)
    }

    fun newGame(){
        errors =-1
        letters.clear()
        wordSearch = chooseWord()
        answers = CharArray(wordSearch!!.length)
        for (i in answers.indices){
            answers[i] = '_'
        }
        updateImage(errors)
        wordTV!!.text = stateWord()
        searchTV!!.text = ""
    }

    private fun readLetter(c: String) {
        if (!letters.contains(c)) {
            if (wordSearch!!.contains(c)) {
                var index = wordSearch!!.indexOf(c)
                while (index >= 0) {
                    answers[index] = c[0]
                    index = wordSearch!!.indexOf(c, index + 1)
                }
            } else {
                errors++
            }
            letters.add(c)
        } else {
            Toast.makeText(this, "Letter already entered", Toast.LENGTH_SHORT).show()
        }
    }
    private fun stateWord(): String {
        val builder = StringBuilder()
        for (i in answers.indices) {
            builder.append(answers[i])
            if (i < answers.size - 1) {
                builder.append(" ")
            }
        }
        return builder.toString()
    }
    fun touchLetter(v: View) {
        if (errors < 6 && searchTV!!.text != "YOU WIN!") {
            val letter = (v as Button).text.toString()
            readLetter(letter)
            wordTV!!.text = stateWord()
            updateImage(errors)
            if (wordSearch.contentEquals(String(answers))) {
                Toast.makeText(this, "YOU WIN!", Toast.LENGTH_SHORT).show()
                searchTV!!.text = "YOU WIN!"
                if (difficulty == "Hard") {
                    score += 15
                }
                if (difficulty == "Normal") {
                    score += 10
                }
                if (difficulty == "Easy") {
                    score += 5
                }
                scoreTV!!.text = "Score: $score"
            } else {
                if (errors >= 6) {
                    updateImage(7)
                    Toast.makeText(this, "You lose...", Toast.LENGTH_SHORT).show()
                    searchTV!!.text = wordSearch
                }
            }
        } else {
            Toast.makeText(this, "End of the game", Toast.LENGTH_SHORT).show()
        }
    }fun start(view: View?) {
        newGame()
    }
    companion object {
        /**
         * List of words used on the Easy difficulty
         */
        val easyWords = arrayOf(
            "GARDEN", "LAKE", "SONG", "COW", "HOUSE", "TREEHOUSE", "STAR", "FLOWER", "KEY",
            "MUSIC", "BIRD", "SKY", "DREAM", "WINDOW", "BEACH", "GREEN", "GATE", "LAUGH",
            "RIVER", "COOK", "CLOUD", "COLOR", "WAVE", "BALANCE", "STORY", "BEAUTY", "SHADOW"
        )

        /**
         * List of the words used on the Normal difficulty
         */
        val normalWords = arrayOf(
            "WISDOM", "SPARKLE", "REFLECTION", "TENDER", "GIGGLE", "CHERISH", "WANDER"
        )

        /**
         * List of the words used on the Hard difficulty
         */
        val hardWords = arrayOf(
            "TRANQUILITY", "UNFORGETTABLE", "FASCINATION", "INNOVATION", "PERSEVERANCE", "IMAGINATION"
        )
        val random = Random()
    }



}