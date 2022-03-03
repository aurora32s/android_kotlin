package com.haman.aop_part5_chapter06.presentation.trackingitems

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.haman.aop_part5_chapter06.R
import com.haman.aop_part5_chapter06.data.entity.TrackingInformation
import com.haman.aop_part5_chapter06.data.entity.TrackingItem
import com.haman.aop_part5_chapter06.databinding.FragmentTrackingItemsBinding
import com.haman.aop_part5_chapter06.extension.toGone
import com.haman.aop_part5_chapter06.extension.toInvisible
import com.haman.aop_part5_chapter06.extension.toVisible
import org.koin.android.scope.ScopeFragment

class TrackingItemsFragment : ScopeFragment(), TrackingItemsContract.View {

    override val presenter: TrackingItemsContract.Presenter by inject()
    private var binding: FragmentTrackingItemsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentTrackingItemsBinding.inflate(inflater)
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

    private fun initViews() {
        binding?.recyclerView?.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = TrackingItemsAdapter()
        }
    }

    private fun bindViews() {
        // refresh swipe layout
        binding?.refreshLayout?.setOnRefreshListener {
            presenter.refresh()
        }
        // 등록된 송장 번호가 없을 때, 송장 추가 버튼
        binding?.addTrackingItemButton?.setOnClickListener {
            // action name -> 등록 화면으로 이동
            findNavController().navigate(R.id.to_add_tracking_item)
        }
        // floating button
        binding?.addTrackingItemFloatingActionButton?.setOnClickListener { _ ->
            // action name -> 등록 화면으로 이동
            findNavController().navigate(R.id.to_add_tracking_item)
        }
    }

    override fun showLoadingIndicator() {
        binding?.progressBar?.toVisible()
    }

    override fun hideLoadingIndicator() {
        binding?.progressBar?.toGone()
        binding?.refreshLayout?.isRefreshing = false
    }

    override fun showNoDataDescription() {
        binding?.refreshLayout?.toInvisible()
        binding?.noDataContainer?.toVisible()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun showTrackingItemInformation(trackingItemInformation: List<Pair<TrackingItem, TrackingInformation>>) {
        with(binding!!) {
            binding?.refreshLayout?.toVisible()
            binding?.noDataContainer?.toGone()
            (binding?.recyclerView?.adapter as? TrackingItemsAdapter)?.apply {
                this.data = trackingItemInformation
                notifyDataSetChanged()
            }
        }
    }
}