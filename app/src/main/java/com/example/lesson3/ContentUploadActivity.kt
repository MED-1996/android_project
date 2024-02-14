package com.example.lesson3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.lesson3.databinding.ActivityContentUploadBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.UUID

class ContentUploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContentUploadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_upload)

        // Step 1: Obtain a reference to a Firebase database.
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val educationDatabase: DatabaseReference = database.getReference("educationDatabase")

        // Step 2: Bind the data, that gets set in the XML, to an object.
        binding = ActivityContentUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.submitContentButton.setOnClickListener {
            val content = binding.content.text.toString() // Capture Content
            val postView = TextView(this).apply {
                text = content
            }
            binding.linearLayoutContent.addView(postView) // Add to LinearLayout
            binding.content.setText("")  // Clear input field for the next post

            // Generate a unique ID for the post
            val postId = UUID.randomUUID().toString()
            // Save the post content under the "posts" node with the unique ID
            educationDatabase.child("posts").child(postId).setValue(content)
        }
    }
}