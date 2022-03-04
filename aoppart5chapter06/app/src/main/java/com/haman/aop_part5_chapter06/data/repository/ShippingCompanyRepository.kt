package com.haman.aop_part5_chapter06.data.repository

import com.haman.aop_part5_chapter06.data.entity.ShippingCompany

/**
 * 택배 회사 정보 관리
 */
interface ShippingCompanyRepository {
    /**
     * 회사 리스트 요청
     */
    suspend fun getShippingCompanies(): List<ShippingCompany>
    /**
     * 추천 회사 검색
     */
    suspend fun getRecommendShippingCompany(invoice: String): ShippingCompany?
}