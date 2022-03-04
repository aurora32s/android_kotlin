package com.haman.aop_part5_chapter06.presentation.addtrackingitem

import android.util.Log
import com.haman.aop_part5_chapter06.data.entity.ShippingCompany
import com.haman.aop_part5_chapter06.data.entity.TrackingItem
import com.haman.aop_part5_chapter06.data.repository.ShippingCompanyRepository
import com.haman.aop_part5_chapter06.data.repository.TrackingItemRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class AddTrackingItemPresenter(
    private val view: AddTrackingItemsContract.View,
    private val shippingCompanyRepository: ShippingCompanyRepository,
    private val trackerRepository: TrackingItemRepository
): AddTrackingItemsContract.Presenter {

    override val scope: CoroutineScope = MainScope()

    override var invoice: String? = null
    override var shippingCompanies: List<ShippingCompany>? = null
    override var selectedShippingCompany: ShippingCompany? = null

    override fun onViewCreated() {
        fetchShippingCompanies()
    }

    override fun onDestroyView() {}

    // 택배 회사 정보 요청
    override fun fetchShippingCompanies() {
        scope.launch {
            view.showShippingCompaniesLoadingIndicator()
            // 처음 택배 회사 정보를 받아오는 경우
            if (shippingCompanies.isNullOrEmpty()) {
                shippingCompanies = shippingCompanyRepository.getShippingCompanies()
            }
            shippingCompanies?.let { view.showCompanies(it) }
            view.hideShippingCompaniesLoadingIndicator()
        }
    }

    // 택배 회사 선택
    override fun changeSelectedShippingCompany(companyName: String) {
        selectedShippingCompany = shippingCompanies?.find { it.name == companyName }
        enableSaveButtonIfAvailable()
    }
    // 송장 번호 변경
    override fun changeShippingInvoice(invoice: String) {
        this.invoice = invoice
        enableSaveButtonIfAvailable()
    }
    // 새로운 송장 번호 저장
    override fun saveTrackingItem() {
        scope.launch {
            try {
                view.showSaveTrackingItemIndicator()
                trackerRepository.saveTrackingItem(
                    TrackingItem(
                        invoice!!,
                        selectedShippingCompany!!
                    )
                )
                view.finish()
            } catch (exception: Exception) {
                view.showErrorToast(exception.message ?: "서비스에 문제가 생겨서 운송장을 추가하지 못했습니다.")
            } finally {
                view.hideSaveTrackingItemIndicator()
            }
        }
    }

    override fun fetchRecommendShippingCompany() {
        scope.launch {
            view.showRecommendCompanyLoadingIndicator()
            shippingCompanyRepository.getRecommendShippingCompany(invoice!!)?.let {
                view.showRecommendCompany(it)
            }
            view.hideRecommendCompanyLoadingIndicator()
        }
    }

    private fun enableSaveButtonIfAvailable() {
        // 송장번호도 입력하고 택배회사도 선택한 경우
        if (!invoice.isNullOrBlank() && selectedShippingCompany != null) {
            view.enableSaveButton()
        } else {
            view.disableSaveButton()
        }
    }

}