package net.whatscall.freec

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import net.whatscall.freec.databinding.ActivityGonzosHitBinding
import net.whatscall.freec.gonzohit.GonzoHitListener

class GonzosHitActivity : AppCompatActivity(), GonzoHitListener {

    private lateinit var binding: ActivityGonzosHitBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGonzosHitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding.gonzoHit) {
            gonzoHitView.setGonzoHitListener(this@GonzosHitActivity)
            imgNewGame.setOnClickListener {
                gonzoHitView.visibility = View.VISIBLE
                gonzoWindow.visibility = View.GONE
                gonzoHitView.start()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        binding.gonzoHit.gonzoHitView.pause()
    }

    override fun onResume() {
        super.onResume()
        binding.gonzoHit.gonzoHitView.resume()
    }

    override fun onWin() {
        with(binding.gonzoHit) {
            txtMessage.text = getString(R.string.you_win)
            txtMessage.visibility = View.VISIBLE
            gonzoWindow.visibility = View.VISIBLE
        }
    }

    override fun onLoose() {
        with(binding.gonzoHit) {
            txtMessage.text = getString(R.string.you_loose)
            txtMessage.visibility = View.VISIBLE
            gonzoWindow.visibility = View.VISIBLE
        }
    }
}