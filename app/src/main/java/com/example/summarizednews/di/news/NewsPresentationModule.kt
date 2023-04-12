package com.example.summarizednews.di.news

import com.example.summarizednews.news.presentation.detail.NewsDetailState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object NewsPresentationModule {
    @Provides
    fun provideInitialNewsDetailState() = NewsDetailState()
}