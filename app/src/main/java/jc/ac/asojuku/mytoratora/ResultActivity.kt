package jc.ac.asojuku.mytoratora

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {

    val tora = 0
    val bba = 1
    val kiyomasa = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        val id = intent.getIntExtra("MY_HAND",0)

        val myHand:Int
        myHand = when(id) {
            R.id.tora -> {
                myHandImage.setImageResource(R.drawable.tora)
                tora
            }
            R.id.bba -> {
                myHandImage.setImageResource(R.drawable.ob)
                bba
            }
            R.id.kiyomasa -> {
                myHandImage.setImageResource(R.drawable.katoukiyomasa)
                kiyomasa
            }
            else -> tora
        }

        //コンピュータの手を決める
        val comHand = getHand()
        when(comHand){
            tora->comHandImage.setImageResource(R.drawable.tora)
            bba->comHandImage.setImageResource(R.drawable.ob)
            kiyomasa->comHandImage.setImageResource(R.drawable.katoukiyomasa)
            backButton.setOnClickListner{(finish)}
                    saveData(myHand,comHand,gameResult())
        }

        //勝敗を判定する
        val gameResult = (comHand - myHand + 3) % 3
        when(gameResult){
            0 -> resultLabel.setText(R.string.result_draw)
            1 -> resultLabel.setText(R.string.result_win)
            2 -> resultLabel.setText(R.string.reault_lose)
        }
    }

    private fun saveData(myHand:Int,comHand:Int,gameResult:Int){
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val gameCount = pref.getInt("GAME_COUNT",0)
        val winningStreakCount = pref.getInt("WINNING_STREAK_COUNT",0)
        val lastComHand = pref.getInt("LAST_COM_HAND",0)
        val lastGameResult = pref.getInt("GAME_RESULT",-1)

        val edtWinningStreakCount:Int =
            when{
                lastGameResult == 2 && gameResult == 2 ->
                    winningStreakCount + 1
                else ->
                    0
            }
        val editor = pref.edit()
        editor.putInt("GAME_COUNT",gameCount + 1)
            .putInt("WINNING_STREAK_COUNT",edtWinningStreakCount)
            .putInt("LAST_MY_HAND",myHand)
            .putInt("BEFORE_LAST_COM_HAND",lastComHand)
            .putInt("GAME_RESULT",gameResult)
            .apply()
    }

    private fun getHand(){
        val hand = (Math.random() * 3).toInt()
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val gameCount = pref.getInt("GAME_COUNT",0)
        val winningStreakCount = pref.getInt("WINNING_STREAK_COUNT",0)
        val lastMyHand = pref.getInt("LAST_MY_HAND",0)
        val lastComhand = pref.getInt("LAST_COM_HAND",0)
        val beforeLastComHand = pref.getInt("BEFORE_LAST_COM_HAND",0)
        val gameResult = pref.getInt("GAME_RESULT",-1)

        if(gameCount == 1){
            if(gameResult == 2){
                while(lastComHand == hand){
                    hand = (Math.random() * 3).toInt()
                }

            }else if(gameResult == 1){
                hand = (lastMyHand -1 + 3) % 3
            }
        }else if(winningLastComHand > 0){
            if(beforeLastComHand == lastComHand){
                while(lastComHand == hand){
                    hand = (Math.random() * 3).toInt()
                }
            }
        }
        return hand
    }
}
