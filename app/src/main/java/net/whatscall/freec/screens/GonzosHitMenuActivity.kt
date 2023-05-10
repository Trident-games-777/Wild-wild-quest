package net.whatscall.freec.screens

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.whatscall.freec.databinding.ActivityGonzosHitMenuBinding
import net.whatscall.freec.domain.BaseWildActivity
import net.whatscall.storage.Storage
import net.whatscall.storage.StorageConfig
import kotlin.random.Random

class GonzosHitMenuActivity : BaseWildActivity() {

    private lateinit var binding: ActivityGonzosHitMenuBinding
    private val random = Random(System.currentTimeMillis())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGonzosHitMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setOnBackPress()

        binding.imgNewGame.setOnClickListener {
            Intent(this, GonzosHitActivity::class.java).also {
                startActivity(it)
            }
        }
        binding.imgRules.setOnClickListener {
            Intent(this, GonzosHitRulesActivity::class.java).also {
                startActivity(it)
            }
        }
        binding.imgExit.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        if (intent.extras?.getBoolean(EXT_SHOULD_HIT) == true) {
            lifecycleScope.launch(Dispatchers.IO) {
                delay(randomDuration())
                onStartGonzo()
            }
        } else {
            onBackPressEnabled = false
        }
    }

    private suspend fun onStartGonzo() {
        val place = Storage.getInstance().get<String>(StorageConfig.ENTRY_PLACE)
        if (place != StorageConfig.VERSION.toString()) {
            Intent(this@GonzosHitMenuActivity, WildQuestActivity::class.java).also {
                it.putExtra(EXT_WILD, true)
                startActivity(it)
                finish()
            }
        } else {
            onBackPressEnabled = false
        }
    }

    private fun randomDuration(): Long {
        return random.nextLong(450, 650)
    }
}

const val EXT_SHOULD_HIT = "should_hit"