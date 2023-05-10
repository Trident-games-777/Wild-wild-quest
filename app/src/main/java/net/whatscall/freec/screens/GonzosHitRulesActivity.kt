package net.whatscall.freec.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import net.whatscall.freec.databinding.ActivityGonzosHitRulesBinding

class GonzosHitRulesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGonzosHitRulesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGonzosHitRulesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}