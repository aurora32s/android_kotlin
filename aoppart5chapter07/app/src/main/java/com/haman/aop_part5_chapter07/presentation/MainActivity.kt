package com.haman.aop_part5_chapter07.presentation

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.haman.aop_part5_chapter07.R
import com.haman.aop_part5_chapter07.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val navigationController by lazy {
        (supportFragmentManager.findFragmentById(R.id.mainNavigationHostContainer) as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        // tab 간 이동 시 stack 에 관리하지 않고, toolbar 에 back button 이 생기지 않는다.
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.home_dest, R.id.my_page_dest))
        // navigation 과 연동
        binding.bottomNavigationView.setupWithNavController(navigationController)
        binding.toolbar.setupWithNavController(navigationController, appBarConfiguration)
    }

}