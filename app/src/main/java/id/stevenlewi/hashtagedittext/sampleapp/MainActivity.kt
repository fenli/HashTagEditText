package id.stevenlewi.hashtagedittext.sampleapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.stevenlewi.hashtagedittext.HashTagEditText

class MainActivity : AppCompatActivity() {

    private lateinit var hashTagEditText: HashTagEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        hashTagEditText = findViewById(R.id.tag_input) as HashTagEditText

        // Insert tag
        hashTagEditText.appendTag("hello")

        // Insert multiple tags
        hashTagEditText.appendTags("android", "kotlin", "tags", "edit-text")
        hashTagEditText.appendTags(listOf("github", "opensource"))

        // Get tags value
        val allTags = hashTagEditText.values
    }
}
