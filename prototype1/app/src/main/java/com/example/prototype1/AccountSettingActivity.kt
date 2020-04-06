package com.example.prototype1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_account_setting.*

class AccountSettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_setting)
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // User is signed in
            val name = user.displayName
            val email = user.email
            val photoUrl = user.photoUrl

            // Check if user's email is verified
            val emailVerified = user.isEmailVerified

            // set text
            TextView_displayName.setText("name:"+name)
            TextView_email.setText("email:"+email)
            TextView_photoUrl.setText("photoUrl:"+photoUrl)
            TextView_isEmailVerified.setText("isEmailVerified:"+emailVerified)


        } else {
            // No user is signed in
            println("user is null")
        }

    }
}
