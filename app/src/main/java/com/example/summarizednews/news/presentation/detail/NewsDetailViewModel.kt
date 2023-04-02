package com.example.summarizednews.news.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.summarizednews.core.util.runCoroutineCatching
import com.example.summarizednews.news.domain.model.NewsDetail
import com.example.summarizednews.news.domain.repository.NewsRepository
import com.example.summarizednews.summary.domain.repository.SummaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsDetailViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val summaryRepository: SummaryRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val newsId = requireNotNull(savedStateHandle.get<String>("news_id"))

    private val _newsDetail = MutableStateFlow<NewsDetail?>(null)
    val newsDetail = _newsDetail.onSubscription {
        if (_newsDetail.value == null) fetchNewsDetail()
    }.onEach {
        if (it != null) fetchNewsSummary(content = it.body)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
        initialValue = null
    )

    private val _newsSummary = MutableStateFlow<String?>(null)
    val newsSummary = _newsSummary.asStateFlow()

    private val _error = MutableStateFlow<Throwable?>(null)
    val error = _error.asStateFlow()

    private fun fetchNewsDetail() {
        viewModelScope.launch {
            runCoroutineCatching {
                newsRepository.getNewsDetailById(id = newsId)
            }.onFailure { cause ->
                _error.update { cause }
            }.onSuccess { fetchedNewsDetail ->
                _newsDetail.update { fetchedNewsDetail }
            }
        }
    }

    private fun fetchNewsSummary(content: String) {
        viewModelScope.launch {
            runCoroutineCatching {
                summaryRepository.summarize(content = content)
            }.onFailure { cause ->
                _error.update { cause }
            }.onSuccess { fetchedSummary ->
                _newsSummary.update { fetchedSummary }
            }
        }
    }

    fun errorHandlingDone() {
        _error.update { null }
    }
}