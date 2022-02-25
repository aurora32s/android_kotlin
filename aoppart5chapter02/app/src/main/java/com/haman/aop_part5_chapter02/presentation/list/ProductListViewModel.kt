package com.haman.aop_part5_chapter02.presentation.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.haman.aop_part5_chapter02.domain.GetProductListUseCase
import com.haman.aop_part5_chapter02.presentation.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class ProductListViewModel(
    private val getProductListUseCase: GetProductListUseCase
) : BaseViewModel() {

    private val _productListStateLiveData =
        MutableLiveData<ProductListState>(ProductListState.Uninitialized)
    val productListStateLiveData
        get() = _productListStateLiveData

    override fun fetchData(): Job = viewModelScope.launch {
        setState(ProductListState.Loading)
        setState(ProductListState.Success(getProductListUseCase()))
    }

    private fun setState(state: ProductListState) {
        _productListStateLiveData.postValue(state)
    }
}