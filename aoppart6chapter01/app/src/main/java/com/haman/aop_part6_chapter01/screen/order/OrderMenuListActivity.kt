package com.haman.aop_part6_chapter01.screen.order

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.haman.aop_part6_chapter01.databinding.ActivityOrderListBinding
import com.haman.aop_part6_chapter01.model.food.FoodModel
import com.haman.aop_part6_chapter01.screen.base.BaseActivity
import com.haman.aop_part6_chapter01.util.provider.ResourcesProvider
import com.haman.aop_part6_chapter01.widget.adapter.ModelRecyclerAdapter
import com.haman.aop_part6_chapter01.widget.adapter.listener.order.OrderMenuListListener
import org.koin.android.ext.android.inject

class OrderMenuListActivity :
    BaseActivity<OrderMenuListViewModel, ActivityOrderListBinding>() {

    override val viewModel: OrderMenuListViewModel by inject()
    private val resourcesProvider by inject<ResourcesProvider>()

    override fun getViewBinding(): ActivityOrderListBinding =
        ActivityOrderListBinding.inflate(layoutInflater)

    private val adapter by lazy {
        ModelRecyclerAdapter<FoodModel, OrderMenuListViewModel>(
            listOf(),
            viewModel,
            resourcesProvider,
            adapterListener = object : OrderMenuListListener {
                override fun onRemoveItem(foodModel: FoodModel) {
                    viewModel.removeOrderMenu(foodModel)
                }
            }
        )
    }

    override fun initViews() = with(binding) {
        recyclerVIew.adapter = adapter
        toolbar.setNavigationOnClickListener { finish() }
        confirmButton.setOnClickListener {
            viewModel.orderMenu()
        }
        orderClearButton.setOnClickListener {
            viewModel.clearOrderMenu()
        }
    }

    override fun observeData() = viewModel.orderStateLiveData.observe(this) {
        when (it) {
            is OrderMenuState.Error -> handleErrorState(it)
            OrderMenuState.Loading -> handleLoadingState()
            OrderMenuState.Order -> handleOrderState()
            is OrderMenuState.Success -> handleSuccessState(it)
            OrderMenuState.UnInitialized -> Unit
        }
    }

    private fun handleErrorState(state: OrderMenuState.Error) {

    }

    private fun handleLoadingState() {
        binding.progressBar.isVisible = true
    }

    private fun handleSuccessState(state: OrderMenuState.Success) = with(binding) {
        progressBar.isGone = true

        val menuOrderIsEmpty = state.restaurantFoodModelIst?.isEmpty() ?: true
        confirmButton.isEnabled = menuOrderIsEmpty.not()
        if (menuOrderIsEmpty) {
            Toast.makeText(
                this@OrderMenuListActivity,
                "주문 내용이 없어 화면을 종료합니다.",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        } else {
            adapter.submitList(state.restaurantFoodModelIst)
        }
    }

    private fun handleOrderState() {

    }

    companion object {
        fun newInstance(context: Context) = Intent(context, OrderMenuListActivity::class.java)
    }

}