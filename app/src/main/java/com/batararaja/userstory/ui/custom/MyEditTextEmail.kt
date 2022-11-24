package com.batararaja.userstory.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import com.batararaja.userstory.R

class MyEditTextEmail : AppCompatEditText {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs){
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (!Patterns.EMAIL_ADDRESS.matcher(p0.toString()).matches()  ) showError(context.resources.getString(R.string.error_email)) else showError(null)
            }

        })
    }

    private fun showError(message : String?) {
        error = message
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        hint = context.resources.getString(R.string.email)
    }
}