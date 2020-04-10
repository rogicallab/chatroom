package com.example.prototype1

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
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
            println("photoUrl:$photoUrl")

            // Check if user's email is verified
            val emailVerified = user.isEmailVerified

            // set text
            TextView_displayName.setText(name)
            // set image
            val ONE_MEGABYTE: Long = 1024 * 1024
            if(photoUrl != null){
                // 参照の作成
                val gsReference = FirebaseStorage.getInstance().getReferenceFromUrl(photoUrl.toString())
                gsReference.getBytes(ONE_MEGABYTE).addOnSuccessListener { bytes ->
                    // Data for "images/island.jpg" is returned, use this as needed
                    //val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, bytes.)
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    imageView.setImageBitmap(bitmap)
                }.addOnFailureListener {
                    // Handle any errors
                }
            }

            // 設定変更ボタン
            button_setting_change.setOnClickListener {
                val intent = Intent(this,ChangeAccountSettingActivity::class.java)
                startActivity(intent)
            }

            // サインアウトボタン
            button_log_out.setOnClickListener{
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            }

            // アカウント削除ボタン
            // 確認ボタンも追加したい
            button_delete_account.setOnClickListener {
                FirebaseAuth.getInstance().currentUser!!.delete()
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            }

        } else {
            // No user is signed in
            println("user is null")
        }

    }
}
