package com.haman.aop_part6_chapter01.screen.main

import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.haman.aop_part6_chapter01.R
import com.haman.aop_part6_chapter01.databinding.ActivityMainBinding
import com.haman.aop_part6_chapter01.screen.main.home.HomeFragment
import com.haman.aop_part6_chapter01.screen.main.like.RestaurantLikedListFragment
import com.haman.aop_part6_chapter01.screen.main.mypage.MyPageFragment
import com.haman.aop_part6_chapter01.util.event.MenuChangeEventBus
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private val menuChangeEventBus by inject<MenuChangeEventBus>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeData()
        initViews()
    }

    private fun observeData() {
        lifecycleScope.launch {
            menuChangeEventBus.mainTabMenuFlow.collect {
                goToTab(it)
            }
        }
    }

    private fun initViews() = with(binding) {
        bottomNav.setOnItemSelectedListener(this@MainActivity)
        showFragment(HomeFragment.newInstance(), HomeFragment.TAG)
    }

    // navigation 변경 event
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_home -> {
                showFragment(HomeFragment.newInstance(), HomeFragment.TAG)
                true
            }
            R.id.menu_liked -> {
                showFragment(
                    RestaurantLikedListFragment.newInstance(),
                    RestaurantLikedListFragment.TAG
                )
                true
            }
            R.id.menu_my -> {
                showFragment(MyPageFragment.newInstance(), MyPageFragment.TAG)
                true
            }
            else -> false
        }
    }

    fun goToTab(mainTabMenu: MainTabMenu) {
        binding.bottomNav.selectedItemId = mainTabMenu.menuId
    }

    private fun showFragment(fragment: Fragment, tag: String) {
        val findFragment = supportFragmentManager.findFragmentByTag(tag)
        // fragment 가 교체가 될 때 다른 fragment 들은 hide
        supportFragmentManager.fragments.forEach { fm ->
            supportFragmentManager.beginTransaction().hide(fm).commitAllowingStateLoss()
        }

        findFragment?.let {
            supportFragmentManager.beginTransaction()
                .show(it)
                .commitAllowingStateLoss()
            if (findFragment is RestaurantLikedListFragment) {
                findFragment.viewModel.fetchData()
            }
        } ?: kotlin.run {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, fragment, tag)
                .commitAllowingStateLoss()
        }
    }
}

enum class MainTabMenu(@IdRes val menuId: Int) {
    HOME(R.id.menu_home),
    LIKE(R.id.menu_liked),
    MY(R.id.menu_my)
}