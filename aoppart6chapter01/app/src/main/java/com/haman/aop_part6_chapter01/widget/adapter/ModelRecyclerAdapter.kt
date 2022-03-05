package com.haman.aop_part6_chapter01.widget.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.haman.aop_part6_chapter01.model.CellType
import com.haman.aop_part6_chapter01.model.Model
import com.haman.aop_part6_chapter01.screen.base.BaseViewModel
import com.haman.aop_part6_chapter01.util.mapper.ModelViewHolderMapper
import com.haman.aop_part6_chapter01.util.provider.ResourcesProvider
import com.haman.aop_part6_chapter01.widget.adapter.listener.AdapterListener
import com.haman.aop_part6_chapter01.widget.adapter.viewholder.ModelViewHolder

class ModelRecyclerAdapter<M : Model, VM : BaseViewModel>(
    private var modelList: List<Model>,
    private val viewModel: VM,
    private val resourceProvider: ResourcesProvider,
    private val adapterListener: AdapterListener
) : ListAdapter<Model, ModelViewHolder<M>>(Model.DIFF_CALLBACK) {

    override fun getItemCount(): Int = modelList.size

    override fun getItemViewType(position: Int): Int = modelList[position].type.ordinal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModelViewHolder<M> {
        // cell type 에 맞는 viewHolder 반환
        return ModelViewHolderMapper.map(
            parent,
            CellType.values()[viewType],
            viewModel,
            resourceProvider
        )
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: ModelViewHolder<M>, position: Int) {
        with(holder) {
            bindData(modelList[position] as M)
            bindViews(modelList[position] as M, adapterListener)
        }
    }

    override fun submitList(list: List<Model>?) {
        list?.let { modelList = it }
        super.submitList(list)
    }

}