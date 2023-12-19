package com.wei.picquest.core.network.retrofit

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.wei.core.network.BuildConfig
import com.wei.picquest.core.network.PqNetworkDataSource
import com.wei.picquest.core.network.model.NetworkSearchImages
import com.wei.picquest.core.network.model.NetworkSearchVideos
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

private const val PIXABAY_BASE_URL = BuildConfig.BACKEND_URL
private const val API_KEY = BuildConfig.API_KEY

/**
 * Retrofit API declaration for Pixabay Network API
 */
interface RetrofitPixabayApi {
    /**
     * https://pixabay.com/api/?key=${api key}&q=yellow+flowers&image_type=photo
     */
    @GET(".")
    suspend fun searchImages(
        @Query("key") apiKey: String = API_KEY,
        @Query("q") query: String,
        @Query("image_type") imageType: String = "photo",
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        // Add more parameters as needed
    ): NetworkSearchImages

    /**
     * https://pixabay.com/api/videos/?key=${api key}&q=yellow+flowers
     */
    @GET("./videos/")
    suspend fun searchVideos(
        @Query("key") apiKey: String = API_KEY,
        @Query("q") query: String,
        @Query("video_type") videoType: String = "film",
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        // Add more parameters as needed
    ): NetworkSearchVideos
}

/**
 * [Retrofit] backed [PqNetworkDataSource]
 */
@Singleton
class RetrofitPqNetwork @Inject constructor(
    networkJson: Json,
    okhttpCallFactory: Call.Factory,
) : PqNetworkDataSource {

    private val pixabayApi = Retrofit.Builder()
        .baseUrl(PIXABAY_BASE_URL)
        .callFactory(okhttpCallFactory)
        .addConverterFactory(
            networkJson.asConverterFactory("application/json".toMediaType()),
        )
        .build()
        .create(RetrofitPixabayApi::class.java)

    override suspend fun searchImages(query: String, page: Int, perPage: Int): NetworkSearchImages {
        return pixabayApi.searchImages(query = query, page = page, perPage = perPage)
    }

    override suspend fun searchVideos(query: String, page: Int, perPage: Int): NetworkSearchVideos {
        return pixabayApi.searchVideos(query = query, page = page, perPage = perPage)
    }
}
