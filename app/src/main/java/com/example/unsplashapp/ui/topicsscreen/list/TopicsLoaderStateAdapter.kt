package com.example.unsplashapp.ui.topicsscreen.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.unsplashapp.R
import com.example.unsplashapp.databinding.ItemErrorBinding
import com.example.unsplashapp.databinding.ItemProgressBinding

class TopicsLoaderStateAdapter(private val retryCallback: () -> Unit) :
    LoadStateAdapter<TopicsLoaderStateAdapter.ItemViewHolder>() {

    override fun getStateViewType(loadState: LoadState): Int =
        when (loadState) {
            is LoadState.NotLoading -> error("Not Supported")
            LoadState.Loading -> PROGRESS
            is LoadState.Error -> ERROR
        }

    override fun onBindViewHolder(holder: ItemViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ItemViewHolder {
        return when (loadState) {
            LoadState.Loading -> ProgressViewHolder(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            is LoadState.Error -> ErrorViewHolder(
                LayoutInflater.from(parent.context),
                parent,
                false,
                retryCallback,
                parent.context
            )
            is LoadState.NotLoading -> error("Not Supported")
        }
    }

    private companion object {
        private const val ERROR = 1
        private const val PROGRESS = 0
    }

    abstract class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(loadState: LoadState)
    }

    class ProgressViewHolder internal constructor(
        private val binding: ItemProgressBinding
    ) : ItemViewHolder(binding.root) {

        override fun bind(loadState: LoadState) {
            //
        }

        companion object {
            operator fun invoke(
                layoutInflater: LayoutInflater,
                parent: ViewGroup? = null,
                attachToRoot: Boolean = false
            ): ProgressViewHolder {
                return ProgressViewHolder(
                    ItemProgressBinding.inflate(
                        layoutInflater,
                        parent,
                        attachToRoot
                    )
                )
            }
        }
    }

    class ErrorViewHolder internal constructor(
        private val context: Context?,
        private val binding: ItemErrorBinding,
        private val retryCallback: () -> Unit
    ) : ItemViewHolder(binding.root) {

        init {
            binding.retryButton.setOnClickListener {
                retryCallback()
            }
        }

        override fun bind(loadState: LoadState) {
            require(loadState is LoadState.Error)
            binding.errorMessage.text = context?.resources?.getString(R.string.error_title)
        }

        companion object {
            operator fun invoke(
                layoutInflater: LayoutInflater,
                parent: ViewGroup? = null,
                attachToRoot: Boolean = false,
                retryCallback: () -> Unit,
                context: Context?
            ): ErrorViewHolder {
                return ErrorViewHolder(
                    context,
                    ItemErrorBinding.inflate(
                        layoutInflater,
                        parent,
                        attachToRoot
                    ),
                    retryCallback
                )
            }
        }
    }
}