package com.topnetwork.wallet.modules.welcome

import android.os.Bundle
import android.transition.Fade
import android.transition.Slide
import android.transition.TransitionSet
import android.view.Gravity
import android.view.Window
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.topnetwork.core.setOnSingleClickListener
import com.topnetwork.wallet.BuildConfig
import com.topnetwork.wallet.R
import com.topnetwork.wallet.core.BaseActivity
import kotlinx.android.synthetic.main.activity_welcome.*

/**
 * @FileName     : WelcomeActivity
 * @date         : 2020/6/11 15:50
 * @author       : Owen
 * @description  :
 */
class WelcomeActivity : BaseActivity() {

    private lateinit var viewModel: WelcomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(window) {
            requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)

            val slideFromTop = Slide(Gravity.TOP)
                .addTarget(R.id.title)

            val slideFromBottom = Slide(Gravity.BOTTOM)
                .addTarget(R.id.buttonCreate)
                .addTarget(R.id.buttonRestore)
                .addTarget(R.id.privacySettings)
                .addTarget(R.id.textVersion)

            val transitionSet = TransitionSet()
                .addTransition(Fade())
                .addTransition(slideFromTop)
                .addTransition(slideFromBottom)

            transitionSet.duration = 700
            enterTransition = transitionSet
        }

        setTransparentStatusBar()

        setContentView(R.layout.activity_welcome)

        viewModel = ViewModelProvider(this).get(WelcomeViewModel::class.java)
        viewModel.init()

        viewModel.openRestoreModule.observe(this, Observer {
//            RestoreModule.start(this)
        })

        viewModel.openCreateWalletModule.observe(this, Observer {
//            CreateWalletModule.start(this)
        })

        viewModel.appVersionLiveData.observe(this, Observer { appVersion ->
            appVersion?.let {
                var version = it
                if (getString(R.string.is_release) == "false") {
                    version = "$version (${BuildConfig.VERSION_CODE})"
                }
                textVersion.text = getString(R.string.Welcome_Version, version)
            }
        })

        viewModel.openTorPage.observe(this, Observer {
//            PrivacySettingsModule.start(this)
        })

        buttonCreate.setOnSingleClickListener {
            viewModel.delegate.createWalletDidClick()
        }

        buttonRestore.setOnSingleClickListener {
            viewModel.delegate.restoreWalletDidClick()
        }

        privacySettings.setOnSingleClickListener {
            viewModel.delegate.openTorPage()
        }

    }


}