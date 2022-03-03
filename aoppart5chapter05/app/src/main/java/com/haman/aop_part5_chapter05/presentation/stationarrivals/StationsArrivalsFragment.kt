package com.haman.aop_part5_chapter05.presentation.stationArrivals

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.haman.aop_part5_chapter05.R
import com.haman.aop_part5_chapter05.databinding.FragmentStationArrivalsBinding
import com.haman.aop_part5_chapter05.domain.ArrivalInformation
import com.haman.aop_part5_chapter05.extension.toGone
import com.haman.aop_part5_chapter05.extension.toVisible
import org.koin.android.scope.ScopeFragment
import org.koin.core.parameter.parametersOf

class StationsArrivalsFragment: ScopeFragment(), StationArrivalsContract.View {

    private var binding: FragmentStationArrivalsBinding? = null
    private val arguments: StationsArrivalsFragmentArgs by navArgs()

    override val presenter: StationArrivalsContract.Presenter by inject { parametersOf(arguments.station) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // option menu 사용 활성화
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentStationArrivalsBinding.inflate(inflater, container, false)
        .also { binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        bindViews()
        presenter.onViewCreated()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    // action bar menu setting
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_station_arrivals, menu)
        menu.findItem(R.id.favoriteAction).apply {
            setIcon(
                if (arguments.station.isFavorite) {
                    R.drawable.ic_launcher_background
                } else {
                    R.drawable.ic_launcher_foreground
                }
            )
            isChecked = arguments.station.isFavorite
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when(item.itemId) {
            R.id.refreshAction -> { // 새로고침
                presenter.fetchStationArrivals()
                true
            }
            R.id.favoriteAction -> { // 즐겨찾기 추가
                item.isChecked = !item.isChecked
                item.setIcon(
                    if (item.isChecked) {
                        R.drawable.ic_launcher_background
                    } else {
                        R.drawable.ic_launcher_foreground
                    }
                )
                presenter.toggleStationFavorite(item)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    override fun showLoadingIndicator() {
        binding?.progressBar?.toVisible()
    }

    override fun hideLoadingIndecator() {
        binding?.progressBar?.toGone()
    }

    override fun showErrorDescription(message: String) {
        binding?.recyclerView?.toGone()
        binding?.errorDescriptionTextView?.toVisible()
        binding?.errorDescriptionTextView?.text = message
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun showStationArrivals(arrivalInformation: List<ArrivalInformation>) {
        binding?.errorDescriptionTextView?.toGone()
        (binding?.recyclerView?.adapter as? StationArrivalsAdapter)?.run {
            this.data = arrivalInformation
            notifyDataSetChanged()
        }
    }

    private fun initViews() {
        binding?.recyclerView?.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = StationArrivalsAdapter()
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
        }
    }

    private fun bindViews() {}
}