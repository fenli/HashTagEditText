package id.fenli.hashtagedittext.sampleapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.fenli.hashtagedittext.HashTagEditText

class MainActivity : AppCompatActivity() {

    private lateinit var hashTagInput: HashTagEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        hashTagInput = findViewById(R.id.tag_input) as HashTagEditText

        // Insert tag
        hashTagInput.appendTag("hello")

        // Insert multiple tags
        hashTagInput.appendTags(
            listOf("android", "kotlin", "tags", "edit-text")
        )

        // Get tags value
        val allTags = hashTagInput.values
    }
}
