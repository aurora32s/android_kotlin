package com.haman.aop_part5_chapter07.data.api.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.haman.aop_part5_chapter07.data.api.MovieApi
import com.haman.aop_part5_chapter07.domain.model.Movie
import kotlinx.coroutines.tasks.await

class MovieFireStoreApi(
    private val fireStore: FirebaseFirestore
) : MovieApi {

    override suspend fun getAllMovies(): List<Movie> =
        fireStore.collection("movies")
            .get()
            .await()
            .map { it.toObject() }

}