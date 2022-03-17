package com.haman.aop_part6_chapter01.widget.adapter.impl

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.haman.aop_part6_chapter01.R
import com.haman.aop_part6_chapter01.databinding.ItemGalleryBinding
import com.haman.aop_part6_chapter01.extension.load
import com.haman.aop_part6_chapter01.model.photo.PhotoModel

class GalleryPhotoListAdapter(
    private val checkPhotoListener: (PhotoModel) -> Unit
): RecyclerView.Adapter<GalleryPhotoListAdapter.PhotoViewHolder>() {

    private var galleryPhotoList: List<PhotoModel> = emptyList()

    inner class PhotoViewHolder(
        private val binding: ItemGalleryBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bindData(model: PhotoModel) = with(binding) {
            photoImageView.load(model.uri.toString(), 8f, CenterCrop())
            checkButton.setImageDrawable(
                ContextCompat.getDrawable(
                    root.context,
                    if (model.isSelected)
                        R.drawable.ic_launcher_background
                    else
                        R.drawable.ic_launcher_foreground
                )
            )
        }

        fun bindView(model: PhotoModel) {
            binding.root.setOnClickListener {
                checkPhotoListener(model)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder(
            ItemGalleryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bindData(galleryPhotoList[position])
        holder.bindView(galleryPhotoList[position])
    }

    override fun getItemCount() = galleryPhotoList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setPhotoList(galleryPhotoList: List<PhotoModel>) {
        this.galleryPhotoList = galleryPhotoList
        notifyDataSetChanged()
    }
}