package com.haman.aop_part5_chapter06.presentation.addtrackingitem

import com.haman.aop_part5_chapter06.data.entity.ShippingCompany
import com.haman.aop_part5_chapter06.presentation.BasePresenter
import com.haman.aop_part5_chapter06.presentation.BaseView

class AddTrackingItemsContract {

    interface View : BaseView<Presenter> {
        fun showShippingCompaniesLoadingIndicator() // 택배 회사 요청 로딩 show
        fun hideShippingCompaniesLoadingIndicator() // 택배 회사 요청 로딩 hide
        fun showSaveTrackingItemIndicator() // 저장 로딩 show
        fun hideSaveTrackingItemIndicator() // 저장 로딩 hide
        fun showRecommendCompanyLoadingIndicator() // 추천 택배 회사 요청 로딩 show
        fun hideRecommendCompanyLoadingIndicator() // 추천 택배 회사 요청 로딩 hide
        fun showCompanies(companies: List<ShippingCompany>) // 택배 회사 요청
        fun showRecommendCompany(company: ShippingCompany) // 추천 택배 회사 요청
        fun enableSaveButton() // 저장 버튼 enable
        fun disableSaveButton() // 저장 버튼 disable
        fun showErrorToast(message: String) // toast
        fun finish() // 종료
    }

    interface Presenter : BasePresenter {
        var invoice: String? // 사용자가 입력한 송장번호
        var shippingCompanies: List<ShippingCompany>? // 택배회사 리스트
        var selectedShippingCompany: ShippingCompany? // 선택된 택배회사

        fun fetchShippingCompanies() // 택배회사 리스트 요청
        fun fetchRecommendShippingCompany() // 택배회사 추천 요청
        fun changeSelectedShippingCompany(companyName: String) // 택배 회사 선택 이벤트
        fun changeShippingInvoice(invoice: String) // 송장 번호 변경
        fun saveTrackingItem() // 저장 버튼 클릭
    }
}