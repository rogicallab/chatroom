package com.example.prototype1.Auth

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.prototype1.MainActivity
import com.example.prototype1.R
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.database.paging.FirebaseDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_anonymous_account_setting.*

class AnonymousAccountSettingActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anonymous_account_setting)
        // toolbarの設定
        setSupportActionBar(findViewById(R.id.toolbar_anonymous_account_setting))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid
        println("photoURL:"+user?.photoUrl)
        textView_anony_title.setText("匿名アカウントでログインしています")
        textView_anony_id.setText("id:"+uid)
        // 新規アカウント作成
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build())
        button_create_account.setOnClickListener {
            // Create and launch sign-in intent
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .setTheme(R.style.AppTheme)
                    .build(),
                RC_SIGN_IN
            )
        }
    }

    // アカウント作成の結果呼び出されるメソッドのオーバーライド
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        println("オーバーライドされたメソッドが呼び出された")
        if(requestCode == RC_SIGN_IN){

            if(resultCode == RESULT_OK){
                val user = FirebaseAuth.getInstance().currentUser
                val photoUrl = user!!.photoUrl
                val ONE_MEGABYTE: Long = 1024 * 1024
                println("userName:"+user?.displayName)
                // 画像が設定されていない場合デフォルトの画像を設定する
                if(photoUrl == null){
                    // 写真が設定されていない時（アカウント作成直後など）
                    // firebaseにおいてあるデフォルトの画像を自分のprofilePhotoとして保存する
                    val storageRef = FirebaseStorage.getInstance().reference
                    val imagesRef: StorageReference? = storageRef.child("/images/user_profile.png")
                    imagesRef?.getBytes(ONE_MEGABYTE)?.addOnSuccessListener { bytes ->
                        val filename = user?.uid.toString()
                        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
                        val myUri = Uri.parse(ref.toString())
                        ref.putBytes(bytes)
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setPhotoUri(myUri)
                            .build()
                        user.updateProfile(profileUpdates)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    println("User profile updated.")
                                }
                            }
                    }
                }
                //koko
                setUserData(user.uid,photoUrl.toString())
                // MainActivityに移動
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent)
            }else{
                println("アカウント作成に失敗しました")
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==android.R.id.home){
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    // sing in するためのフローが完了したのちにonActivityRestart()が呼び出される。そこで・・
    companion object {
        private const val RC_SIGN_IN = 123 // RCはrequest codeの略だと思われる
    }
   fun setUserData(id:String,fUrl:String){
        val reference=FirebaseDatabase.getInstance().reference.child("user").child(id)
        reference.child("fUrl").setValue(fUrl)
    }
}
