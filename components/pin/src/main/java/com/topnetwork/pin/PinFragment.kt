package com.topnetwork.pin

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.biometric.BiometricConstants
import androidx.biometric.BiometricPrompt
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.topnetwork.core.helpers.HudHelper
import com.topnetwork.pin.core.NumPadItem
import com.topnetwork.pin.core.NumPadItemType
import com.topnetwork.pin.core.NumPadItemsAdapter
import com.topnetwork.pin.core.SmoothLinearLayoutManager
import com.topnetwork.pin.edit.EditPinModule
import com.topnetwork.pin.edit.EditPinPresenter
import com.topnetwork.pin.edit.EditPinRouter
import com.topnetwork.pin.set.SetPinModule
import com.topnetwork.pin.set.SetPinPresenter
import com.topnetwork.pin.set.SetPinRouter
import com.topnetwork.pin.unlock.UnlockPinModule
import com.topnetwork.pin.unlock.UnlockPinPresenter
import com.topnetwork.pin.unlock.UnlockPinRouter
import com.topnetwork.views.TopMenuItem
import kotlinx.android.synthetic.main.fragment_pin.*
import java.util.concurrent.Executor

/**
 * @FileName     : PinFragment
 * @date         : 2020/6/10 10:53
 * @author       : Owen
 * @description  :
 */
class PinFragment : Fragment(), NumPadItemsAdapter.Listener {

    private val interactionType: PinInteractionType by lazy {
        arguments?.getParcelable(PinModule.keyInteractionType) ?: PinInteractionType.UNLOCK
    }

    private val showCancelButton: Boolean by lazy {
        arguments?.getBoolean(PinModule.keyShowCancel) ?: true
    }

    private lateinit var pinView: PinView
    private lateinit var viewDelegate: PinModule.IViewDelegate
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var pinPagesAdapter: PinPagesAdapter
    private lateinit var numpadAdapter: NumPadItemsAdapter
    private val executor = Executor { command -> command.run() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pinPagesAdapter = PinPagesAdapter()
        context?.let {
            layoutManager = SmoothLinearLayoutManager(it, LinearLayoutManager.HORIZONTAL, false)
            pinPagesRecyclerView.layoutManager = layoutManager
        }
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(pinPagesRecyclerView)
        pinPagesRecyclerView.adapter = pinPagesAdapter
        pinPagesRecyclerView.setOnTouchListener { _, _ -> true /*disable RecyclerView scroll*/ }

        when (interactionType) {
            PinInteractionType.UNLOCK -> {
                val unlockPresenter = ViewModelProvider(this, UnlockPinModule.Factory(showCancelButton)).get(
                    UnlockPinPresenter::class.java)
                val unlockRouter = unlockPresenter.router as UnlockPinRouter
                pinView = unlockPresenter.view as PinView
                viewDelegate = unlockPresenter

                unlockRouter.dismissWithSuccess.observe(viewLifecycleOwner, Observer { dismissWithSuccess() })
            }
            PinInteractionType.EDIT_PIN -> {
                val editPresenter = ViewModelProvider(this, EditPinModule.Factory()).get(
                    EditPinPresenter::class.java)
                val editRouter = editPresenter.router as EditPinRouter
                pinView = editPresenter.view as PinView
                viewDelegate = editPresenter

                editRouter.dismissWithSuccess.observe(viewLifecycleOwner, Observer { dismissWithSuccess() })
            }
            PinInteractionType.SET_PIN -> {
                val setPresenter = ViewModelProvider(this, SetPinModule.Factory()).get(
                    SetPinPresenter::class.java)
                val setRouter = setPresenter.router as SetPinRouter
                pinView = setPresenter.view as PinView
                viewDelegate = setPresenter

                setRouter.dismissWithSuccess.observe(viewLifecycleOwner, Observer { dismissWithSuccess() })

                setRouter.navigateToMain.observe(viewLifecycleOwner, Observer {
                    context?.let { ctx ->
                        // MainModule.start(ctx)
                    }
                    activity?.finish()
                })
            }
        }

        viewDelegate.viewDidLoad()

        numpadAdapter = NumPadItemsAdapter(this, NumPadItemType.FINGER)

        numPadItemsRecyclerView.adapter = numpadAdapter
        numPadItemsRecyclerView.layoutManager = GridLayoutManager(context, 3)

        observeData()
    }

    private fun dismissWithSuccess() {
        activity?.setResult(PinModule.RESULT_OK)
        activity?.finish()
    }

    private fun observeData() {
        pinView.hideToolbar.observe(viewLifecycleOwner, Observer {
            shadowlessToolbar.visibility = View.GONE
        })
        pinView.showBackButton.observe(viewLifecycleOwner, Observer {
            shadowlessToolbar.bind(null, TopMenuItem(R.drawable.ic_back, onClick = { activity?.onBackPressed() }))
        })
        pinView.title.observe(viewLifecycleOwner, Observer {
            shadowlessToolbar.bindTitle(getString(it))
        })
        pinView.addPages.observe(viewLifecycleOwner, Observer {
            pinPagesAdapter.pinPages.addAll(it)
            pinPagesAdapter.notifyDataSetChanged()
        })
        pinView.showPageAtIndex.observe(viewLifecycleOwner, Observer {
            Handler().postDelayed({
                pinPagesRecyclerView.smoothScrollToPosition(it)
                viewDelegate.resetPin()
                pinPagesAdapter.setEnteredPinLength(layoutManager.findFirstVisibleItemPosition(), 0)
            }, 300)
        })

        pinView.updateTopTextForPage.observe(viewLifecycleOwner, Observer { (error, pageIndex) ->
            pinPagesAdapter.updateTopTextForPage(error, pageIndex)
        })
        pinView.showError.observe(viewLifecycleOwner, Observer {
            context?.let { context -> HudHelper.showErrorMessage(context, it) }
        })

        pinView.fillPinCircles.observe(viewLifecycleOwner, Observer { (length, pageIndex) ->
            pinPagesAdapter.setEnteredPinLength(pageIndex, length)
        })

        pinView.showFingerprintButton.observe(viewLifecycleOwner, Observer {
            numpadAdapter.showFingerPrintButton = true
        })

        pinView.showFingerprintInput.observe(viewLifecycleOwner, Observer {
            setFingerprintInputScreenVisible(true)

            fingerprintCancelButton.setOnClickListener {
                setFingerprintInputScreenVisible(false)
            }
            showFingerprintDialog(it)
        })
    }

    override fun onItemClick(item: NumPadItem) {

    }

    private fun showFingerprintDialog(cryptoObject: BiometricPrompt.CryptoObject) {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.Fingerprint_DialogTitle))
            .setNegativeButtonText(getString(R.string.Button_Cancel))
            .build()
        val biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                activity?.runOnUiThread {
                    fingerprintCancelButton.visibility = View.GONE
                    viewDelegate.onFingerprintUnlock()
                }
            }
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                if (errorCode == BiometricConstants.ERROR_USER_CANCELED
                    || errorCode == BiometricConstants.ERROR_NEGATIVE_BUTTON
                    || errorCode == BiometricConstants.ERROR_CANCELED
                ) {
                    activity?.runOnUiThread {
                        setFingerprintInputScreenVisible(false)
                    }
                }
            }
        })
        biometricPrompt.authenticate(promptInfo, cryptoObject)
    }

    private fun setFingerprintInputScreenVisible(fingerprintVisible: Boolean) {
        fingerprintInput.visibility = if (fingerprintVisible) View.VISIBLE else View.INVISIBLE
        pinUnlock.visibility = if (fingerprintVisible) View.INVISIBLE else View.VISIBLE
    }

}

class PinPagesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var pinPages = mutableListOf<PinPage>()
    var shakePageIndex: Int? = null

    fun updateTopTextForPage(text: TopText, pageIndex: Int) {
        pinPages[pageIndex].topText = text
        notifyDataSetChanged()
    }

    fun setEnteredPinLength(pageIndex: Int, enteredLength: Int) {
        pinPages[pageIndex].enteredDigitsLength = enteredLength
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PinPageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_pin_page, parent, false))
    }

    override fun getItemCount() = pinPages.count()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PinPageViewHolder) {
            holder.bind(pinPages[position], shakePageIndex == position)//, { listener.onChangeProvider(numPadItems[position]) }, listener.isBiometricEnabled())
        }
    }

}

class PinPageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
    private var bigError: TextView = itemView.findViewById(R.id.txtBigError)
    private var txtDesc: TextView = itemView.findViewById(R.id.txtDescription)
    private var smallError: TextView = itemView.findViewById(R.id.txtSmallError)
    private var pinCirclesWrapper = itemView.findViewById<ConstraintLayout>(R.id.pinCirclesWrapper)

    private var imgPinMask1: ImageView = itemView.findViewById(R.id.imgPinMaskOne)
    private var imgPinMask2: ImageView = itemView.findViewById(R.id.imgPinMaskTwo)
    private var imgPinMask3: ImageView = itemView.findViewById(R.id.imgPinMaskThree)
    private var imgPinMask4: ImageView = itemView.findViewById(R.id.imgPinMaskFour)
    private var imgPinMask5: ImageView = itemView.findViewById(R.id.imgPinMaskFive)
    private var imgPinMask6: ImageView = itemView.findViewById(R.id.imgPinMaskSix)

    fun bind(pinPage: PinPage, shake: Boolean) {
        bigError.visibility = View.GONE
        txtDesc.visibility = View.GONE
        txtTitle.visibility = View.GONE
        smallError.visibility = View.GONE

        when (pinPage.topText) {
            is TopText.Title -> {
                txtTitle.visibility = View.VISIBLE
                txtTitle.setText(pinPage.topText.text)
            }
            is TopText.BigError -> {
                bigError.visibility = View.VISIBLE
                bigError.setText(pinPage.topText.text)
            }
            is TopText.Description -> {
                txtDesc.visibility = View.VISIBLE
                txtDesc.setText(pinPage.topText.text)
            }
            is TopText.SmallError -> {
                smallError.visibility = View.VISIBLE
                smallError.setText(pinPage.topText.text)
            }
        }

        updatePinCircles(pinPage.enteredDigitsLength)
        if (shake) {
            val shakeAnim = AnimationUtils.loadAnimation(itemView.context, R.anim.shake_pin_circles)
            pinCirclesWrapper.startAnimation(shakeAnim)
        }

    }

    private fun updatePinCircles(length: Int) {
        val filledCircle = R.drawable.ic_pin_ellipse_yellow
        val emptyCircle = R.drawable.ic_pin_ellipse

        imgPinMask1.setImageResource(if (length > 0) filledCircle else emptyCircle)
        imgPinMask2.setImageResource(if (length > 1) filledCircle else emptyCircle)
        imgPinMask3.setImageResource(if (length > 2) filledCircle else emptyCircle)
        imgPinMask4.setImageResource(if (length > 3) filledCircle else emptyCircle)
        imgPinMask5.setImageResource(if (length > 4) filledCircle else emptyCircle)
        imgPinMask6.setImageResource(if (length > 5) filledCircle else emptyCircle)
    }

}

class PinPage(var topText: TopText, var enteredDigitsLength: Int = 0)