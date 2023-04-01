package com.example.summarizednews.news.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.summarizednews.R
import com.example.summarizednews.core.presentation.showToast
import com.example.summarizednews.databinding.FragmentNewsDetailBinding
import com.example.summarizednews.news.domain.model.NewsDetail
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NewsDetailFragment : Fragment(), NewsDetailView {
    private val navArgs by navArgs<NewsDetailFragmentArgs>()

    private var _binding: FragmentNewsDetailBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var presenter: NewsDetailPresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentNewsDetailBinding.inflate(inflater, container, false)
        presenter.fetchNewsDetail(newsId = navArgs.newsId)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onLoading() = with(binding) {
        newsDetailProgressBar.isVisible = true
        newsDetailLayout.isVisible = false
        newsSummaryLayout.isVisible = false
    }

    override fun onNewsDetailFetched(newsDetail: NewsDetail) = with(binding) {
        newsDetailProgressBar.isVisible = false
        newsDetailLayout.isVisible = true
        newsDetailTitle.text = newsDetail.title
        newsDetailBody.text =
            HtmlCompat.fromHtml(newsDetail.body, HtmlCompat.FROM_HTML_MODE_COMPACT)
        newsDetailSection.text = newsDetail.section
        newsDetailDate.text = newsDetail.writtenAt
    }

    override fun onSummaryFetched(summary: String) = with(binding) {
        newsSummaryLayout.isVisible = true
        newsSummaryContent.text = summary
    }

    override fun onError(cause: Throwable) {
        showToast(message = cause.message ?: getString(R.string.error_occurred_while_getting_news))
    }
}