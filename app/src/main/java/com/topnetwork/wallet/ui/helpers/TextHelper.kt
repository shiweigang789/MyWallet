package com.topnetwork.wallet.ui.helpers

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.topnetwork.views.helpers.LayoutHelper
import com.topnetwork.wallet.core.App
import com.topnetwork.wallet.core.IClipboardManager

/**
 * @FileName     : TextHelper
 * @date         : 2020/6/11 17:30
 * @author       : Owen
 * @description  :
 */
object TextHelper : IClipboardManager {

    private val clipboard: ClipboardManager?
        get() = App.instance.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager

    override val hasPrimaryClip: Boolean
        get() = clipboard?.hasPrimaryClip() ?: false

    override fun copyText(text: String) {
        copyTextToClipboard(App.instance, text)
    }

    override fun getCopiedText(): String {
        return clipboard?.primaryClip?.itemCount?.let { count ->
            if (count > 0) {
                clipboard?.primaryClip?.getItemAt(0)?.text?.toString()
            } else {
                null
            }
        } ?: ""
    }

    fun getQrCodeBitmap(address: String, size: Float = 150F): Bitmap? {
        val multiFormatWriter = MultiFormatWriter()
        return try {
            val imageSize = LayoutHelper.dp(size, App.instance)
            val bitMatrix = multiFormatWriter.encode(address, BarcodeFormat.QR_CODE, imageSize, imageSize, hashMapOf(
                EncodeHintType.MARGIN to 0))
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.createBitmap(bitMatrix)
            bitmap
        } catch (e: WriterException) {
            e.printStackTrace()
            null
        }
    }

    private fun copyTextToClipboard(context: Context, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        val clip = ClipData.newPlainText("text", text)
        clipboard?.setPrimaryClip(clip)
    }

}