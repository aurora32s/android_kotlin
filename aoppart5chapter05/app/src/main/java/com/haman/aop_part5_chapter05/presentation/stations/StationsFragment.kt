package com.haman.aop_part5_chapter05.presentation.stations

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethod
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.haman.aop_part5_chapter05.databinding.FragmentStationsBinding
import com.haman.aop_part5_chapter05.domain.Station
import com.haman.aop_part5_chapter05.extension.toGone
import com.haman.aop_part5_chapter05.extension.toVisible
import org.koin.android.scope.ScopeFragment

class StationsFragment: ScopeFragment(), StationContract.View {

    override val presenter: StationContract.Presenter by inject()
    private var binding: FragmentStationsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentStationsBinding.inflate(inflater, container, false)
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
        hideKeyboard()
    }

    override fun showLoadingIndicator() {
        binding?.progressBar?.toVisible()
    }

    override fun hideLoadingIndicator() {
        binding?.progressBar?.toGone()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun showStations(stations: List<Station>) {
        (binding?.recyclerView?.adapter as? StationAdapter)?.run {
            this.data = stations
            notifyDataSetChanged()
        }
    }
    private fun initViews() {
        binding?.recyclerView?.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = StationAdapter()
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
        }
    }

    private fun bindViews() {
        binding?.searchEditText?.addTextChangedListener { editable ->
            presenter.filterStations(editable.toString())
        }

        (binding?.recyclerView?.adapter as? StationAdapter)?.apply {
            onItemClickListener = { station ->
                // action 에 지정한 id 이름
                val action = StationsFragmentDirections.toStationArrivalsDest(station)
                findNavController().navigate(action)
            }
            onFavoriteClickListener = { station ->
                presenter.toggleStationFavorite(station)
            }
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
    }
}