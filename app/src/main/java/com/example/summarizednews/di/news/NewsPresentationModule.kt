package com.example.summarizednews.di.news

import com.example.summarizednews.AppState
import com.example.summarizednews.core.presentation.SuppressedMiddleware
import com.example.summarizednews.core.presentation.SuppressedReducer
import com.example.summarizednews.news.presentation.NewsSummaryThunk
import com.example.summarizednews.news.presentation.NewsThunk
import com.example.summarizednews.news.presentation.detail.NewsDetailAction
import com.example.summarizednews.news.presentation.detail.NewsDetailState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import org.reduxkotlin.middleware

@Module
@InstallIn(SingletonComponent::class)
object NewsPresentationModule {
    @Provides
    @IntoSet
    fun provideNewsMiddleware(
        newsThunk: NewsThunk,
        newsSummaryThunk: NewsSummaryThunk,
    ): SuppressedMiddleware<AppState> = middleware { store, next, action ->
        val dispatch = store.dispatch

        when (action) {
            is NewsDetailAction.FetchNewsDetail -> dispatch(newsThunk.fetchNewsDetail(newsId = action.id))

            is NewsDetailAction.FetchingSuccess -> {
                next(action)
                dispatch(newsSummaryThunk.fetchSummary(content = action.newsDetail.body))
            }

            else -> next(action)
        }
    }

    @Provides
    fun provideNewsDetailReducer(): SuppressedReducer<NewsDetailState> =
        { newsDetailState, action ->
            when (action) {
                NewsDetailAction.FetchingStarted -> NewsDetailState(isLoading = true)

                is NewsDetailAction.FetchingSuccess -> newsDetailState.copy(
                    isLoading = false,
                    newsDetail = action.newsDetail
                )

                is NewsDetailAction.SummarizingSuccess -> newsDetailState.copy(
                    summary = action.summary
                )

                is NewsDetailAction.ErrorOccurred -> newsDetailState.copy(
                    isLoading = false,
                    error = action.cause
                )

                else -> newsDetailState
            }
        }
}