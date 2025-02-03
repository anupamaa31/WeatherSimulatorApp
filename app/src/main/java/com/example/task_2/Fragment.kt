package com.example.task_2

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class Fragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment, container, false)

        // Retrieve views
        val userIdInput = view.findViewById<EditText>(R.id.username)
        val passwordInput = view.findViewById<EditText>(R.id.password)
        val submitButton = view.findViewById<Button>(R.id.Submit_Button)

        // Handle button click
        submitButton.setOnClickListener {
            val userId = userIdInput.text.toString()
            val password = passwordInput.text.toString()

            if (validateCredentials(userId, password)) {
                // Navigate to the Dashboard if validation is successful
                startActivity(Intent(activity, Dashboard::class.java))
            } else {
                // Show a toast message for invalid credentials
                Toast.makeText(activity, "Invalid User ID or Password", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    // Function to validate the user ID and password
    private fun validateCredentials(userId: String, password: String): Boolean {
        // Replace with actual validation logic (e.g., check against stored credentials)
        return userId == "ANU" && password == "4713"
    }
}
