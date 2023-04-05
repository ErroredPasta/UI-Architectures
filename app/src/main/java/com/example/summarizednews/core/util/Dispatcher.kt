package com.example.summarizednews.core.util

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Dispatcher(val type: Type) {
    enum class Type {
        IO, DEFAULT, UNCONFINED
    }
}