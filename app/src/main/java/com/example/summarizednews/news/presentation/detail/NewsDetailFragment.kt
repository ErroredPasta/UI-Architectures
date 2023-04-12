package com.example.summarizednews.news.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.summarizednews.R
import com.example.summarizednews.core.presentation.showToast
import com.example.summarizednews.databinding.FragmentNewsDetailBinding
import com.example.summarizednews.flux.Dispatcher
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NewsDetailFragment : Fragment() {
    @Inject
    lateinit var store: NewsDetailStore

    private val navArgs by navArgs<NewsDetailFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = FragmentNewsDetailBinding.inflate(inflater, container, false).also { binding ->
        Dispatcher.subscribe(store)

        store.onStateChange { newsDetailState ->
            binding.state = newsDetailState

            newsDetailState.error?.let {
                showToast(message = it.message ?: getString(R.string.error_occurred_while_getting_news))
            }
        }

        Dispatcher.dispatch(action = NewsDetailAction.FetchNewsDetail(id = navArgs.newsId))
    }.root

    override fun onDestroyView() {
        super.onDestroyView()
        Dispatcher.unsubscribe(store)
    }
}