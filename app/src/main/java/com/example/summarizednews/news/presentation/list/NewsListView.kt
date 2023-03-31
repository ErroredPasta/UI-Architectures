package com.example.summarizednews.news.presentation.list

import android.widget.Toast
import androidx.core.view.isVisible
import com.example.summarizednews.R
import com.example.summarizednews.databinding.FragmentNewsListBinding

fun FragmentNewsListBinding.onLoading() {
    showLoading(isLoading = true)
}

fun FragmentNewsListBinding.onSuccess() {
    showLoading(isLoading = false)
}

fun FragmentNewsListBinding.onError(cause: Throwable) = with(root.context) {
    showLoading(isLoading = false)

    Toast.makeText(
        this,
        cause.message ?: getString(R.string.error_occurred_while_getting_news),
        Toast.LENGTH_SHORT
    ).show()
}

private fun FragmentNewsListBinding.showLoading(isLoading: Boolean) {
    newsListRefreshLayout.isRefreshing = isLoading
    newsListLoadingShimmer.run {
        isVisible = isLoading
        if (isLoading) startShimmer() else stopShimmer()
    }
    newsListRecyclerView.isVisible = !isLoading
}