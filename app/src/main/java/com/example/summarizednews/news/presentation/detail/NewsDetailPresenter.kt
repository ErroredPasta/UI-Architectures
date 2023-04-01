package com.example.summarizednews.news.presentation.detail

import com.example.summarizednews.core.util.runCoroutineCatching
import com.example.summarizednews.news.domain.repository.NewsRepository
import com.example.summarizednews.summary.domain.repository.SummaryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewsDetailPresenter @Inject constructor(
    private val view: NewsDetailView,
    private val newsRepository: NewsRepository,
    private val summaryRepository: SummaryRepository,
    private val lazyCoroutineScope: dagger.Lazy<CoroutineScope>,
) {
    fun fetchNewsDetail(newsId: String) {
        lazyCoroutineScope.get().launch {
            view.onLoading()

            runCoroutineCatching {
                newsRepository.getNewsDetailById(id = newsId)
            }.onFailure { cause ->
                view.onError(cause = cause)
            }.onSuccess { newsDetail ->
                view.onNewsDetailFetched(newsDetail = newsDetail)
                fetchNewsSummary(newsDetail.body)
            }
        }
    }

    private suspend fun fetchNewsSummary(content: String) {
        runCoroutineCatching {
            summaryRepository.summarize(content = content)
        }.onFailure { cause ->
            view.onError(cause = cause)
        }.onSuccess { summary ->
            view.onSummaryFetched(summary = summary)
        }
    }
}