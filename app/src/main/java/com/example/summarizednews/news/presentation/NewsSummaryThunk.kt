package com.example.summarizednews.news.presentation

import com.example.summarizednews.AppState
import com.example.summarizednews.core.util.runCoroutineCatching
import com.example.summarizednews.news.presentation.detail.NewsDetailAction
import com.example.summarizednews.summary.domain.repository.SummaryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.launch
import org.reduxkotlin.thunk.Thunk
import javax.inject.Inject

class NewsSummaryThunk @Inject constructor(
    private val repository: SummaryRepository,
    coroutineDispatcher: MainCoroutineDispatcher
) {
    private val scope = CoroutineScope(coroutineDispatcher)

    fun fetchSummary(content: String): Thunk<AppState> = { dispatch, _, _ ->
        scope.launch {
            runCoroutineCatching {
                repository.summarize(content = content)
            }.onFailure { cause ->
                dispatch(NewsDetailAction.ErrorOccurred(cause = cause))
            }.onSuccess { summary ->
                dispatch(NewsDetailAction.SummarizingSuccess(summary = summary))
            }
        }
    }
}