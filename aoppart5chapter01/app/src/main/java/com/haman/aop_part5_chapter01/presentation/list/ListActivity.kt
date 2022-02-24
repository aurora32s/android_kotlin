package com.haman.aop_part5_chapter01.presentation.list

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.haman.aop_part5_chapter01.R
import com.haman.aop_part5_chapter01.databinding.ActivityListBinding
import com.haman.aop_part5_chapter01.presentation.BaseActivity
import com.haman.aop_part5_chapter01.presentation.detail.DetailActivity
import com.haman.aop_part5_chapter01.presentation.detail.DetailMode
import com.haman.aop_part5_chapter01.presentation.view.ToDoListAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.coroutines.CoroutineContext

internal class ListActivity : BaseActivity<ListViewModel>(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()
    override val viewModel: ListViewModel by viewModel()

    private lateinit var binding: ActivityListBinding
    private val adapter = ToDoListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun initViews() = with(binding) {
        recyclerView.layoutManager =
            LinearLayoutManager(this@ListActivity, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter

        refreshLayout.setOnRefreshListener { viewModel.fetchData() }
        addToDoButton.setOnClickListener {
            // TODO DetailActivity 구현
            startActivityForResult(
                DetailActivity.getIntent(this@ListActivity, DetailMode.WRITE),
                DetailActivity.FETCH_REQUEST_CODE
            )
        }
    }

    override fun observeData() {
        viewModel.toDoListLiveData.observe(this) {
            when (it) {
                is ToDoListState.UnInitialized -> {
                    initViews()
                }
                is ToDoListState.Loading -> {
                    handleLoadingStart()
                }
                is ToDoListState.Success -> {
                    handleSuccessState(it)
                }
                is ToDoListState.Error -> {
                    handleErrorState()
                }
            }
        }
    }

    private fun handleErrorState() {
        Toast.makeText(this, "에러가 발생하였습니다.", Toast.LENGTH_LONG).show()
    }

    private fun handleLoadingStart() = with(binding) {
        refreshLayout.isRefreshing = true
    }

    private fun handleSuccessState(state: ToDoListState.Success) = with(binding) {
        refreshLayout.isEnabled = state.toDoList.isNotEmpty()
        refreshLayout.isRefreshing = false

        if (state.toDoList.isEmpty()) {
            emptyResultTextView.isVisible = true
            recyclerView.isVisible = false
        } else {
            emptyResultTextView.isVisible = false
            recyclerView.isVisible = true
            adapter.setToDoList(
                toDoList = state.toDoList,
                toDoItemClickListener = {
                    // TODO DetailActivity 구현
                    startActivityForResult(
                        DetailActivity.getIntent(this@ListActivity, it.id, DetailMode.DETAIL),
                        DetailActivity.FETCH_REQUEST_CODE
                    )
                },
                toDoCheckListener = {
                    viewModel.updateEntity(it)
                }
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_delete_all -> {
                viewModel.deleteAll()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.list_menu, menu)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == DetailActivity.FETCH_REQUEST_CODE &&
                resultCode == Activity.RESULT_OK) {
            viewModel.fetchData()
        }
    }
}