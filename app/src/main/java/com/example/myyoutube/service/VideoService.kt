package com.example.myyoutube.service

import com.example.myyoutube.dto.VideoDTO
import retrofit2.Call
import retrofit2.http.GET

interface VideoService {
    @GET("/v3/b8b3d921-4c9c-4251-b600-aabd581cb2d1")
    fun listVideos(): Call<VideoDTO>
}