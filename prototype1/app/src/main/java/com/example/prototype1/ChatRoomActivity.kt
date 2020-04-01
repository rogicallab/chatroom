package com.example.prototype1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ChatRoomActivity : AppCompatActivity() {

    val PAGE_NAME = "com.example.prototype1.NAME"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)

        // 押された板によってアクティビティのタイトルを更新する
        val message = intent.getStringExtra(PAGE_NAME)
        title =message
    }
}
