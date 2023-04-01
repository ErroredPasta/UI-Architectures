package com.example.summarizednews.news.presentation.list

import com.example.summarizednews.news.domain.repository.NewsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class NewsListPresenter @Inject constructor(
    private val view: NewsListView,
    private val repository: NewsRepository,
    private val lazyCoroutineScope: dagger.Lazy<CoroutineScope>,
) {
    fun fetchNewsList() {
        repository.getNewsList().onEach {
            view.onPageLoaded(it)
        }.launchIn(lazyCoroutineScope.get())
    }

    fun reloadNewsList() {
        repository.reloadNewsList()
    }
}