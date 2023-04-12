package com.example.summarizednews.news.presentation.detail

import com.example.summarizednews.flux.Action
import com.example.summarizednews.news.domain.model.NewsDetail

data class NewsDetailState(
    val isLoading: Boolean = false,
    val newsDetail: NewsDetail = emptyNewsDetail,
    val summary: String? = null,
    val error: Throwable? = null
)

private val emptyNewsDetail = NewsDetail(
    id = "",
    title = "",
    section = "",
    writtenAt = "",
    body = ""
)

sealed interface NewsDetailAction : Action {
    data class FetchNewsDetail(val id: String) : NewsDetailAction
    object FetchingStarted : NewsDetailAction
    data class FetchingSuccess(val newsDetail: NewsDetail) : NewsDetailAction
    data class SummarizingSuccess(val summary: String) : NewsDetailAction
    data class ErrorOccurred(val cause: Throwable) : NewsDetailAction
}