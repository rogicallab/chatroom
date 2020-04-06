package com.example.prototype1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.prototype1.ui.main.SectionsPagerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        val fab: FloatingActionButton = findViewById(R.id.fab)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        // tool barの設置
        setSupportActionBar(findViewById(R.id.my_toolbar))

    }
    // tool barにmenuをセット
    override fun onCreateOptionsMenu(menu : Menu): Boolean {
        println("Menu:$menu")
        val inflater = menuInflater
        inflater.inflate(R.menu.tab_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.user_profile){
            // すでにサインインしているか確認する
            val auth = FirebaseAuth.getInstance()
            println(auth.currentUser)
            if (auth.currentUser != null) { // already signed in
                // アカウント管理Activity起動
                println("サインインしてる")
                val intent = Intent(this,AccountSettingActivity::class.java)
                startActivity(intent)
            } else { // not signed in
                println("サインインしていない")
                // 匿名アカウントとしてログイン
                val auth = FirebaseAuth.getInstance()
                auth.signInAnonymously()
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            println("signInAnonymously:success")
                            val user = auth.currentUser
                            val intent = Intent(this,AnonymousAccountSettingActivity::class.java)
                            startActivity(intent)
                            //updateUI(user)
                        } else {
                            // If sign in fails, display a message to the user.
                            println("signInAnonymously:failure")
                            Toast.makeText(baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                            //updateUI(null)
                        }

                    }
            }
        }else{
            super.onOptionsItemSelected(item)
        }
        return false
    }

//    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
//
//        if(menu.itemId == R.id.user_profile){
//            // すでにサインインしているか確認する
//            val auth = FirebaseAuth.getInstance()
//            if (auth.currentUser != null) { // already signed in
//                // アカウント管理Activity起動
//                println("サインインしてる")
//                val intent = Intent(this,AccountSettingActivity::class.java)
//                startActivity(intent)
//            } else { // not signed in
//                println("サインインしていない")
//                // 匿名アカウントとしてログイン
//                val auth = FirebaseAuth.getInstance()
//                auth.signInAnonymously()
//                    .addOnCompleteListener(this) { task ->
//                        if (task.isSuccessful) {
//                            // Sign in success, update UI with the signed-in user's information
//                            println("signInAnonymously:success")
//                            val user = auth.currentUser
//                            val intent = Intent(this,AnonymousAccountSettingActivity::class.java)
//                            startActivity(intent)
//                            //updateUI(user)
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            println("signInAnonymously:failure")
//                            Toast.makeText(baseContext, "Authentication failed.",
//                                Toast.LENGTH_SHORT).show()
//                            //updateUI(null)
//                        }
//
//                    }
//            }
//        }else{
//            return super.onPrepareOptionsMenu(menu)
//        }
//
//    }


}