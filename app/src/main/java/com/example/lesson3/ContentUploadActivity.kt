package com.example.lesson3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.lesson3.databinding.ActivityContentUploadBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DatabaseError
import java.util.UUID

class ContentUploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContentUploadBinding

    private fun fetchPost() {
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val educationDatabase: DatabaseReference = database.getReference("educationDatabase")

        // Note: Replace 'specificPostId' with the actual ID of the post you want to test
        val postRef = educationDatabase.child("posts").child("498ca568-f747-4771-bd0d-af967207e10c")
        postRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Extract the post's text from the snapshot
                    val postText = snapshot.value.toString()
                    // Example of how you can display this text in your app's UI
                    Log.d("FirebaseData", "Post text: $postText")
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // Handle any errors here
                Log.e("FirebaseData", "Error fetching post: ${error.message}")
            }
        })
    }

    private fun fetchAllPosts() {
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val educationDatabase: DatabaseReference = database.getReference("educationDatabase")
        val postsRef = educationDatabase.child("posts")

        postsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    snapshot.children.forEach { childSnapshot ->
                        val postText = childSnapshot.value.toString()
                        Log.e("FirebaseData","Posttext: ${postText}")
                        // Create a TextView for each post and add it to your layout
                        val postView = TextView(this@ContentUploadActivity).apply {
                            text = postText
                        }
                        // Add the TextView to your layout
                        binding.linearLayoutContent.addView(postView)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // Handle any errors here
                Log.e("FirebaseData", "Error fetching post: ${error.message}")
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_upload)

        // Step 1: Obtain a reference to a Firebase database.
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val educationDatabase: DatabaseReference = database.getReference("educationDatabase")

        // Step 2: Bind the data, that gets set in the XML, to an object.
        binding = ActivityContentUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Log and individual post to the Logcat
        fetchPost()

        // Show all posts on the content page.
        fetchAllPosts()

        binding.submitContentButton.setOnClickListener {
            val content = binding.content.text.toString() // Capture Content
            val username = "dummy username" // todo: Need to fetch the actual username in the future
            val postMap = hashMapOf(
                "text" to content,
                "username" to username
            )
            binding.content.setText("")
            binding.linearLayoutContent.removeAllViews()  // Clear all posts so that the new list can be refreshed.
            val postId = UUID.randomUUID().toString()
            educationDatabase.child("posts").child(postId).setValue(postMap)
        }
    }
}