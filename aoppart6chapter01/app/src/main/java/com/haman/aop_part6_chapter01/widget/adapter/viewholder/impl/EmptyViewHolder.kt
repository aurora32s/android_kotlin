package com.haman.aop_part6_chapter01.widget.adapter.viewholder.impl

import com.haman.aop_part6_chapter01.databinding.ViewholderEmptyBinding
import com.haman.aop_part6_chapter01.model.Model
import com.haman.aop_part6_chapter01.screen.base.BaseViewModel
import com.haman.aop_part6_chapter01.util.provider.ResourcesProvider
import com.haman.aop_part6_chapter01.widget.adapter.listener.AdapterListener
import com.haman.aop_part6_chapter01.widget.adapter.viewholder.ModelViewHolder

class EmptyViewHolder(
    private val binding: ViewholderEmptyBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
): ModelViewHolder<Model>(binding, viewModel, resourcesProvider) {
    override fun reset() = Unit
    override fun bindViews(model: Model, adapterListener: AdapterListener) = Unit
}