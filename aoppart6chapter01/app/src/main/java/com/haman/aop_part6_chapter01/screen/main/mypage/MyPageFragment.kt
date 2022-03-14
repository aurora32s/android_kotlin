package com.haman.aop_part6_chapter01.screen.main.mypage

import com.haman.aop_part6_chapter01.databinding.FragmentMypageBinding
import com.haman.aop_part6_chapter01.screen.base.BaseFragment
import com.haman.aop_part6_chapter01.screen.main.home.HomeFragment
import org.koin.android.viewmodel.ext.android.viewModel

class MyPageFragment: BaseFragment<MyPageViewModel, FragmentMypageBinding>() {
    override val viewModel: MyPageViewModel by viewModel()

    override fun getViewBinding(): FragmentMypageBinding =
        FragmentMypageBinding.inflate(layoutInflater)

    override fun observeData() {
    }

    companion object {
        const val TAG = "MyPageFragment"
        fun newInstance() = MyPageFragment()
    }
}