package com.haman.aop_part4_chapter03

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.haman.aop_part4_chapter03.databinding.ActivityMainBinding
import com.haman.aop_part4_chapter03.model.LocationLatLngEntity
import com.haman.aop_part4_chapter03.model.SearchResultEntity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: SearchRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() = with(binding) {
        emptyResultTextView.isVisible = false

        adapter = SearchRecyclerAdapter(
            testData
        ) { item ->
            Log.d("MainActivity", "${item.name} / ${item.fullAddress}")
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
    }

    companion object {
        private val testData: List<SearchResultEntity> by lazy {
            (0..10).map {
                SearchResultEntity(
                    name = "빌딩 $it", fullAddress = "주소 $it",
                    locationLatLng = LocationLatLngEntity(
                        it.toFloat(), it.toFloat()
                    )
                )
            }
        }
    }
}