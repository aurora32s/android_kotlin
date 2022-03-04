package com.haman.aop_part5_chapter06.data.entity

import com.google.gson.annotations.SerializedName

data class ShippingCompanies(
    // 하나의 데이터에 대해서 여러 이름으로 serialized 할 필요가 있는 경우
    @SerializedName("Company", alternate = ["Recommend"])
    val shippingCompanies: List<ShippingCompany>? = null
)
