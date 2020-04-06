package com.example.prototype1.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prototype1.ChatRoomActivity
import com.example.prototype1.CustomAdapter
import com.example.prototype1.R
import kotlinx.android.synthetic.main.fragment_main.*
import kotlin.properties.Delegates

/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment : Fragment() {

    private lateinit var pageViewModel: PageViewModel
    val PAGE_NAME = "com.example.prototype1.NAME"

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

        if(arguments?.getInt(ARG_SECTION_NUMBER,0)==1){
            /// 表示するテキスト配列を作る [テキスト0, テキスト1, ....]
            val list = Array<String>(10) {"板$it"}
            val adapter = CustomAdapter(list)
            val layoutManager = LinearLayoutManager(context)

            // アダプターとレイアウトマネージャーをセット
            (root.findViewById<RecyclerView>(R.id.simpleRecyclerView) as RecyclerView).layoutManager = layoutManager
            (root.findViewById<RecyclerView>(R.id.simpleRecyclerView) as RecyclerView).adapter = adapter
            (root.findViewById<RecyclerView>(R.id.simpleRecyclerView) as RecyclerView).setHasFixedSize(true)

            // インターフェースの実装
            adapter.setOnItemClickListener(object:CustomAdapter.OnItemClickListener{
                override fun onItemClickListener(view: View, position: Int, clickedText: String) {
                    // 新しいアクティビティの開始
                    var intent = Intent(context,ChatRoomActivity::class.java).apply {
                        putExtra(PAGE_NAME,clickedText)
                    }
                    startActivity(intent)
                }
            })
        }


        return root
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
}