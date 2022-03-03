package com.haman.aop_part5_chapter05

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.haman.aop_part5_chapter05.databinding.ActivityMainBinding
import com.haman.aop_part5_chapter05.extension.toGone
import com.haman.aop_part5_chapter05.extension.toVisible
import com.haman.aop_part5_chapter05.presentation.stationArrivals.StationsArrivalsFragmentArgs

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val navigationController by lazy {
        (supportFragmentManager.findFragmentById(R.id.mainNavigationHostContainer) as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
        bindViews()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navigationController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun initViews() {
        setSupportActionBar(binding.toolbar)
        // action bar 와 navigation controller 연결
        // ex. navigation back button create automatically
        setupActionBarWithNavController(navigationController)
    }

    private fun bindViews() {
        navigationController.addOnDestinationChangedListener { _, destination, argument /* Bundle */ ->
            if (destination.id == R.id.station_arrivals_dest) {
                title = StationsArrivalsFragmentArgs.fromBundle(argument!!).station.name
                binding.toolbar.toVisible()
            } else {
                binding.toolbar.toGone()
            }
        }
    }
}