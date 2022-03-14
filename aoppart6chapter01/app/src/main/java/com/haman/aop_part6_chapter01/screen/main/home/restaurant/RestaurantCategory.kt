package com.haman.aop_part6_chapter01.screen.main.home.restaurant

import androidx.annotation.StringRes
import com.haman.aop_part6_chapter01.R

enum class RestaurantCategory(
    @StringRes val categoryNameId: Int, // resource id
    @StringRes val categoryTypeId: Int
) {
    ALL(R.string.all, R.string.all_type)
}