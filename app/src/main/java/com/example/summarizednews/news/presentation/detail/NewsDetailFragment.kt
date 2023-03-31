package com.example.summarizednews.news.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.summarizednews.core.util.runCoroutineCatching
import com.example.summarizednews.databinding.FragmentNewsDetailBinding
import com.example.summarizednews.news.domain.repository.NewsRepository
import com.example.summarizednews.summary.domain.repository.SummaryRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NewsDetailFragment : Fragment() {
    @Inject
    lateinit var newsRepository: NewsRepository

    @Inject
    lateinit var summaryRepository: SummaryRepository

    private val navArgs by navArgs<NewsDetailFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = FragmentNewsDetailBinding.inflate(inflater, container, false).also { binding ->

        viewLifecycleOwner.lifecycleScope.launch {
            binding.onLoading()

            val newsDetailResult = runCoroutineCatching {
                newsRepository.getNewsDetailById(id = navArgs.newsId)
            }.onFailure { cause ->
                binding.onError(cause = cause)
            }.onSuccess { newsDetail ->
                binding.onNewsDetailFetched(newsDetail = newsDetail)
            }

            newsDetailResult.getOrNull()?.body?.let {
                runCoroutineCatching {
                    summaryRepository.summarize(content = it)
                }.onFailure { cause ->
                    binding.onError(cause = cause)
                }.onSuccess { summary ->
                    binding.onSummaryFetched(summary = summary)
                }
            }
        }
    }.root
}