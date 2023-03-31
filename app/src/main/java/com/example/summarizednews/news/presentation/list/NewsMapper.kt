package com.example.summarizednews.news.presentation.list

import com.example.summarizednews.news.domain.model.News

internal fun News.toNewsUiState(
    onClick: () -> Unit
) = NewsUiState(
    id = id,
    title = title,
    writtenAt = writtenAt,
    section = section,
    onClick = onClick
)