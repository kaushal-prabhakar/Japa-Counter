package com.kaushal.japacounter.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.kaushal.japacounter.R
import com.kaushal.japacounter.databinding.DialogAddNewJapaBinding
import com.kaushal.japacounter.ext.hideKeyboard
import com.kaushal.japacounter.ext.toast

class AddNewJapaDialog : DialogFragment(), View.OnClickListener {

    private var _binding: DialogAddNewJapaBinding? = null
    private val binding: DialogAddNewJapaBinding
        get() = _binding!!


    private val viewModel by activityViewModels<MainActivityViewModel>()

    companion object {
        fun newInstance() = AddNewJapaDialog()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogAddNewJapaBinding.inflate(inflater, container, false)
        return binding.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)

        binding.btnSave.setOnClickListener(this)
        binding.btnCancel.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnCancel -> dismissDialog()

            R.id.btnSave -> saveJapa()
        }
    }

    private fun dismissDialog() {
        binding.rootView.hideKeyboard()
        dismiss()
    }

    private fun saveJapa() {
        binding.txtJapaName.text?.apply {
            if (this.isNotBlank()) {
                viewModel.addNewJapa(
                    binding.txtJapaName.text.toString(),
                    binding.txtTarget.text.toString()
                )
                dismissDialog()
            } else requireContext().toast(requireContext().getString(R.string.error_name_empty))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
