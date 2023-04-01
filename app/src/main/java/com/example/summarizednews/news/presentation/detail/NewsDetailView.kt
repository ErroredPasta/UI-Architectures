package com.example.summarizednews.news.presentation.detail

import com.example.summarizednews.news.domain.model.NewsDetail

interface NewsDetailView {
    fun onLoading()
    fun onNewsDetailFetched(newsDetail: NewsDetail)
    fun onSummaryFetched(summary: String)
    fun onError(cause: Throwable)
}