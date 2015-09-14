# HashTagEditText
EditText for Hashtags input.

![Screenshot] (https://lh3.googleusercontent.com/az5qta7BY6rt1GJ1ZuHIaOxW2kVPg9510_igrcluyPBzhltUWcM=w443-h738-no)

## Importing the library
You can import this library to your project by adding following dependency to your `build.gradle` :
```gradle
repositories {
    jcenter()
}

dependencies {
    compile 'com.fenlisproject.hashtagedittext:library:1.0.0'
}
```

## How to Use
Basic :
```xml
<com.fenlisproject.hashtagedittext.HashTagEditText
        android:id="@+id/tag_input"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
```

All Attributes :
```xml
<com.fenlisproject.hashtagedittext.HashTagEditText
        android:id="@+id/tag_input"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:bubbleTextColor="#555"
        app:bubbleTextSize="16sp"
        app:horizontalPadding="8dp"
        app:horizontalSpacing="2dp"
        app:maxSize="10"
        app:bubbleBackground="@drawable/bg_tag_bubble"
        app:verticalPadding="4dp"
        app:verticalSpacing="2dp" />
```

##### XML Atrributes : 
- bubbleTextColor : Text color for each bubble
- bubbleTextSize : Text size for each bubble
- horizontalPadding : Left and right padding for each bubble
- verticalPadding : Top and bottom padding for each bubble
- horizontalSpacing : left-right margin between bubble
- verticalSpacing : top-bottom margin between bubble
- maxSize : Maximum tag allowed to enter
- bubbleBackground : Custom background for bubble
