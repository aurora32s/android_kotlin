package com.haman.aop_part5_chapter06.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.haman.aop_part5_chapter06.R
import com.haman.aop_part5_chapter06.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() = with(binding) {
        val navigationController =
            (supportFragmentManager.findFragmentById(R.id.mainNavigationHostContainer) as NavHostFragment).navController
        // toolbar 의 각 label 자동 설정
        // 이 때, 일반 toolbar 가 아닌 androidx.toolbar 이어야 한다.
        toolbar.setupWithNavController(navigationController)
    }

}