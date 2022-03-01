package com.haman.aop_part3_chapter06.photo

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.haman.aop_part3_chapter06.R
import com.haman.aop_part3_chapter06.databinding.ActivityImageListBinding
import com.haman.aop_part5_chapter03.adapter.ImageListAdapter
import com.haman.aop_part5_chapter03.util.PathUtil
import java.io.File
import java.io.FileNotFoundException

class ImagePreviewListActivity : AppCompatActivity() {

    companion object {
        private const val URI_LIST_KEY = "uriList"

        fun newIntent(activity: Activity, uriList: List<Uri>): Intent {
            return Intent(activity, ImagePreviewListActivity::class.java).apply {
                putExtra(URI_LIST_KEY, ArrayList<Uri>().apply { uriList.forEach { add(it) } })
            }
        }
    }

    private lateinit var binding: ActivityImageListBinding
    private lateinit var adapter: ImageListAdapter

    private val uriList by lazy<List<Uri>> { intent.getParcelableArrayListExtra(URI_LIST_KEY)!! }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() = with(binding) {
        setSupportActionBar(toolbar)
        setupImageList()
    }

    private fun setupImageList() = with(binding) {
        if (::adapter.isInitialized.not())
            adapter = ImageListAdapter(uriList)

        imageViewPager.adapter = adapter
        indicator.setViewPager(imageViewPager)
        imageViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                toolbar.title = getString(
                    R.string.images_page,
                    position + 1,
                    adapter.uriList.size
                )
            }
        })
        deleteButton.setOnClickListener {
            removeImage(uriList[imageViewPager.currentItem])
        }
        confirmButton.setOnClickListener {
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(URI_LIST_KEY, ArrayList<Uri>(adapter.uriList))
            })
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun removeImage(uri: Uri) {
        try {
            val file = File(PathUtil.getPath(this, uri) ?: throw FileNotFoundException())
            file.delete()
            // gallery 에 이미지가 제거되었다는 정보 전달
            MediaScannerConnection.scanFile(
                this,
                arrayOf(file.path),
                arrayOf("image/jpeg"),
                null
            )
            binding.indicator.setViewPager(binding.imageViewPager)

            adapter.uriList.let {
                val imageList = it.toMutableList()
                imageList.remove(uri)
                adapter.uriList = imageList
                adapter.notifyDataSetChanged()
            }

            // 보여줄 리스트가 없는 경우
            if (adapter.uriList.isEmpty()) {
                Toast.makeText(this, "삭제할 수 있는 이미지가 없습니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
        } catch (exception: FileNotFoundException) {
            Toast.makeText(this, "삭제할 이미지가 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}