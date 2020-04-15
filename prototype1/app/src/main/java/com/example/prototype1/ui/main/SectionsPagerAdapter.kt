package com.example.prototype1.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.prototype1.R
import com.google.firebase.database.*

private val TAB_TITLES = arrayOf(
        "Top",
    "人間関係",
    "男女関係",
    "会社"
)


/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager)
    : FragmentPagerAdapter(fm) {

//    private fun getList():ArrayList<String> {
//        val l=FirebaseDatabase.getInstance().reference.child("Category")
//        val list =ArrayList<String>()
//        val listener= object : ValueEventListener {
//            override fun onCancelled(p0: DatabaseError) {
//            }
//
//            override fun onDataChange(p0: DataSnapshot) {
//                if(p0!!.exists()){
//                    list.clear()
//                    for (e in p0.children){
//                        val name=e.key
//                        list.add(name!!)
//                    }
//                }
//            }
//        }
//        l.addListenerForSingleValueEvent(listener)
//        return list
//
//    }





    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return PlaceholderFragment.newInstance(position + 1)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return TAB_TITLES[position]
    }

    override fun getCount(): Int {
        // Show 2 total pages.
        return TAB_TITLES.size
    }
}