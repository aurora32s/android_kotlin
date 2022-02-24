package com.haman.aop_part5_chapter01.presentation.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.haman.aop_part5_chapter01.R
import com.haman.aop_part5_chapter01.data.entity.ToDoEntity
import com.haman.aop_part5_chapter01.databinding.ItemTodoBinding

class ToDoListAdapter: RecyclerView.Adapter<ToDoListAdapter.ToDoViewHolder>() {

    private var toDoList: List<ToDoEntity> = emptyList()
    private lateinit var toDoItemClickListener: (ToDoEntity) -> Unit
    private lateinit var toDoCheckListener: (ToDoEntity) -> Unit

    inner class ToDoViewHolder(
        val binding: ItemTodoBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(toDoEntity: ToDoEntity) {
            binding.checkBox.text = toDoEntity.title
            checkToComplete(toDoEntity.hasCompleted)

            binding.checkBox.setOnClickListener {
                toDoCheckListener(
                    toDoEntity.copy(hasCompleted = binding.checkBox.isChecked)
                )
                checkToComplete(binding.checkBox.isChecked)
            }
            binding.root.setOnClickListener {
                toDoItemClickListener(toDoEntity)
            }
        }

        private fun checkToComplete(isChecked: Boolean) = with(binding) {
            checkBox.isChecked = isChecked
            root.setBackgroundColor(
                ContextCompat.getColor(
                    root.context,
                    if (isChecked) R.color.gray_300 else R.color.white
                )
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        return ToDoViewHolder(
            ItemTodoBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.bind(toDoList[position])
    }

    override fun getItemCount(): Int = toDoList.size

    fun setToDoList(
        toDoList: List<ToDoEntity>,
        toDoItemClickListener: (ToDoEntity)->Unit,
        toDoCheckListener: (ToDoEntity)-> Unit
    ) {
        this.toDoList = toDoList
        this.toDoItemClickListener = toDoItemClickListener
        this.toDoCheckListener = toDoCheckListener
    }
}