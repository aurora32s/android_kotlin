package com.haman.aop_part5_chapter02.presentation.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.haman.aop_part5_chapter02.data.entity.product.ProductEntity
import com.haman.aop_part5_chapter02.domain.GetProductItemUseCase
import com.haman.aop_part5_chapter02.domain.OrderProductItemUseCase
import com.haman.aop_part5_chapter02.presentation.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class ProductDetailViewModel(
    private val productId: Long,
    private val getProductItemUseCase: GetProductItemUseCase,
    private val orderProductItemUseCase: OrderProductItemUseCase
): BaseViewModel() {

    private val _productDetailStateLiveData = MutableLiveData<ProductDetailState>(ProductDetailState.Uninitialized)
    val productDetailStateLiveData
        get() = _productDetailStateLiveData

    private lateinit var productEntity: ProductEntity

    override fun fetchData(): Job = viewModelScope.launch {
        setState(ProductDetailState.Loading)
        getProductItemUseCase(productId)?.let { product ->
            productEntity = product
            setState(
                ProductDetailState.Success(product)
            )
        } ?: kotlin.run {
            setState(ProductDetailState.Error)
        }
    }

    fun orderProduct() = viewModelScope.launch {
        if (::productEntity.isInitialized) {
            val productId = orderProductItemUseCase(productEntity)
            if (productEntity.id == productId) {
                setState(ProductDetailState.Order)
            }
        } else {
            setState(ProductDetailState.Error)
        }
    }

    private fun setState(state: ProductDetailState) {
        _productDetailStateLiveData.postValue(state)
    }
}