HashTagEditText
===============

EditText for multiple tags input.

![Demo](https://github.com/fenli/HashTagEditText/assets/5110285/e7150cbd-0e9c-49f6-a563-06b81320184e)

## Importing the library
You can import this library to your project by adding following dependency to your `build.gradle`:
```gradle
dependencies {
    implementation 'id.stevenlewi:hashtag-edittext:1.0.1'
}
```

## How to Use
#### XML:
```xml
<id.stevenlewi.hashtagedittext.HashTagEditText
    android:id="@+id/tag_input"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```
```xml
<id.stevenlewi.hashtagedittext.HashTagEditText
    android:id="@+id/tag_input"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:bubbleTextColor="#555"
    app:bubbleTextSize="16sp"
    app:horizontalPadding="8dp"
    app:horizontalSpacing="2dp"
    app:maxSize="10"
    app:bubbleBackground="@drawable/custom_background"
    app:verticalPadding="4dp"
    app:verticalSpacing="2dp" />
```

| View Attribute    | Type              | Description                            |
|-------------------|-------------------|----------------------------------------|
| bubbleTextColor   | Color             | Text color for each bubble             |
| bubbleTextSize    | Dimen             | Text size for each bubble              |
| horizontalPadding | Dimen             | Left and right padding for each bubble |
| verticalPadding   | Dimen             | Top and bottom padding for each bubble |
| horizontalSpacing | Dimen             | Top and bottom padding for each bubble |
| verticalSpacing   | Dimen             | Top and bottom padding for each bubble |
| maxSize           | Int               | Maximum tag allowed to enter           |
| bubbleBackground  | Drawable or Color | Custom background for bubble           |

#### Code:
```kotlin
// Insert single tag
hashTagEditText.appendTag("hello")

// Insert multiple tags
hashTagEditText.appendTags("android", "kotlin", "tags", "edit-text")
hashTagEditText.appendTags(listOf("github", "opensource"))

// Get tags value as list of string
val allTags = hashTagEditText.values

// Don't set text directly like this (not supported yet)
// hashTagEditText.setText("android,kotlin")
```

License
-------

    Copyright (C) 2023 Steven Lewi

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
