package com.example.prototype1.Setting

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.prototype1.Auth.AccountSettingActivity
import com.example.prototype1.Auth.AnonymousAccountSettingActivity
import com.example.prototype1.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        setSupportActionBar(findViewById(R.id.toolbar_setting_activity))
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)

        // ユーザ設定 アカウント設定のアクティビティへ
        textView_users_setting.setOnClickListener {
            // すでにサインインしているか確認する
            val auth = FirebaseAuth.getInstance()
            if (auth.currentUser != null) {
                if (auth.currentUser!!.isAnonymous) {
                    // 匿名アカウントでのログイン　匿名アカウント用セッティングアクティビティへ
                    val intent = Intent(
                        this,
                        AnonymousAccountSettingActivity::class.java
                    )
                    startActivity(intent)
                } else {
                    // 通常アカウントでのログイン
                    val intent = Intent(
                        this,
                        AccountSettingActivity::class.java
                    )
                    startActivity(intent)
                }
            }
        }
        // お問い合わせfragmentの起動
        textView_contact.setOnClickListener{
            val newFragment = ContactFragment()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.setting_fragment_container, newFragment)
            transaction.addToBackStack(null)
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            transaction.commit()
        }

        // プライバシーポリシーfragmentの起動
        textView_privacy_prolicy.setOnClickListener {
            val newFragment = PrivacyPolicyFragment()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.setting_fragment_container, newFragment)
            transaction.addToBackStack(null)
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            transaction.commit()
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
