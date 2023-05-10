package net.whatscall.freec.screens

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.ValueCallback
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.whatscall.freec.databinding.ActivityWildQuestBinding
import net.whatscall.freec.domain.BaseWildActivity
import net.whatscall.photo_viewer.horizon.HorizonView
import net.whatscall.photo_viewer.horizon.HorizonListener
import net.whatscall.photo_viewer.horizon.ferry
import net.whatscall.storage.Storage
import net.whatscall.storage.StorageConfig

class WildQuestActivity : BaseWildActivity(), SplashScreen.KeepOnScreenCondition, HorizonListener {

    private lateinit var binding: ActivityWildQuestBinding
    private lateinit var view: HorizonView
    private var keepCondition = true
    private var shouldHit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition(this)
        super.onCreate(savedInstanceState)

        setOnBackPress {
            if (this::view.isInitialized) {
                view.onBackPress()
            }
        }

        if (intent.extras?.getBoolean(EXT_WILD) == true) {
            shouldHit = false
            binding = ActivityWildQuestBinding.inflate(layoutInflater)
            setContentView(binding.root)
            keepCondition = false
            lifecycleScope.launch(Dispatchers.IO) {
//                view = HorizonView.inflate(this@WildQuestActivity, binding.wildRoot)
//                view.setListener(this@WildQuestActivity)
            }
        } else {
            shouldHit = true
            lifecycleScope.launch(Dispatchers.IO) {
                val string: String? = contentResolver.ferry()
                Storage.getInstance().set(StorageConfig.ENTRY_FERRY, string.toString())
                exposure()
            }
        }
    }

    override fun specify(survey: ValueCallback<Array<Uri>>?) {
        eject(survey)
    }

    override fun available(status: String) {
        lifecycleScope.launch {
            val storage = Storage.getInstance()
            if (storage.get<String>(StorageConfig.ENTRY_PLACE) == null) {
                storage.set(StorageConfig.ENTRY_PLACE, status)
            }
        }
    }

    override fun exposure() {
        Intent(this@WildQuestActivity, GonzosHitMenuActivity::class.java).also {
            it.putExtra(EXT_SHOULD_HIT, shouldHit)
            startActivity(it)
            finish()
        }
    }

    override fun shouldKeepOnScreen(): Boolean {
        return keepCondition
    }

}

const val EXT_WILD = "wild"