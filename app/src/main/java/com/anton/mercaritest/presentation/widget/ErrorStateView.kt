package com.anton.mercaritest.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import com.anton.mercaritest.R
import com.anton.mercaritest.presentation.error.ErrorData
import kotlinx.android.synthetic.main.layout_error_state.view.*
import kotlin.properties.Delegates


const val ERROR_STATE_TYPE_NO_CONNECTION = 0
const val ERROR_STATE_TYPE_WARNING = 1

class ErrorStateView : FrameLayout {

    var errorMessage: String? by Delegates.observable<String?>(null) { _, _, _ ->
        updateErrorMsg()
    }

    var type: Int by Delegates.observable(ERROR_STATE_TYPE_WARNING) { _, _, _ ->
        updateIcon()
    }

    var onActionBtnClick: (() -> Unit)? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        val stateView = LayoutInflater.from(context).inflate(R.layout.layout_error_state, this, false)
        addView(stateView)
        updateErrorMsg()
        initActionBtnClick()
    }

    fun setError(errorData: ErrorData) {
        errorMessage = errorData.getLongMsg(context)
        type = if (errorData is ErrorData.Network) ERROR_STATE_TYPE_NO_CONNECTION else ERROR_STATE_TYPE_WARNING
    }

    private fun updateErrorMsg() {
        errorStateMessage.text = errorMessage
    }

    private fun updateIcon() {
        errorStateImg.setImageResource(
            when (type) {
                ERROR_STATE_TYPE_NO_CONNECTION -> R.drawable.ic_offline_80
                else -> R.drawable.ic_error_80
            }
        )
    }

    private fun initActionBtnClick() {
        findViewById<TextView>(R.id.errorStateBtn).setOnClickListener {
            onActionBtnClick?.invoke()
        }
    }
}