package com.example.summarizednews.core.presentation

import org.reduxkotlin.Middleware
import org.reduxkotlin.Reducer

typealias SuppressedReducer<State> = @JvmSuppressWildcards Reducer<State>
typealias SuppressedMiddleware<State> = @JvmSuppressWildcards Middleware<State>