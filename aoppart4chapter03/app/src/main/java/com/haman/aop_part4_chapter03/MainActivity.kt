package com.haman.aop_part4_chapter03

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.haman.aop_part4_chapter03.databinding.ActivityMainBinding
import com.haman.aop_part4_chapter03.model.LocationLatLngEntity
import com.haman.aop_part4_chapter03.model.SearchResultEntity
import com.haman.aop_part4_chapter03.response.search.Poi
import com.haman.aop_part4_chapter03.response.search.Pois
import com.haman.aop_part4_chapter03.utility.RetrofitUtil
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: SearchRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        job = Job()
        initView()
        bindViews()
    }

    private fun initView() = with(binding) {
        emptyResultTextView.isVisible = false

        adapter = SearchRecyclerAdapter() { item ->
            val intent = Intent(this@MainActivity, MapActivity::class.java)
            startActivity(intent.apply {
                putExtra(SEARCH_RESULT_EXTRA_KEY, item)
            })
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
    }

    private fun bindViews() {
        binding.apply {
            searchButton.setOnClickListener{
                val keyword = keywordEditText.text.toString()
                if (keyword.isNotBlank()) {
                    searchKeyword(keyword)
                }
            }
        }
    }

    private fun searchKeyword(keyword: String) {
        launch(coroutineContext) {
            try {
                // IO thread 로 전환
                withContext(Dispatchers.IO) {
                    val searchResponse = RetrofitUtil.apiService.getSearchLocation(
                        keyword = keyword
                    )

                    if (searchResponse.isSuccessful) {
                        val body = searchResponse.body()
                        withContext(Dispatchers.Main) {
                            body?.let {
                                setData(body.searchPoiInfo.pois)
                            }
                        }
                    }
                }
            } catch (exception: Exception) {

            }
        }
    }

    private fun setData(pois: Pois) {
        val dataList = pois.poi.map {
            Log.d("MainActivity", it.toString())
            SearchResultEntity(
                name = it.name ?: "빌딩명 없음",
                fullAddress = makeMainAddress(it),
                locationLatLng = LocationLatLngEntity(
                    it.noorLat, it.noorLon
                )
            )
        }
        adapter.setSearchResultList(dataList)
    }

    private fun makeMainAddress(poi: Poi): String =
        if (poi.secondNo?.trim().isNullOrEmpty()) {
            (poi.upperAddrName?.trim() ?: "") + " " +
                    (poi.middleAddrName?.trim() ?: "") + " " +
                    (poi.lowerAddrName?.trim() ?: "") + " " +
                    (poi.detailAddrName?.trim() ?: "") + " " +
                    (poi.firstNo?.trim() ?: "") + " "
        } else {
            (poi.upperAddrName?.trim() ?: "") + " " +
                    (poi.middleAddrName?.trim() ?: "") + " " +
                    (poi.lowerAddrName?.trim() ?: "") + " " +
                    (poi.detailAddrName?.trim() ?: "") + " " +
                    (poi.firstNo?.trim() ?: "") + " " +
                    (poi.secondNo?.trim())
        }

    companion object {
        val SEARCH_RESULT_EXTRA_KEY = "SEARCH_RESULT_EXTRA_KEY"
    }
}