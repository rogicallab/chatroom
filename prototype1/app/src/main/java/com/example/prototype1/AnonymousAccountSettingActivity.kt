package com.example.prototype1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_anonymous_account_setting.*

class AnonymousAccountSettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anonymous_account_setting)

        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid
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
                RC_SIGN_IN)
        }
    }

    // アカウント作成の結果呼び出されるメソッドのオーバーライド
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        println("オーバーライドされたメソッドが呼び出された")
        if(requestCode == RC_SIGN_IN){

            if(resultCode == RESULT_OK){
                println("アカウント作成に成功しました")
                //startActivity(SignedInActivity.createIntent(this, response));
                //finish();
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            }else{
                println("アカウント作成に失敗しました")
            }
        }
    }

    // sing in するためのフローが完了したのちにonActivityRestart()が呼び出される。そこで・・
    companion object {
        private const val RC_SIGN_IN = 123 // RCはrequest codeの略だと思われる
    }
}
