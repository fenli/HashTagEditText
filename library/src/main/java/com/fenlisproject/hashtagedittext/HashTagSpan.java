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

import android.text.style.ImageSpan;

class HashTagSpan {

    private String text;
    private ImageSpan span;

    public HashTagSpan(String text, ImageSpan span) {
        this.text = text;
        this.span = span;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ImageSpan getSpan() {
        return span;
    }

    public void setSpan(ImageSpan span) {
        this.span = span;
    }
}
