/*
* Copyright (C) 2015 Steven Lewi
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

package com.fenlisproject.hashtagedittext;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HashTagEditText extends EditText {

    private final String ALLOWED_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789, \n";
    private List<HashTagSpan> mValuesSpan;
    private ViewGroup bubbleRootView;
    private TextView bubbleTextView;
    private int mMaxSize;
    private Drawable mBubbleBackground;
    private float mBubbleTextSize;
    private int mBubbleTextColor;
    private int mHorizontalSpacing;
    private int mVerticalSpacing;
    private int mHorizontalPadding;
    private int mVerticalPadding;

    public HashTagEditText(Context context) {
        super(context);
        if (!isInEditMode()) {
            initAttributeSet(null);
            initView();
        }
    }

    public HashTagEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            initAttributeSet(attrs);
            initView();
        }
    }

    public HashTagEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode()) {
            initAttributeSet(attrs);
            initView();
        }
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        if (selStart < length()) {
            setSelection(length());
        }
    }

    private void initAttributeSet(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray style = getContext().obtainStyledAttributes(attrs, R.styleable.HashTagEditText, 0, 0);
            mMaxSize = style.getInteger(R.styleable.HashTagEditText_maxSize, -1);
            mBubbleTextSize = style.getDimension(R.styleable.HashTagEditText_bubbleTextSize, getTextSize());
            mBubbleTextColor = style.getColor(R.styleable.HashTagEditText_bubbleTextColor, getCurrentTextColor());
            mHorizontalSpacing = style.getDimensionPixelSize(R.styleable.HashTagEditText_horizontalSpacing, 0);
            mVerticalSpacing = style.getDimensionPixelSize(R.styleable.HashTagEditText_verticalSpacing, 0);
            mHorizontalPadding = style.getDimensionPixelSize(R.styleable.HashTagEditText_horizontalPadding, 0);
            mVerticalPadding = style.getDimensionPixelSize(R.styleable.HashTagEditText_verticalPadding, 0);
            mBubbleBackground = style.getDrawable(R.styleable.HashTagEditText_bubbleBackground);
            if (mBubbleBackground == null) {
                mBubbleBackground = getContext().getResources().getDrawable(R.drawable.bg_default_bubble);
            }
            style.recycle();
        } else {
            mBubbleTextSize = getTextSize();
            mBubbleTextColor = getCurrentTextColor();
            mHorizontalSpacing = 0;
            mVerticalSpacing = 0;
            mHorizontalPadding = 0;
            mVerticalPadding = 0;
            mBubbleBackground = getContext().getResources().getDrawable(R.drawable.bg_default_bubble);
        }
        if (mMaxSize == -1) {
            mValuesSpan = new ArrayList<>();
        } else {
            mValuesSpan = new ArrayList<>(mMaxSize);
        }
    }

    private void initView() {
        bubbleRootView = (ViewGroup) View.inflate(getContext(), R.layout.default_bubble_item, null);
        bubbleRootView.setPadding(mHorizontalSpacing, mVerticalSpacing, mHorizontalSpacing, mVerticalSpacing);

        bubbleTextView = (TextView) bubbleRootView.findViewById(R.id.hashtag_text_item);
        bubbleTextView.setTextColor(mBubbleTextColor);
        bubbleTextView.setPadding(mHorizontalPadding, mVerticalPadding, mHorizontalPadding, mVerticalPadding);
        bubbleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mBubbleTextSize);
        bubbleTextView.setBackgroundDrawable(mBubbleBackground);

        setSingleLine(false);
        setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_FLAG_MULTI_LINE |
                InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        setFilters(new InputFilter[]{DigitsKeyListener.getInstance(ALLOWED_CHARS)});
        addTextChangedListener(new HashTagWatcher());
    }

    public void appendTag(String tag) {
        append(tag.replace(" ", "") + ' ');
    }

    public List<String> getValues() {
        List<String> values = new ArrayList<>();
        for (HashTagSpan span : mValuesSpan) {
            values.add(span.getText());
        }
        return values;
    }

    private int getLastOffset() {
        int lastOffset = 0;
        for (HashTagSpan span : mValuesSpan) {
            lastOffset += span.getText().length();
        }
        return lastOffset;
    }

    public void reDrawHashTagBubble() {
        int index = 0;
        for (HashTagSpan span : mValuesSpan) {
            try {
                getText().setSpan(span.getSpan(), index, index + span.getText().length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            index += span.getText().length();
        }
    }

    private BitmapDrawable generateBubbleBitmap(String tag) {
        bubbleTextView.setText(tag);
        int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        bubbleRootView.measure(spec, spec);
        bubbleRootView.layout(0, 0, bubbleRootView.getMeasuredWidth(), bubbleRootView.getMeasuredHeight());
        Bitmap b = Bitmap.createBitmap(bubbleRootView.getMeasuredWidth(), bubbleRootView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        c.translate(-bubbleRootView.getScrollX(), -bubbleRootView.getScrollY());
        bubbleRootView.draw(c);
        bubbleRootView.setDrawingCacheEnabled(true);
        Bitmap cacheBmp = bubbleRootView.getDrawingCache();
        Bitmap viewBmp = cacheBmp.copy(Bitmap.Config.ARGB_8888, true);
        bubbleRootView.destroyDrawingCache();
        BitmapDrawable bd = new BitmapDrawable(getResources(), viewBmp);
        if (bd != null) {
            bd.setBounds(0, 0, bd.getIntrinsicWidth(), bd.getIntrinsicHeight());
        }
        return bd;
    }

    private class HashTagWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public synchronized void onTextChanged(CharSequence s, int start, int before, int count) {
            removeTextChangedListener(this);
            if (s.length() > 0) {
                if (s.length() < getLastOffset()) {
                    getText().removeSpan(mValuesSpan.get(mValuesSpan.size() - 1).getSpan());
                    mValuesSpan.remove(mValuesSpan.size() - 1);
                } else if (mValuesSpan.size() >= mMaxSize && mMaxSize != -1) {
                    if (count > before) {
                        setText(getText().delete(s.length() - 1, s.length()));
                    }
                } else {
                    char last = s.charAt(s.length() - 1);
                    if (last == ' ' || last == ',' || last == '\n') {
                        getText().delete(s.length() - 1, s.length());
                        if (s.length() > getLastOffset()) {
                            CharSequence lastTag = s.subSequence(getLastOffset(), s.length());
                            BitmapDrawable tagBitmap = generateBubbleBitmap(lastTag.toString());
                            ImageSpan span = new ImageSpan(tagBitmap, lastTag.toString());
                            mValuesSpan.add(new HashTagSpan(lastTag.toString(), span));
                        }
                    }
                }
            } else {
                mValuesSpan.clear();
            }
            reDrawHashTagBubble();
            addTextChangedListener(this);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }
}
