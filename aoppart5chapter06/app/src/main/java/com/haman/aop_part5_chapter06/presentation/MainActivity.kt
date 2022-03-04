package com.haman.aop_part5_chapter06.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.haman.aop_part5_chapter06.R
import com.haman.aop_part5_chapter06.databinding.ActivityMainBinding
import com.haman.aop_part5_chapter06.work.TrackingCheckWorker
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
        initWorker()
    }

    private fun initViews() = with(binding) {
        val navigationController =
            (supportFragmentManager.findFragmentById(R.id.mainNavigationHostContainer) as NavHostFragment).navController
        // toolbar 의 각 label 자동 설정
        // 이 때, 일반 toolbar 가 아닌 androidx.toolbar 이어야 한다.
        toolbar.setupWithNavController(navigationController)
    }

    private fun initWorker() {
        val workerStartTime = Calendar.getInstance()
        workerStartTime.set(Calendar.HOUR_OF_DAY, 16)
        val initialDelay = workerStartTime.timeInMillis - System.currentTimeMillis()
        val dailyTrackingCheckRequest =
            PeriodicWorkRequestBuilder<TrackingCheckWorker>(1, TimeUnit.DAYS)
                .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR, // 정해진 시간에 딱딱 수행
                    PeriodicWorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.MILLISECONDS
                )
                .build()
        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                "DailyTrackingCheck",
                ExistingPeriodicWorkPolicy.KEEP,
                dailyTrackingCheckRequest
            )
    }

}