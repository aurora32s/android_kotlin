package com.haman.aop_part3_chapter06.photo.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.haman.aop_part3_chapter06.databinding.ItemPhotoBinding
import com.haman.aop_part5_chapter03.extenstions.loadCenterCrop

class PhotoListAdapter(
    private val removePhotoListener: (Uri) -> Unit
): RecyclerView.Adapter<PhotoListAdapter.PhotoViewHolder>() {

    private var imageUriList: List<Uri> = listOf()

    inner class PhotoViewHolder(
        private val binding: ItemPhotoBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Uri) = with(binding) {
            photoImageView.loadCenterCrop(data.toString(), 8f)
            closeButton.setOnClickListener {
                removePhotoListener(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder(
            ItemPhotoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(imageUriList[position])
    }

    override fun getItemCount(): Int = imageUriList.size

    fun setPhotoList(imageUriList: List<Uri>) {
        this.imageUriList = imageUriList
        notifyDataSetChanged()
    }
}