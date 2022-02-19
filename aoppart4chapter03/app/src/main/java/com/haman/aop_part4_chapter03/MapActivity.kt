package com.haman.aop_part4_chapter03

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.haman.aop_part4_chapter03.databinding.ActivityMapBinding
import com.haman.aop_part4_chapter03.model.LocationLatLngEntity
import com.haman.aop_part4_chapter03.model.SearchResultEntity
import com.haman.aop_part4_chapter03.utility.RetrofitUtil
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class MapActivity : AppCompatActivity(), OnMapReadyCallback, CoroutineScope {

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var binding: ActivityMapBinding
    private lateinit var searchResult: SearchResultEntity
    private lateinit var map: GoogleMap
    private lateinit var currentSelectMarker: Marker
    private lateinit var locationManager: LocationManager
    private lateinit var mylocationListener: MyLocationListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        job = Job()
        if (::searchResult.isInitialized.not()) {
            intent?.let {
                searchResult = it.getParcelableExtra<SearchResultEntity>(SEARCH_RESULT_EXTRA_KEY)
                    ?: throw Exception("데이터가 존재하지 않습니다.")
            }
        }
        setUpGoogleMap()
        bindViews()
    }

    private fun bindViews() {
        binding.currentLocationButton.setOnClickListener {
            getMyLocation()
        }
    }

    private fun setUpGoogleMap() {
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        currentSelectMarker = setUpMarker(searchResult) ?: return
        currentSelectMarker.showInfoWindow()
    }

    private fun setUpMarker(searchResult: SearchResultEntity): Marker? {
        val positionLatLng = LatLng(
            searchResult.locationLatLng.latitude.toDouble(),
            searchResult.locationLatLng.longitude.toDouble()
        )
        val markerOption = MarkerOptions().apply {
            position(positionLatLng)
            title(searchResult.name)
            snippet(searchResult.fullAddress)
        }
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(positionLatLng, CAMERA_ZOOM_LEVEL))
        return map.addMarker(markerOption)
    }

    private fun getMyLocation() {
        // 위치 데이터 접근을 위한 권한 요청
        if (::locationManager.isInitialized.not()) {
            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }
        // gps 사용 가능 여부
        val isGpsEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (isGpsEnable) {
            // 사용이 가능한 경우, 권한 요청
            when {
                (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED) -> {
                    setMyLocationListener()
                }
                shouldShowRequestPermissionRationale(
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) || shouldShowRequestPermissionRationale(
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) -> {
                    showPermissionDialog()
                }
                else -> {
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ),
                        REQUEST_LOCATION_PERMISSION
                    )
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun setMyLocationListener() {
        val minTime = 1500L
        val minDistance = 100f

        if(::mylocationListener.isInitialized.not()) {
            mylocationListener = MyLocationListener()
        }
        with(locationManager) {
            requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                minTime,
                minDistance,
                mylocationListener
            )

        }
    }

    private fun onCurrentLocationChanged(location: LocationLatLngEntity) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
            LatLng(
                location.latitude.toDouble(),
                location.longitude.toDouble()
            ), CAMERA_ZOOM_LEVEL))
        removeMyLocationListener()
        loadReverseGeoInformation(location)
    }

    private fun loadReverseGeoInformation(location: LocationLatLngEntity) {
        launch(coroutineContext) {
            try {
                withContext(Dispatchers.IO) {
                    val response = RetrofitUtil.apiService.getReverseGeoCode(
                        lat = location.latitude.toDouble(),
                        lon = location.longitude.toDouble()
                    )
                    if (response.isSuccessful) {
                        val body = response.body()
                        withContext(Dispatchers.Main) {
                            body?.let {
                                setUpMarker(
                                    SearchResultEntity(
                                        name="Me",
                                        fullAddress = it.addressInfo.fullAddress ?: "주소 없음,",
                                        locationLatLng = location
                                    )
                                )
                            }
                        }
                    }
                }
            } catch(exception: Exception) {
                exception.printStackTrace()
                Toast.makeText(this@MapActivity, "검색하는 과정에서 에러가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun removeMyLocationListener() {
        if(::locationManager.isInitialized && ::mylocationListener.isInitialized) {
            locationManager.removeUpdates(mylocationListener)
        }
    }

    inner class MyLocationListener: LocationListener {
        override fun onLocationChanged(location: Location) {
            val locationLatLngEntity = LocationLatLngEntity(
                location.latitude.toFloat(),
                location.longitude.toFloat()
            )
            onCurrentLocationChanged(locationLatLngEntity)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        val locationPermissionGranted =
            requestCode == REQUEST_LOCATION_PERMISSION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED

        if (locationPermissionGranted) {
            setMyLocationListener()
        }
    }

    private fun showPermissionDialog() {
        AlertDialog.Builder(this)
            .setTitle("권한요청")
            .setMessage("사용자의 위치를 표현해주기 위해 위치 정보 접근 권한이 필요합니다.")
            .setPositiveButton("예"){ _,_ ->
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ),
                    REQUEST_LOCATION_PERMISSION
                )
            }.setNegativeButton("아니요"){dialog, _ -> dialog.dismiss()}
            .create().show()
    }

    companion object {
        val SEARCH_RESULT_EXTRA_KEY = "SEARCH_RESULT_EXTRA_KEY"
        // camera zoom ratio
        val CAMERA_ZOOM_LEVEL = 17f
        // permission request
        val REQUEST_LOCATION_PERMISSION = 1001
    }
}