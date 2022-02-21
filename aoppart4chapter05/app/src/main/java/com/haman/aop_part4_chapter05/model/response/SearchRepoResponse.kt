package com.haman.aop_part4_chapter05.model.response

import com.haman.aop_part4_chapter05.model.entity.GithubRepoEntity

data class SearchRepoResponse(
    val totalCount: Int,
    val items: List<GithubRepoEntity>
)
