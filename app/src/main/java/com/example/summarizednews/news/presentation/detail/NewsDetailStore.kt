package com.example.summarizednews.news.presentation.detail

import com.example.summarizednews.core.util.runCoroutineCatching
import com.example.summarizednews.flux.Action
import com.example.summarizednews.flux.Dispatcher
import com.example.summarizednews.flux.Store
import com.example.summarizednews.news.domain.repository.NewsRepository
import com.example.summarizednews.summary.domain.repository.SummaryRepository
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@ActivityRetainedScoped
class NewsDetailStore @Inject constructor(
    initialState: NewsDetailState,
    mainCoroutineDispatcher: MainCoroutineDispatcher,
    private val newsRepository: NewsRepository,
    private val summaryRepository: SummaryRepository,
) : Store<NewsDetailState>(initialState) {
    private val scope = CoroutineScope(mainCoroutineDispatcher)

    override fun sendAction(action: Action) {
        if (action !is NewsDetailAction) return

        when (action) {
            is NewsDetailAction.FetchNewsDetail -> fetchNewsDetail(newsId = action.id)

            NewsDetailAction.FetchingStarted -> state = NewsDetailState(isLoading = true)

            is NewsDetailAction.FetchingSuccess -> {
                state = state.copy(isLoading = false, newsDetail = action.newsDetail)
                summarize(content = action.newsDetail.body)
            }

            is NewsDetailAction.SummarizingSuccess -> state = state.copy(summary = action.summary)

            is NewsDetailAction.ErrorOccurred -> state = state.copy(error = action.cause)
        }
    }

    private fun fetchNewsDetail(newsId: String) {
        scope.launch {
            runCoroutineCatching {
                Dispatcher.dispatch(action = NewsDetailAction.FetchingStarted)
                newsRepository.getNewsDetailById(id = newsId)
            }.onFailure { cause ->
                Dispatcher.dispatch(action = NewsDetailAction.ErrorOccurred(cause = cause))
            }.onSuccess { newsDetail ->
                Dispatcher.dispatch(action = NewsDetailAction.FetchingSuccess(newsDetail = newsDetail))
            }
        }
    }

    private fun summarize(content: String) {
        scope.launch {
            runCoroutineCatching {
                summaryRepository.summarize(content = content)
            }.onFailure { cause ->
                Dispatcher.dispatch(action = NewsDetailAction.ErrorOccurred(cause = cause))
            }.onSuccess { summary ->
                Dispatcher.dispatch(action = NewsDetailAction.SummarizingSuccess(summary = summary))
            }
        }
    }
}