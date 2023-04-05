package com.example.summarizednews.news.presentation.detail

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

sealed interface NewsDetailAction {
    data class FetchNewsDetail(val id: String)
    object FetchingStarted
    data class FetchingSuccess(val newsDetail: NewsDetail)
    data class SummarizingSuccess(val summary: String)
    data class ErrorOccurred(val cause: Throwable)
}