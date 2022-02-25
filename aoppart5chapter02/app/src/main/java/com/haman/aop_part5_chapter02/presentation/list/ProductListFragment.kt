package com.haman.aop_part5_chapter02.presentation.list

import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import com.haman.aop_part5_chapter02.databinding.FragmentProductBinding
import com.haman.aop_part5_chapter02.extensions.toast
import com.haman.aop_part5_chapter02.presentation.BaseFragment
import com.haman.aop_part5_chapter02.presentation.adapter.ProductListAdapter
import com.haman.aop_part5_chapter02.presentation.detail.ProductDetailActivity
import com.haman.aop_part5_chapter02.presentation.main.MainActivity
import org.koin.android.ext.android.inject

internal class ProductListFragment(

) : BaseFragment<ProductListViewModel, FragmentProductBinding>() {

    companion object {
        const val TAG = "ProductListFragment"
    }

    override val viewModel: ProductListViewModel by inject()
    override fun getViewBinding(): FragmentProductBinding =
        FragmentProductBinding.inflate(layoutInflater)

    private val adapter = ProductListAdapter()

    private val startProductDetailForResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            // TODO 성공적으로 처리 완료 이후 동작
            if (result.resultCode == ProductDetailActivity.PRODUCT_ORDERED_RESULT_CODE) {
                (requireActivity() as MainActivity).viewModel.refreshOrderList()
            }
        }

    override fun observeData() =
        viewModel.productListStateLiveData.observe(this) {
            when (it) {
                ProductListState.Uninitialized -> {
                    initViews()
                }
                ProductListState.Loading -> {
                    handleLoadingState()
                }
                is ProductListState.Success -> {
                    handleSuccessState(it)
                }
                ProductListState.Error -> {
                    handleErrorState()
                }
            }
        }

    private fun initViews() = with(binding) {
        recyclerView.adapter = adapter

        refreshLayout.setOnRefreshListener {
            viewModel.fetchData()
        }
    }

    private fun handleLoadingState() = with(binding) {
        refreshLayout.isRefreshing = true
    }

    private fun handleSuccessState(state: ProductListState.Success) = with(binding) {
        refreshLayout.isRefreshing = false

        if (state.productList.isEmpty()) {
            emptyResultTextView.isVisible = true
            recyclerView.isVisible = false
        } else {
            emptyResultTextView.isVisible = false
            recyclerView.isVisible = true
            adapter.setData(state.productList) {
                startProductDetailForResult.launch(
                    ProductDetailActivity.netIntent(requireContext(), it.id)
                )
            }
        }
    }

    private fun handleErrorState() = with(binding) {
        refreshLayout.isRefreshing = false
        requireActivity().toast("에러가 발생하였습니다.")
    }
}