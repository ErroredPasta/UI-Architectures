package com.example.summarizednews.news.presentation

import com.example.summarizednews.AppState
import com.example.summarizednews.core.util.runCoroutineCatching
import com.example.summarizednews.news.domain.repository.NewsRepository
import com.example.summarizednews.news.presentation.detail.NewsDetailAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.launch
import org.reduxkotlin.thunk.Thunk
import javax.inject.Inject

class NewsThunk @Inject constructor(
    private val newsRepository: NewsRepository,
    coroutineDispatcher: MainCoroutineDispatcher,
) {
    private val scope = CoroutineScope(coroutineDispatcher)

    fun fetchNewsDetail(newsId: String): Thunk<AppState> = { dispatch, _, _ ->
        scope.launch {
            dispatch(NewsDetailAction.FetchingStarted)

            runCoroutineCatching {
                newsRepository.getNewsDetailById(id = newsId)
            }.onFailure { cause ->
                dispatch(NewsDetailAction.ErrorOccurred(cause = cause))
            }.onSuccess { newsDetail ->
                dispatch(NewsDetailAction.FetchingSuccess(newsDetail = newsDetail))
            }
        }
    }
}