package com.haman.aop_part5_chapter06.presentation.addtrackingitem

import android.app.Activity
import android.app.AlertDialog
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.children
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.haman.aop_part5_chapter06.data.entity.ShippingCompany
import com.haman.aop_part5_chapter06.databinding.FragmentAddTrackingItemBinding
import com.haman.aop_part5_chapter06.extension.toGone
import com.haman.aop_part5_chapter06.extension.toVisible
import org.koin.android.scope.ScopeFragment

class AddTrackingItemFragment : ScopeFragment(), AddTrackingItemsContract.View {

    override val presenter: AddTrackingItemsContract.Presenter by inject()
    private var binding: FragmentAddTrackingItemBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentAddTrackingItemBinding.inflate(inflater)
        .also { binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews()
        presenter.onViewCreated()

        changeInvoiceIfAvailable()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideKeyboard()
        presenter.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    // 택배 회서 정보 요청
    override fun showShippingCompaniesLoadingIndicator() {
        binding?.shippingCompanyProgressBar?.toVisible()
    }

    override fun hideShippingCompaniesLoadingIndicator() {
        binding?.shippingCompanyProgressBar?.toGone()
    }

    // 송장번호 저장
    override fun showSaveTrackingItemIndicator() {
        binding?.saveButton?.apply {
            text = null
            isEnabled = false
        }
        binding?.saveProgressBar?.toVisible()
    }

    override fun hideSaveTrackingItemIndicator() {
        binding?.saveButton?.apply {
            text = "저장하기"
            isEnabled = true
        }
        binding?.saveProgressBar?.toGone()
    }

    // 택배 회사 화면에 표현
    override fun showCompanies(companies: List<ShippingCompany>) {
        companies.forEach { company ->
            binding?.chipGroup?.addView(
                Chip(context).apply {
                    text = company.name
                }
            )
        }
    }

    // 필수 정보를 입력한 경우에만 저장 버튼 클릭 가능
    override fun enableSaveButton() {
        binding?.saveButton?.isEnabled = true
    }

    override fun disableSaveButton() {
        binding?.saveButton?.isEnabled = false
    }

    // 에러 발생
    override fun showErrorToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    // 저장 성공 시, 화면 전환
    override fun finish() {
        findNavController().popBackStack()
    }

    // 추천 택배사 검우
    override fun showRecommendCompanyLoadingIndicator() {
        binding?.shippingCompanyProgressBar?.toVisible()
    }
    override fun hideRecommendCompanyLoadingIndicator() {
        binding?.shippingCompanyProgressBar?.toGone()
    }

    override fun showRecommendCompany(company: ShippingCompany) {
        binding?.chipGroup
            ?.children
            ?.filterIsInstance(Chip::class.java)
            ?.forEach { chip ->
                if (chip.text == company.name) {
                    binding?.chipGroup?.apply { check(chip.id) }
                    return@forEach
                }
            }
    }

    private fun bindViews() {
        // 택배 회사 변경
        binding?.chipGroup?.setOnCheckedChangeListener { group, checkedId ->
            presenter.changeSelectedShippingCompany(group.findViewById<Chip>(checkedId).text.toString())
        }
        // 송장 번호 변경
        binding?.invoiceEditText?.addTextChangedListener { editable ->
            presenter.changeShippingInvoice(editable.toString())
        }
        // 저장하기 버튼 클릭
        binding?.saveButton?.setOnClickListener { _ ->
            presenter.saveTrackingItem()
        }
    }

    /**
     * 클립보드에 저장되어 있는 송장 번호 가져오기
     */
    private fun changeInvoiceIfAvailable() {
        val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val invoice = clipboard.plainTextClip()
        if (!invoice.isNullOrBlank()) {
            AlertDialog.Builder(requireActivity())
                .setTitle("클립 보드에 있는 $invoice 를 송장 번호로 추가하시겠습니까?")
                .setPositiveButton("추가할래요.") { _, _ ->
                    binding?.invoiceEditText?.setText(invoice)
                    presenter.fetchRecommendShippingCompany()
                }
                .setNegativeButton("안 할래요.") { _, _ -> }
                .create()
                .show()
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
    }

    private fun ClipboardManager.plainTextClip(): String? =
        if (hasPrimaryClip() && (primaryClipDescription?.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) == true) {
            primaryClip?.getItemAt(0)?.text?.toString()
        } else {
            null
        }
}