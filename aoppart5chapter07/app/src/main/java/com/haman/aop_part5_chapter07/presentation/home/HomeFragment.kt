package com.haman.aop_part5_chapter07.presentation.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.haman.aop_part5_chapter07.databinding.FragmentHomeBinding
import com.haman.aop_part5_chapter07.domain.model.FeaturedMovie
import com.haman.aop_part5_chapter07.domain.model.Movie
import com.haman.aop_part5_chapter07.extension.dip
import com.haman.aop_part5_chapter07.extension.toGone
import com.haman.aop_part5_chapter07.extension.toVisible
import com.haman.aop_part5_chapter07.presentation.home.HomeAdapter.Companion.ITEM_VIEW_TYPE_FEATURED
import com.haman.aop_part5_chapter07.presentation.home.HomeAdapter.Companion.ITEM_VIEW_TYPE_SECTION_HEADER
import org.koin.android.scope.ScopeFragment

class HomeFragment : ScopeFragment(), HomeContract.View {

    override val presenter: HomeContract.Presenter by inject()
    private var binding: FragmentHomeBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentHomeBinding.inflate(layoutInflater)
        .also { binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        presenter.onViewCreated()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun showLoadingIndicator() {
        binding?.progressBar?.toVisible()
    }

    override fun hideLoadingIndicator() {
        binding?.progressBar?.toGone()
    }

    override fun showErrorDescription(message: String) {
        binding?.recyclerView?.toGone()
        binding?.errorDescriptionTextView?.toVisible()
        binding?.errorDescriptionTextView?.text = message
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun showMovies(featuredMovie: FeaturedMovie?, movies: List<Movie>) {
        binding?.recyclerView?.toVisible()
        binding?.errorDescriptionTextView?.toGone()
        (binding?.recyclerView?.adapter as? HomeAdapter)?.run {
            addData(featuredMovie, movies)
            notifyDataSetChanged()
        }
    }

    private fun initViews() {
        binding?.recyclerView?.apply {
            val gridLayout = createGridLayoutManager()
            layoutManager = gridLayout
            adapter = HomeAdapter()
            addItemDecoration(GridSpacingItemDecoration(gridLayout.spanCount, dip(6f)))
        }
    }

    private fun RecyclerView.createGridLayoutManager(): GridLayoutManager =
        GridLayoutManager(context, 3, RecyclerView.VERTICAL, false).apply {
            // span 사이즈 설정
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int =
                    when(adapter?.getItemViewType(position)) {
                        ITEM_VIEW_TYPE_SECTION_HEADER,
                        ITEM_VIEW_TYPE_FEATURED -> {
                            spanCount // 3
                        } else -> {
                            1 // 추천 영화는 1줄
                        }
                    }
            }
        }
}