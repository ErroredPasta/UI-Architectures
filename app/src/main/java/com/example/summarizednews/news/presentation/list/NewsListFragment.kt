package com.example.summarizednews.news.presentation.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.map
import androidx.recyclerview.widget.RecyclerView
import com.example.summarizednews.core.presentation.repeatOnLifecycleWhenStarted
import com.example.summarizednews.databinding.FragmentNewsListBinding
import com.example.summarizednews.news.domain.repository.NewsRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class NewsListFragment : Fragment() {
    private val adapter = NewsListAdapter()
    private val navController by lazy { findNavController() }
    private var recyclerView: RecyclerView? = null

    @Inject
    lateinit var repository: NewsRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = FragmentNewsListBinding.inflate(inflater, container, false).apply {
        newsListRecyclerView.adapter = adapter
        newsListRefreshLayout.setOnRefreshListener {
            repository.reloadNewsList()
            adapter.refresh()
        }
    }.also { binding ->
        recyclerView = binding.newsListRecyclerView

        viewLifecycleOwner.repeatOnLifecycleWhenStarted {
            adapter.loadStateFlow.collectLatest { loadState ->
                when (val currentState = loadState.refresh) {
                    LoadState.Loading -> binding.onLoading()
                    is LoadState.NotLoading -> binding.onSuccess()
                    is LoadState.Error -> binding.onError(cause = currentState.error)
                }
            }
        }

        viewLifecycleOwner.repeatOnLifecycleWhenStarted {
            repository.getNewsList().collectLatest { pagingData ->
                val newsUiState = pagingData.map { news ->
                    news.toNewsUiState(onClick = { navigateToDetailScreen(newsId = news.id) })
                }

                adapter.submitData(newsUiState)
            }
        }
    }.root

    private fun navigateToDetailScreen(newsId: String) {
        navController.navigate(NewsListFragmentDirections.actionNewsListFragmentToNewsDetailFragment(
            newsId = newsId
        ))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerView?.adapter = null
        recyclerView = null
    }
}