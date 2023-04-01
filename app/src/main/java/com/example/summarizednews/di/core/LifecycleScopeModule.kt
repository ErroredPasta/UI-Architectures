package com.example.summarizednews.di.core

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import kotlinx.coroutines.CoroutineScope

@Module
@InstallIn(FragmentComponent::class)
object LifecycleScopeModule {
    @Provides
    fun provideViewLifecycleScope(fragment: Fragment): CoroutineScope =
        fragment.viewLifecycleOwner.lifecycleScope
}