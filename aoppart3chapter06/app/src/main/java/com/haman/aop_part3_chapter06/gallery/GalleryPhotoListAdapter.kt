package com.haman.aop_part3_chapter06.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.haman.aop_part3_chapter06.R
import com.haman.aop_part3_chapter06.databinding.ItemGalleryBinding
import com.haman.aop_part5_chapter03.extenstions.loadCenterCrop

class GalleryPhotoListAdapter(
    private val checkPhotoListener: (GalleryPhoto) -> Unit
): RecyclerView.Adapter<GalleryPhotoListAdapter.PhotoViewHolder>() {

    private var galleryPhotoList: List<GalleryPhoto> = emptyList()

    inner class PhotoViewHolder(
        private val binding: ItemGalleryBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(data: GalleryPhoto) = with(binding) {
            photoImageView.loadCenterCrop(data.uri.toString(), 8f)
            checkButton.setImageDrawable(
                ContextCompat.getDrawable(
                    root.context,
                    if (data.isSelected)
                        R.drawable.ic_launcher_background
                    else
                        R.drawable.ic_launcher_foreground
                )
            )
            root.setOnClickListener {
                checkPhotoListener(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder(
            ItemGalleryBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(galleryPhotoList[position])
    }

    override fun getItemCount() = galleryPhotoList.size

    fun setPhotoList(galleryPhotoList: List<GalleryPhoto>) {
        this.galleryPhotoList = galleryPhotoList
        notifyDataSetChanged()
    }
}