package com.haman.aop_part4_chapter05.model.response

data class GithubAccessTokenResponse (
    val accessToken: String,
    val scope: String,
    val tokenType: String
)