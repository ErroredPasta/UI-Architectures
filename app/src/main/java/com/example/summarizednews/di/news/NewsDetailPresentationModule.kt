package com.example.summarizednews.di.news

import androidx.fragment.app.Fragment
import com.example.summarizednews.news.presentation.detail.NewsDetailView
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
object NewsDetailPresentationModule {
    @Provides
    fun provideNewsDetailView(fragment: Fragment) = fragment as NewsDetailView
}