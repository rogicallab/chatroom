package com.example.prototype1

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_account_setting.*


class AccountSettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_setting)
        // toolbarの設定
        setSupportActionBar(findViewById(R.id.toolbar_account_setting))
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // User is signed in
            val name = user.displayName
            val photoUrl = user.photoUrl

            // Check if user's email is verified
            val emailVerified = user.isEmailVerified

            // set text
            TextView_displayName.setText(name)

            // 画像の表示
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
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent)
            }

            // アカウント削除ボタン
            // 確認ボタンも追加したい
            button_delete_account.setOnClickListener {
                FirebaseAuth.getInstance().currentUser!!.delete()
                // アカウント削除後すぐにcurrentUserがnullになるわけではないため、ここで匿名アカウントとしてログインしておく
                FirebaseAuth.getInstance().signInAnonymously()
                val intent = Intent(this,MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent)
            }

        } else {
            // No user is signed in
            println("user is null")
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==android.R.id.home){
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
