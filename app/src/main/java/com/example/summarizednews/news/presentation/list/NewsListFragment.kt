package com.example.summarizednews.news.presentation.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.map
import com.example.summarizednews.R
import com.example.summarizednews.core.presentation.repeatOnLifecycleWhenStarted
import com.example.summarizednews.core.presentation.showToast
import com.example.summarizednews.databinding.FragmentNewsListBinding
import com.example.summarizednews.news.domain.model.News
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NewsListFragment : Fragment(), NewsListView {
    private val adapter = NewsListAdapter()
    private val navController by lazy { findNavController() }

    @Inject
    lateinit var presenter: NewsListPresenter

    private val binding get() = _binding!!
    private var _binding: FragmentNewsListBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentNewsListBinding.inflate(inflater, container, false).apply {
            newsListRecyclerView.adapter = adapter
            newsListRefreshLayout.setOnRefreshListener {
                presenter.reloadNewsList()
                adapter.refresh()
            }
        }
        presenter.fetchNewsList()
        collectAdapterState()

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.newsListRecyclerView.adapter = null
        _binding = null
    }

    private fun navigateToDetailScreen(newsId: String) {
        navController.navigate(NewsListFragmentDirections.actionNewsListFragmentToNewsDetailFragment(
            newsId = newsId
        ))
    }

    override suspend fun onPageLoaded(pageData: PagingData<News>) {
        val newsUiState = pageData.map { news ->
            news.toNewsUiState(onClick = { navigateToDetailScreen(newsId = news.id) })
        }

        adapter.submitData(newsUiState)
    }

    private fun showLoading(isLoading: Boolean) = with(binding) {
        newsListRefreshLayout.isRefreshing = isLoading
        newsListLoadingShimmer.run {
            isVisible = isLoading
            if (isLoading) startShimmer() else stopShimmer()
        }
        newsListRecyclerView.isVisible = !isLoading
    }

    private fun collectAdapterState() {
        viewLifecycleOwner.repeatOnLifecycleWhenStarted {
            adapter.loadStateFlow.collect {
                when (val currentState = it.refresh) {
                    LoadState.Loading -> showLoading(isLoading = true)
                    is LoadState.NotLoading -> showLoading(isLoading = false)
                    is LoadState.Error -> {
                        showLoading(isLoading = false)
                        showToast(
                            message = currentState.error.message
                                ?: getString(R.string.error_occurred_while_getting_news)
                        )
                    }
                }
            }
        }
    }
}