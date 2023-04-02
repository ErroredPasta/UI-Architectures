package com.example.summarizednews.news.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.summarizednews.R
import com.example.summarizednews.core.presentation.repeatOnLifecycleWhenStarted
import com.example.summarizednews.core.presentation.showToast
import com.example.summarizednews.databinding.FragmentNewsDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class NewsDetailFragment : Fragment() {
    private val viewModel by viewModels<NewsDetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = FragmentNewsDetailBinding.inflate(inflater, container, false).apply {
        viewModel = this@NewsDetailFragment.viewModel
        lifecycleOwner = this@NewsDetailFragment.viewLifecycleOwner
    }.also {
        viewLifecycleOwner.repeatOnLifecycleWhenStarted {
            viewModel.error.collectLatest { cause ->
                if (cause == null) return@collectLatest

                showToast(message = cause.message ?: getString(R.string.error_occurred_while_getting_news))
                viewModel.errorHandlingDone()
            }
        }
    }.root
}