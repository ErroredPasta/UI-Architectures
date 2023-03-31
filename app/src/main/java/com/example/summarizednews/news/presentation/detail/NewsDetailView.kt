package com.example.summarizednews.news.presentation.detail

import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import com.example.summarizednews.R
import com.example.summarizednews.databinding.FragmentNewsDetailBinding
import com.example.summarizednews.news.domain.model.NewsDetail

fun FragmentNewsDetailBinding.onLoading() {
    newsDetailProgressBar.isVisible = true
    newsDetailLayout.isVisible = false
}

fun FragmentNewsDetailBinding.onNewsDetailFetched(newsDetail: NewsDetail) {
    newsDetailProgressBar.isVisible = false
    newsDetailLayout.isVisible = true
    newsDetailTitle.text = newsDetail.title
    newsDetailBody.text = HtmlCompat.fromHtml(newsDetail.body, HtmlCompat.FROM_HTML_MODE_COMPACT)
    newsDetailSection.text = newsDetail.section
    newsDetailDate.text = newsDetail.writtenAt
}

fun FragmentNewsDetailBinding.onError(cause: Throwable) = with(root.context) {
    newsDetailProgressBar.isVisible = false

    Toast.makeText(
        this,
        cause.message ?: getString(R.string.error_occurred_while_getting_news),
        Toast.LENGTH_SHORT
    ).show()
}

fun FragmentNewsDetailBinding.onSummaryFetched(summary: String) {
    newsSummaryLayout.isVisible = true
    newsSummaryContent.text = summary
}