package com.example.prototype1.Setting

import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.prototype1.R
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        setSupportActionBar(findViewById(R.id.toolbar_setting_activity))
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)

        textView_supprot.setBackgroundColor(Color.rgb(127, 127, 0))
        textView_contact.setBackgroundColor(Color.rgb(127, 127, 0))
        textView_privacy_prolicy.setBackgroundColor(Color.rgb(127, 127, 0))
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
