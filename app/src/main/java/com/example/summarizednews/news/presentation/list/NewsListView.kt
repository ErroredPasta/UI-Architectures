package com.example.summarizednews.news.presentation.list

import androidx.paging.PagingData
import com.example.summarizednews.news.domain.model.News

interface NewsListView {
    suspend fun onPageLoaded(pageData: PagingData<News>)
}