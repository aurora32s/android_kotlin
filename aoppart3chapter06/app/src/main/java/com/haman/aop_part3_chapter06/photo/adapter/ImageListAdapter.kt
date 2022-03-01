package com.haman.aop_part5_chapter03.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.haman.aop_part3_chapter06.databinding.ItemImageListBinding
import com.haman.aop_part5_chapter03.extenstions.loadCenterCrop

class ImageListAdapter(
    var uriList: List<Uri>
): RecyclerView.Adapter<ImageListAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(
        private val binding: ItemImageListBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(uri: Uri) = with(binding) {
            imageView.loadCenterCrop(uri.toString())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            ItemImageListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(uriList[position])
    }

    override fun getItemCount(): Int = uriList.size
}