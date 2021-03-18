package com.yeongil.digitalwellbeing

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.WindowManager.LayoutParams
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.whenResumed
import androidx.lifecycle.withResumed
import com.yeongil.digitalwellbeing.background.AppBlockService
import com.yeongil.digitalwellbeing.databinding.DialogAppBlockAlertBinding
import com.yeongil.digitalwellbeing.databinding.DialogAppBlockCloseBinding
import com.yeongil.digitalwellbeing.viewModel.viewModel.AppBlockViewModel
import com.yeongil.digitalwellbeing.viewModelFactory.AppBlockViewModelFactory

class AppBlockActivity : AppCompatActivity() {
    private val appBlockViewModel: AppBlockViewModel by viewModels {
        AppBlockViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /* If current activity is recreated with history, forward to main activity. */
        if (intent.flags and Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY != 0) {
            startActivity(Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            })
            finishAffinity()
        }

        /* Initialize window settings */
        setFinishOnTouchOutside(false)

        /* Initialize */
        val packageName = intent.getStringExtra(PACKAGE_NAME_KEY) ?: return
        val blockingAppKey = intent.getStringExtra(BLOCKING_APP_KEY_KEY) ?: return
        appBlockViewModel.putPackageName(packageName)
        appBlockViewModel.putBlockingAppKey(blockingAppKey)

        /* Show dialog */
        when (intent.action) {
            CLOSE_IMMEDIATE -> {
                appBlockViewModel.setClose()
                showCloseDialog()
            }
            ALERT -> {
                appBlockViewModel.setAlert()
                showAlertDialog()
            }
        }
    }

    override fun onRestart() {
        startActivity(Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        })
        finishAffinity()
        super.onRestart()
    }

    private fun showCloseDialog() {
        val binding: DialogAppBlockCloseBinding =
            DataBindingUtil.setContentView(this, R.layout.dialog_app_block_close)
        binding.vm = appBlockViewModel
        binding.lifecycleOwner = this
        binding.notifyChange()

        binding.tempUseBtn.setOnClickListener { extendAllowedTime() }
        binding.completeBtn.setOnClickListener { goHome() }
        onBackPressedDispatcher.addCallback { goHome() }
    }

    private fun showAlertDialog() {
        val binding: DialogAppBlockAlertBinding =
            DataBindingUtil.setContentView(this, R.layout.dialog_app_block_alert)
        binding.vm = appBlockViewModel
        binding.lifecycleOwner = this
        binding.notifyChange()

        binding.closeBtn.setOnClickListener { goHome() }
        binding.tempUseBtn.setOnClickListener { allowForThisTime() }
        binding.completeBtn.setOnClickListener { extendAllowedTime() }
        onBackPressedDispatcher.addCallback { extendAllowedTime() }
    }

    private fun goHome() {
        val intent = Intent().apply {
            action = "android.intent.action.MAIN"
            addCategory("android.intent.category.HOME")
            addFlags(
                Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
                        or Intent.FLAG_ACTIVITY_FORWARD_RESULT
                        or Intent.FLAG_ACTIVITY_NEW_TASK
                        or Intent.FLAG_ACTIVITY_CLEAR_TASK
            )
        }

        startActivity(Intent(this, MainActivity::class.java))
        finishAffinity()
        startActivity(intent)
    }

    private fun extendAllowedTime() {
        val packageName = appBlockViewModel.blockingAppKey
        val intent = Intent(this, AppBlockService::class.java).apply {
            action = AppBlockService.EXTEND_ALLOWED_TIME
            putExtra(AppBlockService.PACKAGE_NAME_EXTRA_KEY, packageName)
        }

        finish()
        startService(intent)
    }

    private fun allowForThisTime() {
        val packageName = appBlockViewModel.blockingAppKey
        val intent = Intent(this, AppBlockService::class.java).apply {
            action = AppBlockService.ALLOW_FOR_THIS_TIME
            putExtra(AppBlockService.PACKAGE_NAME_EXTRA_KEY, packageName)
        }

        finish()
        startService(intent)
    }

    companion object {
        const val CLOSE_IMMEDIATE = "CLOSE_IMMEDIATE"
        const val ALERT = "ALERT"

        const val PACKAGE_NAME_KEY = "PACKAGE_NAME"
        const val BLOCKING_APP_KEY_KEY = "BLOCKING_APP_KEY"
    }
}