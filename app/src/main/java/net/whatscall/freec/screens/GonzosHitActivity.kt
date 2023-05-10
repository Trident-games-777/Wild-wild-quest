package net.whatscall.freec.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import net.whatscall.freec.R
import net.whatscall.freec.databinding.ActivityGonzosHitBinding
import net.whatscall.gonzo_hit.GonzoHitListener

class GonzosHitActivity : AppCompatActivity(), GonzoHitListener {

    private lateinit var binding: ActivityGonzosHitBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGonzosHitBinding.inflate(layoutInflater)
        setContentView(binding.root)
        with(binding) {
            gonzoHitView.setGonzoHitListener(this@GonzosHitActivity)
            imgNewGame.setOnClickListener {
                gonzoHitView.visibility = View.VISIBLE
                gonzoWindow.visibility = View.GONE
                gonzoHitView.start()
            }
            btnBack.setOnClickListener {
                finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.gonzoHitView.start()
    }

    override fun onPause() {
        super.onPause()
        binding.gonzoHitView.pause()
    }

    override fun onResume() {
        super.onResume()
        binding.gonzoHitView.resume()
    }

    override fun onWin() {
        with(binding) {
            txtMessage.text = getString(R.string.you_win)
            txtMessage.visibility = View.VISIBLE
            gonzoWindow.visibility = View.VISIBLE
        }
    }

    override fun onLoose() {
        with(binding) {
            txtMessage.text = getString(R.string.you_loose)
            txtMessage.visibility = View.VISIBLE
            gonzoWindow.visibility = View.VISIBLE
        }
    }
}