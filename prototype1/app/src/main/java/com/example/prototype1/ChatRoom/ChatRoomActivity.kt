package com.example.prototype1.ChatRoom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prototype1.R
import com.firebase.ui.database.FirebaseListOptions
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_chat_room.*

class ChatRoomActivity : AppCompatActivity() {
    private lateinit var mDatabase: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var user: FirebaseUser
    private lateinit var mFirebaseAdapter: FirebaseRecyclerAdapter<Message, MessageViewHolder>
    private lateinit var options: FirebaseListOptions<Message>

    val PAGE_NAME = "com.example.prototype1.NAME"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)

        // 押された板によってアクティビティのタイトルを更新する
        val boardName = intent.getStringExtra("title")
        title =boardName
        val tabName=intent.getStringExtra("tabName")
        val boardId=intent.getStringExtra("id")

        val userId = FirebaseAuth.getInstance().currentUser?.uid as String
        val user = if (FirebaseAuth.getInstance().currentUser?.displayName==""){
            "匿名さん"
        }else{
            FirebaseAuth.getInstance().currentUser?.displayName as String
        }


        reference = FirebaseDatabase.getInstance().reference.child("messages").child(boardId)
        reference.child("boardName").setValue(boardName)
        reference.child("id").setValue(boardId)


        recycleView.setHasFixedSize(true)
        recycleView.layoutManager= LinearLayoutManager(this)

//        val dataset:ArrayList<String> = arrayListOf()
//        dataset.add("moemoe")
//        dataset.add("moep")
//        recycleView.adapter=MyAdapter(dataset)

//        val mAdapter:FirebaseRecyclerAdapter<Message,RecyclerView.ViewHolder>
//        override fun populateView(v: View, model: Message, position: Int) {
////            ((TextView) v).setText(model.author+": "+model.content);
//        }
        button2.setOnClickListener(View.OnClickListener {
            val newMessage:Message = Message(user,edittext.text.toString(),userId)
            reference.child("message").push().setValue(newMessage)
            edittext.setText("")
        })
    }
    override fun onStart() {
        super.onStart()


//        val parser = object : SnapshotParser<Message> {
//            override fun parseSnapshot(snapshot: DataSnapshot): Message {
//                val message: Message = snapshot.getValue() as Message
//                if (message != null) {
//                }
//                return message
//            }
//        }

        val options: FirebaseRecyclerOptions<Message> = FirebaseRecyclerOptions.Builder<Message>()
            .setQuery(reference.child("message"), Message::class.java)
            .build()

        mFirebaseAdapter =
            object : FirebaseRecyclerAdapter<Message, MessageViewHolder>(options) {
                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): MessageViewHolder {
                    val view:View=
                        LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false)
                    return MessageViewHolder(view)
                }

                override fun onBindViewHolder(
                    holder: MessageViewHolder,
                    position: Int,
                    model: Message
                ) {
                    holder.textView.text = model.userName
                    holder.textView2.text = model.message
                    Log.d("id",model.id)

                }
            }



//一番下にフォーカス
        mFirebaseAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                Log.d("adapter",mFirebaseAdapter.itemCount.toString())
                recycleView.smoothScrollToPosition(mFirebaseAdapter.itemCount)
            }
        })

        recycleView.adapter=mFirebaseAdapter
        mFirebaseAdapter.startListening()

    }

    override fun onStop() {
        super.onStop()
        mFirebaseAdapter.stopListening()
    }

    public class MessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val textView: TextView =itemView.findViewById(R.id.text_view)
        val textView2: TextView =itemView.findViewById(R.id.text_view2)
    }

}
