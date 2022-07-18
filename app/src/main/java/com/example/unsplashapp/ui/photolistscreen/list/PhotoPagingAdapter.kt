package com.example.unsplashapp.ui.photolistscreen.list

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.unsplashapp.R
import com.example.unsplashapp.data.models.Photo
import com.example.unsplashapp.databinding.RvPhotolistItemBinding
import com.example.unsplashapp.ui.photolistscreen.PhotosCallback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

class PhotoPagingAdapter(private val callback: PhotosCallback) :
    PagingDataAdapter<Photo, PhotoPagingAdapter.PhotoViewHolder>(PhotoPagingDiffUtil()) {

    private var flagShowLike: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PhotoViewHolder(
        item = RvPhotolistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    inner class PhotoViewHolder(private val item: RvPhotolistItemBinding) :
        RecyclerView.ViewHolder(item.root) {

        init {
            item.root.setOnClickListener {
                this@PhotoPagingAdapter.getItem(layoutPosition)
                    ?.let { photo -> callback.onItemClick(photo) }
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
                itemFavBtn.isVisible = flagShowLike
                itemFavBtn.isChecked = photo.favorite == true
            }
            item.itemFavBtn.setOnCheckedChangeListener { compoundButton, b ->
                this@PhotoPagingAdapter.getItem(layoutPosition)?.favorite = b
                this@PhotoPagingAdapter.getItem(layoutPosition)?.let { callback.onLikeClick(b, it) }
            }
        }
    }

    fun addFlag(flagShowLike: Boolean) {
        this.flagShowLike = flagShowLike
    }
}
