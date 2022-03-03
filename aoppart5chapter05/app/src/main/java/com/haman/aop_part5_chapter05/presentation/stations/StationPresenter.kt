package com.haman.aop_part5_chapter05.presentation.stations

import com.haman.aop_part5_chapter05.data.repository.StationRepository
import com.haman.aop_part5_chapter05.domain.Station
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class StationPresenter(
    private val view: StationContract.View,
    private val stationRepository: StationRepository
): StationContract.Presenter {

    override val scope: CoroutineScope = MainScope()

    // 최신 검색 정보를 저장
    private val queryString: MutableStateFlow<String> = MutableStateFlow("")
    // 다시 화면에 돌아왔을 때, 기존의 station 정보를 바로 화면에 보여주기 위해서 저장
    private val stations: MutableStateFlow<List<Station>> = MutableStateFlow(emptyList())

    init {
        observeStations()
    }

    override fun onViewCreated() {
        scope.launch {
            view.showStations(stations.value)
            stationRepository.refreshStations()
        }
    }

    override fun onDestroyView() {}

    override fun filterStations(query: String) {
        scope.launch {
            queryString.emit(query)
        }
    }

    override fun toggleStationFavorite(station: Station) {
        scope.launch {
            stationRepository.updateStation(station.copy(isFavorite = !station.isFavorite))
        }
    }

    private fun observeStations() {
        stationRepository
            .stations
            .combine(queryString) { stations, query -> // observe 에 queryString 도 추가
                if (query.isBlank()) {
                    stations
                } else {
                    stations.filter { it.name.contains(query) }
                }
            }
            .onStart { view.showLoadingIndicator() } // stations flow start
            .onEach { // data 가 update 될 때마다 수행
                if (it.isNotEmpty()) {
                    view.hideLoadingIndicator()
                }
                stations.value = it
                view.showStations(it)
            }
            .catch {
                it.printStackTrace()
                view.hideLoadingIndicator()
            }
            .launchIn(scope) // scope setting
    }
}