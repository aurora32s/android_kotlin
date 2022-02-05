package com.haman.aop_part3_chapter08

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.haman.aop_part3_chapter08.adapter.VideoAdapter
import com.haman.aop_part3_chapter08.dto.VideoDto
import com.haman.aop_part3_chapter08.service.VideoService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var videoAdapter : VideoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // frameLayout 에 fragment attach
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, PlayerFragment())
            .commit()

        getVideoList()

        videoAdapter = VideoAdapter(callbackValue = { source, title ->
            supportFragmentManager.fragments.find { it is PlayerFragment }
                ?.let {
                    (it as PlayerFragment).play(source, title)
                }
        })
        findViewById<RecyclerView>(R.id.mainRecyclerView).apply {
            adapter = videoAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun getVideoList () {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://run.mocky.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(VideoService::class.java).also {
            it.getVideos().enqueue(object : Callback<VideoDto> {
                override fun onResponse(call: Call<VideoDto>, response: Response<VideoDto>) {
                    if (response.isSuccessful.not()) {
                        Log.d("MainActivity", "response fail")
                        return
                    }

                    response.body()?.let { dto ->
                        Log.d("MainActivity", dto.toString())
                        videoAdapter.submitList(dto.items)
                    }
                }
                override fun onFailure(call: Call<VideoDto>, t: Throwable) {
                    // 예외 처리
                    Log.d("MainActivity", t.toString())
                }
            })
        }
    }
}