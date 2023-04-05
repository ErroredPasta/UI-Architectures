package com.example.summarizednews

import android.app.Application
import com.example.summarizednews.news.presentation.detail.NewsDetailState
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SummarizedNewsApplication : Application()

data class AppState(
    val newsDetailState: NewsDetailState = NewsDetailState()
)