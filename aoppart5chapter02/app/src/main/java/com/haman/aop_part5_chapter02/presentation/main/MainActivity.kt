package com.haman.aop_part5_chapter02.presentation.main

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.haman.aop_part5_chapter02.R
import com.haman.aop_part5_chapter02.databinding.ActivityMainBinding
import com.haman.aop_part5_chapter02.presentation.BaseActivity
import com.haman.aop_part5_chapter02.presentation.BaseFragment
import com.haman.aop_part5_chapter02.presentation.list.ProductListFragment
import com.haman.aop_part5_chapter02.presentation.profile.ProfileFragment
import org.koin.android.ext.android.inject

internal class MainActivity
    : BaseActivity<MainViewModel, ActivityMainBinding>(), BottomNavigationView.OnNavigationItemSelectedListener
{

    override val viewModel: MainViewModel by inject()

    override fun getViewBinding(): ActivityMainBinding =
        ActivityMainBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
    }

    private fun initViews() = with(binding) {
        bottomNav.setOnNavigationItemSelectedListener(this@MainActivity)
        replaceFragment(ProductListFragment(), ProductListFragment.TAG)
    }

    override fun observeData() = viewModel.mainStateLiveData.observe(this) {
        when (it) {
            MainState.RefreshOrderList -> {
                binding.bottomNav.selectedItemId = R.id.menu_profile
                val fragment = supportFragmentManager.findFragmentByTag(ProductListFragment.TAG)
                (fragment as BaseFragment<*,*>)?.viewModel?.fetchData()
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.menu_home -> {
                replaceFragment(ProductListFragment(), ProductListFragment.TAG)
                true
            }
            R.id.menu_profile -> {
                replaceFragment(ProfileFragment(), ProfileFragment.TAG)
                true
            }
            else -> false
        }
    }

    private fun replaceFragment(fragment: Fragment, tag: String) {
        val findFragment = supportFragmentManager.findFragmentByTag(tag)
        supportFragmentManager.fragments.forEach { fm ->
            supportFragmentManager.beginTransaction().hide(fm).commit()
        }
        findFragment?.let {
            supportFragmentManager.beginTransaction().show(it).commit()
        } ?: kotlin.run {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, fragment, tag)
                .commitAllowingStateLoss()
        }
    }
}