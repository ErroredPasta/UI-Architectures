package com.example.summarizednews.di.app

import android.util.Log
import com.example.summarizednews.AppState
import com.example.summarizednews.core.presentation.SuppressedMiddleware
import com.example.summarizednews.core.presentation.SuppressedReducer
import com.example.summarizednews.news.presentation.detail.NewsDetailState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import org.reduxkotlin.*
import org.reduxkotlin.threadsafe.createThreadSafeStore
import org.reduxkotlin.thunk.createThunkMiddleware
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ReduxModule {
    @Provides
    fun provideAppReducer(
        newsDetailReducer: SuppressedReducer<NewsDetailState>,
    ): SuppressedReducer<AppState> = { state, action ->
        AppState(
            newsDetailState = newsDetailReducer(state.newsDetailState, action)
        )
    }

    @Provides
    @Singleton
    fun provideStore(
        appReducer: SuppressedReducer<AppState>,
        middlewareSet: Set<SuppressedMiddleware<AppState>>,
    ): Store<AppState> = createThreadSafeStore(
        reducer = appReducer,
        preloadedState = AppState(),
        enhancer = applyMiddleware(
            createThunkMiddleware(), *middlewareSet.toTypedArray()
        )
    )

    @Provides
    @IntoSet
    fun provideLogger(): SuppressedMiddleware<AppState> = middleware { store, next, action ->
        Log.d("Logger", "Before ${action::class.simpleName}: ${store.state}")
        val result = next(action)
        Log.d("Logger", "After ${action::class.simpleName}: ${store.state} ")
        result
    }
}