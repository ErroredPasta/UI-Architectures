package com.example.summarizednews.di.news

import androidx.fragment.app.Fragment
import com.example.summarizednews.news.presentation.list.NewsListView
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
object NewsListPresentationModule {
    @Provides
    fun provideNewsListView(fragment: Fragment): NewsListView = fragment as NewsListView
}