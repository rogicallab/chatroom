package com.example.prototype1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import com.example.prototype1.ui.main.Table
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_board_create.*

class BoardCreateActivity : AppCompatActivity() {
    private lateinit var reference: DatabaseReference
    private val TAB_TITLES = arrayOf(
        "Top",
        "人間関係",
        "男女関係",
        "会社"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_create)
        // tool barの設置
        setSupportActionBar(findViewById(R.id.my_toolbar2))

         val currentCategory:String = intent.getStringExtra("currentCategory")
        val adapter:ArrayAdapter<String> = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            TAB_TITLES)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter=adapter
        for(i in 0..TAB_TITLES.size){
            if(currentCategory==TAB_TITLES[i]){
                spinner.setSelection(i)
                Log.d("broad",TAB_TITLES[i])
                break
            }
        }






        button3.setOnClickListener(View.OnClickListener {
            val title=titleEdit.text.toString()
            val category=spinner.selectedItem.toString()

            reference = FirebaseDatabase.getInstance().reference.child(category).push()

            val newBoard:Table=Table(category,title,reference.key.toString())
            reference.setValue(newBoard)
            finish()
        })
    }
}
