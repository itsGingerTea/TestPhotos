package com.example.unsplashapp.ui.topicsscreen.list

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.unsplashapp.data.models.Topic
import com.example.unsplashapp.databinding.RvTopicsItemBinding
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

class TopicsAdapter(private val onClick: (id: String, title: String, count: Int) -> Unit) :
    PagingDataAdapter<Topic, TopicsAdapter.TopicsViewHolder>(TopicsDiffUtil) {

    inner class TopicsViewHolder(private val item: RvTopicsItemBinding) :
        RecyclerView.ViewHolder(item.root) {

        fun bind(topic: Topic) {
            with(item) {
                Picasso.get()
                    .load(topic.coverPhoto.urls.regular)
                    .networkPolicy(NetworkPolicy.NO_STORE)
                    .placeholder(ColorDrawable(Color.LTGRAY))
                    .into(ivTopic)
                tvTitleTopic.text = topic.title
                tvTotalPhotos.text = topic.totalPhotos.toString()
                ivTopic.setOnClickListener {
                    onClick(topic.id, topic.title, topic.totalPhotos)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TopicsViewHolder(
        item = RvTopicsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: TopicsViewHolder, position: Int) {
        getItem(position).let {
            if (it != null) {
                holder.bind(it)
            }
        }
    }
}