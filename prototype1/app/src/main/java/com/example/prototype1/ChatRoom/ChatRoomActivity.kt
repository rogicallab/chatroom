package com.example.prototype1.ChatRoom

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prototype1.R
import com.firebase.ui.database.FirebaseListOptions
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_chat_room.*


class ChatRoomActivity : AppCompatActivity() {
    private lateinit var mDatabase: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var user: FirebaseUser
    private lateinit var mFirebaseAdapter: FirebaseRecyclerAdapter<Message, MessageViewHolder>
    private lateinit var options: FirebaseListOptions<Message>
    var gson:Gson=Gson()

    val PAGE_NAME = "com.example.prototype1.NAME"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)

        // 押された板によってアクティビティのタイトルを更新する
        val boardName = intent.getStringExtra("title")
        title =boardName
        val tabName=intent.getStringExtra("tabName")
        val boardId=intent.getStringExtra("id")

        val user=FirebaseAuth.getInstance().currentUser!!
        val userId = user.uid as String
        val userName = if (FirebaseAuth.getInstance().currentUser?.displayName==""){
            "匿名さん"
        }else{
            FirebaseAuth.getInstance().currentUser?.displayName as String
        }


        reference = FirebaseDatabase.getInstance().reference.child("messages").child(boardId)
        reference.child("boardName").setValue(boardName)
        reference.child("userId").setValue(userId)


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
            val newMessage:Message = Message(userName,edittext.text.toString(),userId)
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
                    val ONE_MEGABYTE: Long = 1024 * 1024

                   val fref=FirebaseDatabase.getInstance().reference.child("user").child(model.id).child("fUrl")
                    fref.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(p0: DataSnapshot) {
                            val furl=p0.value.toString()
                            Log.d("fUrl",furl)
                            if( furl== null){
                                // 参の作成
                                val ref = FirebaseStorage.getInstance().reference.child("/images/${model.id}")
                                ref.getBytes(ONE_MEGABYTE).addOnSuccessListener { bytes ->
                                    // Data for "images/island.jpg" is returned, use this as needed
                                    //val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, bytes.)
                                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                                    holder.imageView.setImageBitmap(bitmap)
                                }.addOnFailureListener {
                                    // Handle any errors
                                }

                            }else{
                                // 参照の作成
                                val gsReference = FirebaseStorage.getInstance().getReferenceFromUrl(furl)
                                gsReference.getBytes(ONE_MEGABYTE).addOnSuccessListener { bytes ->
                                    // Data for "images/island.jpg" is returned, use this as needed
                                    //val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, bytes.)
                                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                                    holder.imageView.setImageBitmap(bitmap)
                                }.addOnFailureListener {
                                    // Handle any errors
                                }
                            }
                        }

                        override fun onCancelled(p0: DatabaseError) {

                        }
                    })
//                    val cId= FirebaseAuth.getInstance().currentUser?.uid
//                    if(cId==model.id){
//                        holder.imageView.visibility=View.INVISIBLE
//                    }
                    holder.imageView.setOnClickListener(View.OnClickListener {
                        //その人のプロフィールを表示する
                    })

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

        val imageView: ImageView =itemView.findViewById(R.id.userImg)
        val textView2: TextView =itemView.findViewById(R.id.text_view2)
    }

}
