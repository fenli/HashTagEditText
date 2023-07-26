/*
* Copyright (C) 2023 Steven Lewi
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package id.stevenlewi.hashtagedittext

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.Spannable
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.text.style.ImageSpan
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat

class HashTagEditText : AppCompatEditText {

    companion object {
        private const val DEFAULT_MAX_SIZE = -1 // No tags limit
        private const val ALLOWED_CHARS =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789,-_ \n"
    }

    private val mTagTriggerChars = arrayOf(' ', ',', '\n')
    private lateinit var mValuesSpan: ArrayList<HashTagSpan>
    private lateinit var bubbleRootView: ViewGroup
    private lateinit var bubbleTextView: TextView
    private var mBubbleBackground: Drawable? = null
    private var mMaxSize = 0
    private var mBubbleTextSize = 0f
    private var mBubbleTextColor = 0
    private var mHorizontalSpacing = 0
    private var mVerticalSpacing = 0
    private var mHorizontalPadding = 0
    private var mVerticalPadding = 0

    constructor(context: Context) : super(context) {
        if (!isInEditMode) {
            initAttributeSet(null)
            initView()
        }
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        if (!isInEditMode) {
            initAttributeSet(attrs)
            initView()
        }
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        if (!isInEditMode) {
            initAttributeSet(attrs)
            initView()
        }
    }

    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
        super.onSelectionChanged(selStart, selEnd)
        if (selStart < length()) {
            setSelection(length())
        }
    }

    private fun initAttributeSet(attrs: AttributeSet?) {
        if (attrs != null) {
            val style = context.obtainStyledAttributes(attrs, R.styleable.HashTagEditText, 0, 0)
            mMaxSize = style.getInteger(R.styleable.HashTagEditText_maxSize, DEFAULT_MAX_SIZE)
            mBubbleTextSize = style.getDimension(
                R.styleable.HashTagEditText_bubbleTextSize,
                textSize
            )
            mBubbleTextColor = style.getColor(
                R.styleable.HashTagEditText_bubbleTextColor,
                currentTextColor
            )
            mHorizontalSpacing =
                style.getDimensionPixelSize(R.styleable.HashTagEditText_horizontalSpacing, 0)
            mVerticalSpacing =
                style.getDimensionPixelSize(R.styleable.HashTagEditText_verticalSpacing, 0)
            mHorizontalPadding =
                style.getDimensionPixelSize(R.styleable.HashTagEditText_horizontalPadding, 0)
            mVerticalPadding =
                style.getDimensionPixelSize(R.styleable.HashTagEditText_verticalPadding, 0)
            mBubbleBackground = style.getDrawable(R.styleable.HashTagEditText_bubbleBackground)
            if (mBubbleBackground == null) {
                mBubbleBackground = ContextCompat.getDrawable(context, R.drawable.bg_default_bubble)
            }
            style.recycle()
        } else {
            mBubbleTextSize = textSize
            mBubbleTextColor = currentTextColor
            mHorizontalSpacing = 0
            mVerticalSpacing = 0
            mHorizontalPadding = 0
            mVerticalPadding = 0
            mBubbleBackground = ContextCompat.getDrawable(context, R.drawable.bg_default_bubble)
        }

        mValuesSpan = if (mMaxSize == DEFAULT_MAX_SIZE) ArrayList() else ArrayList(mMaxSize)
    }

    private fun initView() {
        bubbleRootView = inflate(context, R.layout.default_bubble_item, null) as ViewGroup
        bubbleRootView.setPadding(
            mHorizontalSpacing,
            mVerticalSpacing,
            mHorizontalSpacing,
            mVerticalSpacing
        )
        bubbleTextView = bubbleRootView.findViewById<View>(R.id.hashtag_text_item) as TextView
        bubbleTextView.setTextColor(mBubbleTextColor)
        bubbleTextView.setPadding(
            mHorizontalPadding,
            mVerticalPadding,
            mHorizontalPadding,
            mVerticalPadding
        )
        bubbleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mBubbleTextSize)
        bubbleTextView.background = mBubbleBackground
        isSingleLine = false
        imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI
        inputType = InputType.TYPE_CLASS_TEXT or
                InputType.TYPE_TEXT_FLAG_MULTI_LINE or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
        filters = arrayOf<InputFilter>(DigitsKeyListener.getInstance(ALLOWED_CHARS))
        addTextChangedListener(HashTagWatcher())
    }

    fun appendTag(tag: String) {
        append(tag.replace(" ", "") + ' ')
    }

    fun appendTags(tags: List<String>) {
        tags.forEach { appendTag(it) }
    }

    fun appendTags(vararg tags: String) {
        appendTags(*tags)
    }

    val values: List<String>
        get() {
            val values: MutableList<String> = ArrayList()
            mValuesSpan.forEach {
                values.add(it.text)
            }
            return values
        }

    private val lastOffset: Int
        get() {
            var lastOffset = 0
            mValuesSpan.forEach {
                lastOffset += it.text.length
            }
            return lastOffset
        }

    fun redrawHashTagBubble() {
        var index = 0
        mValuesSpan.forEach { hashTagSpan ->
            try {
                text?.setSpan(
                    hashTagSpan.span, index, index + hashTagSpan.text.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
            index += hashTagSpan.text.length
        }
    }

    private fun generateBubbleBitmap(tag: String): BitmapDrawable {
        bubbleTextView.text = tag
        val spec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        bubbleRootView.measure(spec, spec)
        bubbleRootView.layout(0, 0, bubbleRootView.measuredWidth, bubbleRootView.measuredHeight)
        val b = Bitmap.createBitmap(
            /* width = */ bubbleRootView.measuredWidth,
            /* height = */ bubbleRootView.measuredHeight,
            /* config = */ Bitmap.Config.ARGB_8888
        )
        val c = Canvas(b)
        c.translate(-bubbleRootView.scrollX.toFloat(), -bubbleRootView.scrollY.toFloat())
        bubbleRootView.draw(c)
        bubbleRootView.isDrawingCacheEnabled = true
        val cacheBmp = bubbleRootView.drawingCache
        val viewBmp = cacheBmp.copy(Bitmap.Config.ARGB_8888, true)
        bubbleRootView.destroyDrawingCache()
        val bubbleBitmapDrawable = BitmapDrawable(resources, viewBmp)
        bubbleBitmapDrawable.setBounds(
            /* left = */ 0,
            /* top = */ 0,
            /* right = */ bubbleBitmapDrawable.intrinsicWidth,
            /* bottom = */ bubbleBitmapDrawable.intrinsicHeight
        )
        return bubbleBitmapDrawable
    }

    private inner class HashTagWatcher : TextWatcher {

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            // No-op
        }

        @Synchronized
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            removeTextChangedListener(this)
            if (s.isNotEmpty()) {
                if (s.length < lastOffset) {
                    text?.removeSpan(mValuesSpan[mValuesSpan.size - 1].span)
                    mValuesSpan.removeAt(mValuesSpan.size - 1)
                } else if (mValuesSpan.size >= mMaxSize && mMaxSize != -1) {
                    if (count > before) {
                        text = text?.delete(s.length - 1, s.length)
                    }
                } else {
                    val last = s[s.length - 1]
                    if (mTagTriggerChars.contains(last)) {
                        text?.delete(s.length - 1, s.length)
                        if (s.length > lastOffset) {
                            val lastTag = s.subSequence(lastOffset, s.length)
                            val tagBitmap = generateBubbleBitmap(lastTag.toString())
                            val span = ImageSpan(tagBitmap, lastTag.toString())
                            mValuesSpan.add(HashTagSpan(lastTag.toString(), span))
                        }
                    }
                }
            } else {
                mValuesSpan.clear()
            }
            redrawHashTagBubble()
            addTextChangedListener(this)
        }

        override fun afterTextChanged(s: Editable) {
            // No-op
        }
    }
}
