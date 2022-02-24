package com.haman.aop_part5_chapter01.presentation.detail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import com.haman.aop_part5_chapter01.databinding.ActivityDetailBinding
import com.haman.aop_part5_chapter01.presentation.BaseActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.coroutines.CoroutineContext

internal class DetailActivity: BaseActivity<DetailViewModel>(), CoroutineScope{
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()
    override val viewModel: DetailViewModel by viewModel {
        parametersOf(
            intent.getSerializableExtra(DETAIL_MODE_KEY),
            intent.getLongExtra(TODO_ID_KEY, -1L)
        )
    }

    companion object {
        const val TODO_ID_KEY = "ToDoId"
        const val DETAIL_MODE_KEY = "DetailMode"

        const val FETCH_REQUEST_CODE = 10

        fun getIntent(context: Context, detailMode: DetailMode) =
            Intent(context, DetailActivity::class.java).apply {
                putExtra(DETAIL_MODE_KEY, detailMode)
            }

        fun getIntent(context: Context, id: Long, detailMode: DetailMode) =
            Intent(context, DetailActivity::class.java).apply {
                putExtra(TODO_ID_KEY, id)
                putExtra(DETAIL_MODE_KEY, detailMode)
            }
    }

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setResult(Activity.RESULT_OK)
    }

    private fun initViews() = with(binding) {
        titleInput.isEnabled = false
        descriptionInput.isEnabled = false

        deleteButton.isVisible = false
        modifyButton.isVisible = false
        updateButton.isVisible = false

        deleteButton.setOnClickListener {
            viewModel.deleteToDo()
        }
        modifyButton.setOnClickListener {
            viewModel.setModifyMode()
        }
        updateButton.setOnClickListener {
            viewModel.writeToDo(
                title = titleInput.text.toString(),
                description = descriptionInput.text.toString()
            )
        }
    }

    override fun observeData() {
        viewModel.toDoDetailLiveData.observe(this) {
            when(it) {
                ToDoDetailState.UnIntialized -> {
                    initViews()
                }
                ToDoDetailState.Loading -> {
                    handleLoadingState()
                }
                is ToDoDetailState.Success -> {
                    handleSuccessState(it)
                }
                ToDoDetailState.Modify -> {
                    handleModifyState()
                }
                ToDoDetailState.Delete -> {
                    Toast.makeText(this, "성공적으로 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                    finish()
                }
                ToDoDetailState.Error -> {
                    Toast.makeText(this, "에러가 발생하였습니다.", Toast.LENGTH_SHORT).show()
                    finish()
                }
                ToDoDetailState.Write -> {
                    handleWriteState()
                }
            }
        }
    }

    private fun handleLoadingState() {
        binding.progressbar.isVisible = true
    }

    private fun handleSuccessState(it: ToDoDetailState.Success) = with(binding) {
        binding.progressbar.isVisible = false

        titleInput.isEnabled = false
        descriptionInput.isEnabled = false

        deleteButton.isVisible = true
        modifyButton.isVisible = true
        updateButton.isVisible = false

        val toDoItem = it.todoItem
        titleInput.setText(toDoItem.title)
        descriptionInput.setText(toDoItem.description)
    }

    private fun handleModifyState() = with(binding) {
        titleInput.isEnabled = true
        descriptionInput.isEnabled = true

        deleteButton.isVisible = false
        modifyButton.isVisible = false
        updateButton.isVisible = true
    }

    private fun handleWriteState() = with(binding) {
        titleInput.isEnabled = true
        descriptionInput.isEnabled = true

        updateButton.isVisible = true
    }
}