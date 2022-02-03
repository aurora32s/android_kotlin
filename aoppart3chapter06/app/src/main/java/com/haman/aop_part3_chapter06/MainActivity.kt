package com.haman.aop_part3_chapter06

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.haman.aop_part3_chapter06.chatlist.ChatListFragment
import com.haman.aop_part3_chapter06.home.HomeFragment
import com.haman.aop_part3_chapter06.mypage.MyPageFragment

/**
 * Root package
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val homeFragment = HomeFragment()
        val chatListFragment = ChatListFragment()
        val myPageFragment = MyPageFragment()

        val bottomNavigationView : BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> replaceFragment(homeFragment)
                R.id.chatList -> replaceFragment(chatListFragment)
                R.id.myPage -> replaceFragment(myPageFragment)
            }
            true
        }

        // 초기에는 home fragment
        replaceFragment(homeFragment)
    }

    // androidx 의 fragment 를 import
    private fun replaceFragment (fragment : Fragment) {
        // activity 에서 attach 되어 있는 fragment 관리
        supportFragmentManager.beginTransaction()
            .apply {
                replace(R.id.fragmentContainer, fragment)
                commit()
            }
    }
}