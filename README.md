HashTagEditText
===============

EditText for multiple tags input.

![Screenshot] (https://lh3.googleusercontent.com/az5qta7BY6rt1GJ1ZuHIaOxW2kVPg9510_igrcluyPBzhltUWcM=w443-h738-no)

## Importing the library
You can import this library to your project by adding following dependency to your `build.gradle`:
```gradle
dependencies {
    implementation 'id.fenli.hashtagedittext:library:1.0.0'
}
```

## How to Use
Basic:
```xml
<com.fenli.hashtagedittext.HashTagEditText
    android:id="@+id/tag_input"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```

All attributes:
```xml
<com.fenli.hashtagedittext.HashTagEditText
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

Usage in code:
```kotlin
// Insert tag
hashTagInput.appendTag("hello")

// Insert multiple tags
hashTagInput.appendTags(
    listOf("android", "kotlin", "tags", "edit-text")
)

// Get tags value
val allTags = hashTagInput.values
```

##### View Attributes:
- bubbleTextColor : Text color for each bubble
- bubbleTextSize : Text size for each bubble
- horizontalPadding : Left and right padding for each bubble
- verticalPadding : Top and bottom padding for each bubble
- horizontalSpacing : left-right margin between bubble
- verticalSpacing : top-bottom margin between bubble
- maxSize : Maximum tag allowed to enter
- bubbleBackground : Custom background for bubble

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