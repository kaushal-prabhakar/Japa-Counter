package com.kaushal.japacounter.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.kaushal.japacounter.R
import com.kaushal.japacounter.data.Entities
import com.kaushal.japacounter.data.Outcome
import com.kaushal.japacounter.databinding.DialogUpdateJapaBinding
import com.kaushal.japacounter.ext.hideKeyboard
import com.kaushal.japacounter.ext.shakeError
import com.kaushal.japacounter.ext.showMaterialAlert
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class UpdateJapaDialog() : DialogFragment(), View.OnClickListener {

    private var _binding: DialogUpdateJapaBinding? = null
    private val binding: DialogUpdateJapaBinding
        get() = _binding!!


    private val viewModel by activityViewModels<MainActivityViewModel>()

    private lateinit var entities: Entities

    private var dialogRef: Dialog? = null

    companion object {
        fun newInstance() = UpdateJapaDialog()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogUpdateJapaBinding.inflate(inflater, container, false)
        return binding.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.setCancelable(true)
        dialog?.setCanceledOnTouchOutside(true)

        binding.btnAdd.setOnClickListener(this)
        binding.btnDeduct.setOnClickListener(this)
        binding.imgCancel.setOnClickListener(this)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.myJapaDetailsOutcome.collectLatest {
                    if(it is Outcome.Success) {
                        entities = it.data
                        binding.txtCurrentCount.text = entities.currentValue.toString()
                    }
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnAdd -> showUpdateConfirmationDialog { addToCurrentCount() }

            R.id.btnDeduct -> showUpdateConfirmationDialog { deductFromCurrentCount() }

            R.id.imgCancel -> dismissDialog()
        }
    }

    private fun dismissDialog() {
        binding.rootView.hideKeyboard()
        dismiss()
    }

    private fun addToCurrentCount() {
       with(binding.etNewValue) {
           if(!this.text.isNullOrEmpty()) {
               val updatedValue = entities.currentValue.plus(this.text.toString().toInt())
               viewModel.newCountLiveData.value = updatedValue
           } else this.shakeError()
       }
    }

    private fun deductFromCurrentCount() {
        with(binding.etNewValue) {
            if(!this.text.isNullOrEmpty()) {
                val updatedValue = entities.currentValue.minus(this.text.toString().toInt())
                viewModel.newCountLiveData.value = updatedValue
            } else this.shakeError()
        }
    }

    private fun showUpdateConfirmationDialog(transform: () -> Unit) {
        dialogRef?.dismiss()
        dialogRef = requireContext().showMaterialAlert {
            titleResource = R.string.warning_title
            messageResource = R.string.warning_update_counter_message
            negativeButton(R.string.label_cancel) {
                dialogRef?.dismiss()
            }
            positiveButton(R.string.label_yes) {
                transform.invoke()
                dismissDialog()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
