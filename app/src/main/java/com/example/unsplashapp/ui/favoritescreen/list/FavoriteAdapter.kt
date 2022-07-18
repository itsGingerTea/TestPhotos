package com.example.unsplashapp.ui.favoritescreen.list

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.unsplashapp.R
import com.example.unsplashapp.data.models.Photo
import com.example.unsplashapp.databinding.RvPhotolistItemBinding
import com.example.unsplashapp.ui.photolistscreen.PhotosCallback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

class FavoriteAdapter(private val callback: PhotosCallback) :
    ListAdapter<Photo, FavoriteAdapter.FavoriteViewHolder>(FavoriteDiffUtil) {

    inner class FavoriteViewHolder(private val item: RvPhotolistItemBinding) :
        RecyclerView.ViewHolder(item.root) {

        init {
            item.root.setOnClickListener {
                callback.onItemClick(this@FavoriteAdapter.getItem(layoutPosition))
            }
        }

        fun bind(photo: Photo) {
            with(item) {
                itemFavBtn.setOnCheckedChangeListener(null)
                Picasso.get()
                    .load(photo.urls.small)
                    .networkPolicy(NetworkPolicy.NO_STORE)
                    .placeholder(ColorDrawable(Color.LTGRAY))
                    .into(imgItem)
                author.text = photo.user.username
                itemLikes.text = itemView.context.getString(R.string.likes, photo.likes)
                itemFavBtn.isChecked = photo.favorite == true
                itemFavBtn.setOnCheckedChangeListener { buttonView, isChecked ->
                    this@FavoriteAdapter.getItem(layoutPosition).favorite = isChecked
                    callback.onLikeClick(isChecked, this@FavoriteAdapter.getItem(layoutPosition))
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder =
        FavoriteViewHolder(
            item = RvPhotolistItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}