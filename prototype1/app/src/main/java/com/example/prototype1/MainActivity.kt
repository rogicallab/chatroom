package com.example.prototype1

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.prototype1.ui.main.SectionsPagerAdapter
import com.example.prototype1.ui.main.Table
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

//     var selectedTabName : String? =null

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
//            val reference :DatabaseReference= FirebaseDatabase.getInstance().reference.child("Top")
//            val newTitle: Table = Table("しののん","しののんフザケンナ")
//            reference.push().setValue(newTitle)
        }

        // tool barの設置
        setSupportActionBar(findViewById(R.id.my_toolbar))

        // すでにサインインしているかを確認し、サインインしていなければ匿名アカウントを作成・ログインする
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) { // already signed in(匿名も含める）
        } else { // not signed in
            // 匿名アカウントとしてログイン
            auth.signInAnonymously()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                    } else {
                        // If sign in fails, display a message to the user.
                        // 本来ならここでチャットに入れない等のことを行いたいが。。。（ここに来たということはログインしていない状態が続くということ）
                        println("signInAnonymously:failure")
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                        //updateUI(null)
                    }
                }
        }
        // tabの名前をとってくる
//        selectedTabName= tabs.getTabAt(0)?.getText() as String
//        println("selectedItemText$selectedTabName")
//        tabs.addOnTabSelectedListener(object : OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab) {
//                selectedTabName = tab.text as String?
//                println("selectedTabName$selectedTabName")
//            }
//            override fun onTabUnselected(tab: TabLayout.Tab) {}
//            override fun onTabReselected(tab: TabLayout.Tab) {}
//        })

    }
    // tool barにmenuをセット
    override fun onCreateOptionsMenu(menu : Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.tab_menu, menu)
        return true
    }


    override fun onResume() {
        super.onResume()
        // すでにサインインしているかを確認し、サインインしていなければ匿名アカウントを作成・ログインする
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) { // already signed in(匿名も含める）
        } else { // not signed in
            // 匿名アカウントとしてログイン
            auth.signInAnonymously()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                    } else {
                        // If sign in fails, display a message to the user.
                        // 本来ならここでチャットに入れない等のことを行いたいが。。。（ここに来たということはログインしていない状態が続くということ）
                        println("signInAnonymously:failure")
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                        //updateUI(null)
                    }

                }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // アカウントのログイン状態（匿名/非匿名）に応じてアクティビティを開始する
        if(item.itemId == R.id.user_profile){
            // すでにサインインしているか確認する
            val auth = FirebaseAuth.getInstance()
            val current = auth.currentUser
            if (auth.currentUser != null){
                if(auth.currentUser!!.isAnonymous){
                    // 匿名アカウントでのログイン　匿名アカウント用セッティングアクティビティへ
                    val intent = Intent(this,AnonymousAccountSettingActivity::class.java)
                    startActivity(intent)
                }else{
                    // 通常アカウントでのログイン
                    val intent = Intent(this,AccountSettingActivity::class.java)
                    startActivity(intent)
                }
            }
        }else{
            super.onOptionsItemSelected(item)
        }
        return false
    }



}