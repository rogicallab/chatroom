package com.example.prototype1.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


import com.example.prototype1.ChatRoom.ChatRoomActivity
import com.example.prototype1.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*

import com.example.prototype1.CustomAdapter
import com.example.prototype1.MainActivity
import kotlinx.android.synthetic.main.fragment_main.*
import kotlin.properties.Delegates

/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment : Fragment() {


    private lateinit var pageViewModel: PageViewModel
    val PAGE_NAME = "com.example.prototype1.NAME"
    private lateinit var reference: DatabaseReference
    private lateinit var user: FirebaseUser
    private lateinit var mFirebaseAdapter: FirebaseRecyclerAdapter<Table, TableViewHolder>
    private lateinit var tabName:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel::class.java).apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_main, container, false)
        val textView: TextView = root.findViewById(R.id.section_label)
        pageViewModel.text.observe(this, Observer<String> {
            textView.text = it
        })
        // 以下現在表示されているタブを取ってくるコード



            /// 表示するテキスト配列を作る [テキスト0, テキスト1, ....]
            val layoutManager = LinearLayoutManager(context)


            val recyclerView:RecyclerView =(root.findViewById<RecyclerView>(R.id.simpleRecyclerView) as RecyclerView)

            tabName=when(arguments?.getInt(ARG_SECTION_NUMBER,0)){
                1->"Top"
                2->"人間関係"
                3->"男女関係"
                4->"会社"
                else -> "Top"
            }
        Log.d("tab",tabName)

            reference = FirebaseDatabase.getInstance().reference.child(tabName)
//            val newTitle:Table= Table("Top","しののんフザケンナ")
//            reference.push().setValue(newTitle)
//
//            val newsTitle:Table= Table("Top","しののんブサイク")
//            reference.push().setValue(newsTitle)

            val options: FirebaseRecyclerOptions<Table> = FirebaseRecyclerOptions.Builder<Table>()
                .setQuery(reference, Table::class.java)
                .build()
//
            mFirebaseAdapter =
                object : FirebaseRecyclerAdapter<Table, TableViewHolder>(options) {
                    override fun onCreateViewHolder(
                        parent: ViewGroup,
                        viewType: Int
                    ): TableViewHolder {
                        val view:View=LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item,parent,false)
                        return TableViewHolder(view)
                    }

                    override fun onBindViewHolder(
                        holder: TableViewHolder,
                        position: Int,
                        model: Table
                    ) {
//                        holder.imageView.sampleImg.setImageResource(R.mipmap.ic_launcher_round)
                        holder.textView.text = model.title
                        holder.itemView.setOnClickListener(View.OnClickListener {
                            val i = Intent(activity, ChatRoomActivity::class.java)
                                i.putExtra("tabName",tabName)
                            i.putExtra("title",mFirebaseAdapter.getItem(position).title)
                            i.putExtra("id",model.id)
                            startActivity(i);
                        })
                    }
                }


            // アダプターとレイアウトマネージャーをセット
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = mFirebaseAdapter
            recyclerView.setHasFixedSize(true)

            mFirebaseAdapter.startListening()

            // インターフェースの実装
//            adapter.setOnItemClickListener(object:CustomAdapter.OnItemClickListener{
//                override fun onItemClickListener(view: View, position: Int, clickedText: String) {
//                    // 新しいアクティビティの開始
//                    var intent = Intent(context,ChatRoomActivity::class.java).apply {
//                        putExtra(PAGE_NAME,clickedText)
//                    }
//                    startActivity(intent)
//                }
//            })


        return root
    }

    override fun onStart() {
        super.onStart()
//        reference = FirebaseDatabase.getInstance().reference.child("Top")
//
//        val options: FirebaseRecyclerOptions<Table> = FirebaseRecyclerOptions.Builder<Table>()
//            .setQuery(reference, Table::class.java)
//            .build()
//
//        mFirebaseAdapter =
//            object : FirebaseRecyclerAdapter<Table, TableViewHolder>(options) {
//                override fun onCreateViewHolder(
//                    parent: ViewGroup,
//                    viewType: Int
//                ): TableViewHolder {
//                    val view:View=LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item,parent,false)
//                    return TableViewHolder(view)
//                }
//
//                override fun onBindViewHolder(
//                    holder: TableViewHolder,
//                    position: Int,
//                    model: Table
//                ) {
//                    holder.imageView.sampleImg.setImageResource(R.mipmap.ic_launcher_round)
//                    holder.textView.text = model.title
//                }
//            }
//        simpleRecyclerView.adapter=mFirebaseAdapter
//        mFirebaseAdapter.startListening()

    }
    override fun onStop() {
        super.onStop()
//        mFirebaseAdapter.stopListening()
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): PlaceholderFragment {
            return PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }
    public class TableViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val imageView:ImageView  =itemView.findViewById(R.id.sampleImg)
        val textView:TextView=itemView.findViewById(R.id.sampleTxt)
    }
}