package com.example.lesson3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.lesson3.databinding.ActivityProfileBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Step 1: Obtain a reference to a Firebase database.
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val educationDatabase: DatabaseReference = database.getReference("educationDatabase")

        // Step 2: Bind the data, that gets set in the XML, to an object.
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Step 3: Update or Add a user profile to the database when the save button is clicked.
        binding.profileSave.setOnClickListener {
            val userName = binding.profileUsername.text.toString()
            val fullName = binding.profileFullName.text.toString()
            val email = binding.profileEmailAddress.text.toString()

            educationDatabase.child("userProfiles").child(userName).apply {
                child("userName").setValue(userName)
                child("fullName").setValue(fullName)
                child("email").setValue(email).addOnSuccessListener {
                    Toast.makeText(baseContext, "Profile updated successfully.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
