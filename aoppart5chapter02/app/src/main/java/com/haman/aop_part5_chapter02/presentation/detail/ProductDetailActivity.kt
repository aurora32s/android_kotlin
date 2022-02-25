package com.haman.aop_part5_chapter02.presentation.detail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.core.view.isVisible
import com.haman.aop_part5_chapter02.databinding.ActivityDetailBinding
import com.haman.aop_part5_chapter02.extensions.load
import com.haman.aop_part5_chapter02.extensions.loadCenterCrop
import com.haman.aop_part5_chapter02.extensions.toast
import com.haman.aop_part5_chapter02.presentation.BaseActivity
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

internal class ProductDetailActivity: BaseActivity<ProductDetailViewModel, ActivityDetailBinding>() {

    companion object {
        const val PRODUCT_ID_KEY = "PRODUCT_ID_KEY"
        const val PRODUCT_ORDERED_RESULT_CODE = 99

        fun netIntent(context: Context, productId: Long) =
            Intent(context, ProductDetailActivity::class.java).apply {
                putExtra(PRODUCT_ID_KEY, productId)
            }
    }

    override val viewModel: ProductDetailViewModel by inject{
        parametersOf(
            intent.getLongExtra(PRODUCT_ID_KEY, -1L)
        )
    }

    override fun getViewBinding(): ActivityDetailBinding =
        ActivityDetailBinding.inflate(layoutInflater)

    override fun observeData() = viewModel.productDetailStateLiveData.observe(this) {
        when (it) {
            ProductDetailState.Uninitialized -> {
                initViews()
            }
            ProductDetailState.Loading -> {
                handleLoading()
            }
            is ProductDetailState.Success -> {
                handleSuccess(it)
            }
            ProductDetailState.Error -> {
                handleError()
            }
            ProductDetailState.Order -> {
                handleOrder()
            }
        }
    }

    private fun initViews() = with(binding) {
        setSupportActionBar(toolbar)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)
        title = ""
        toolbar.setNavigationOnClickListener { finish() }
        orderButton.setOnClickListener {
            viewModel.orderProduct()
        }
    }

    private fun handleLoading() = with(binding) {
        progressBar.isVisible = true
    }

    @SuppressLint("SetTextI18n")
    private fun handleSuccess(state: ProductDetailState.Success) = with(binding) {
        progressBar.isVisible = false
        val product = state.productEntity
        title = product.productName
        productCategoryTextView.text = product.productType
        productImageView.loadCenterCrop(product.productImage, 8f)
        productPriceTextView.text = "${product.productPrice}원"
        introductionImageView.load(product.productImage)
    }

    private fun handleError() {
        toast("제품 정보를 불러올 수 없습니다.")
        finish()
    }

    private fun handleOrder() {
        setResult(PRODUCT_ORDERED_RESULT_CODE)
        toast("성공적으로 주문이 완료되었습니다.")
        finish()
    }
}