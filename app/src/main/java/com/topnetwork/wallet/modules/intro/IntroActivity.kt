package com.topnetwork.wallet.modules.intro

import android.app.ActivityOptions
import android.os.Bundle
import android.transition.Fade
import android.view.Window
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.topnetwork.wallet.R
import com.topnetwork.wallet.core.BaseActivity
import com.topnetwork.wallet.modules.welcome.WelcomeModule
import kotlinx.android.synthetic.main.activity_intro.*

/**
 * @FileName     : IntroActivity
 * @date         : 2020/6/11 14:59
 * @author       : Owen
 * @description  :
 */
class IntroActivity : BaseActivity() {

    private val presenter by lazy {
        ViewModelProvider(this, IntroModule.Factory()).get(
            IntroPresenter::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(window) {
            requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)

            val fade = Fade()
            fade.duration = 700

            exitTransition = fade
        }

        setTransparentStatusBar()
        setContentView(R.layout.activity_intro)

        val viewPagerAdapter = IntroViewPagerAdapter(supportFragmentManager)
        val pagesCount = viewPagerAdapter.count

        viewPager.adapter = viewPagerAdapter

        try {
            // set custom mScroller to viewPager via Reflection to make viewPager swipe smoothly for next and back buttons
            val mScroller = ViewPager::class.java.getDeclaredField("mScroller")
            mScroller.isAccessible = true
            mScroller.set(viewPager, ViewPagerScroller(viewPager.context))
        } catch (e: Exception) {
        }

        val images = arrayOf(R.drawable.ic_independence, R.drawable.ic_knowledge, R.drawable.ic_privacy)
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) = Unit

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                root.progress = (position + positionOffset) / (pagesCount - 1)
            }

            override fun onPageSelected(position: Int) {
                imageSwitcher.setImageResource(images[position])
            }
        })

        circleIndicator.setViewPager(viewPager)

        btnNext.setOnClickListener {
            if (viewPager.currentItem < pagesCount - 1) {
                viewPager.currentItem = viewPager.currentItem + 1
            } else {
                presenter.start()
            }
        }

        btnSkip.setOnClickListener {
            presenter.skip()
        }

        (presenter.router as? IntroRouter)?.let { router ->
            router.navigateToWelcomeLiveEvent.observe(this, Observer {
                WelcomeModule.start(this, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
            })
        }
    }

    override fun onBackPressed() {
        if (viewPager.currentItem == 0) {
            super.onBackPressed()
        } else {
            viewPager.currentItem = viewPager.currentItem - 1
        }
    }

}