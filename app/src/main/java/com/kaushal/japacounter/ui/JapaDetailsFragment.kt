package com.kaushal.japacounter.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.kaushal.japacounter.R
import com.kaushal.japacounter.data.Entities
import com.kaushal.japacounter.data.Outcome
import com.kaushal.japacounter.databinding.FragmentJapaDetailsBinding
import com.kaushal.japacounter.ext.showMaterialAlert
import com.kaushal.japacounter.ext.toast
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class JapaDetailsFragment : Fragment(), View.OnClickListener {

    private val viewModel by activityViewModels<MainActivityViewModel>()
    private var _binding: FragmentJapaDetailsBinding? = null
    private val binding: FragmentJapaDetailsBinding
        get() = _binding!!

    private var dialogRef: Dialog? = null

    companion object {

        private const val keyName = "japaName"

        fun newInstance(name: String) = JapaDetailsFragment().apply {
            arguments = bundleOf(keyName to name)
        }

        private const val TAG_SHOW_UPDATE_DIALOG = "update_counter_dialog"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJapaDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val japaName: String
        get() {
            return requireArguments().getString(keyName).toString()
        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val actionBar = requireActivity().actionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        binding.imgMinus.setOnClickListener(this)
        binding.imgAdd.setOnClickListener(this)
        binding.btnUpdate.setOnClickListener(this)
        binding.btnMarkComplete.setOnClickListener(this)
        binding.btnDelete.setOnClickListener(this)
        binding.textCurrentCount.setOnClickListener(this)

        viewModel.getJapaDetails(japaName)

        val newCountObserver = Observer<Int> { newValue ->
            binding.textCurrentCount.text = newValue.toString()
        }

        viewModel.newCountLiveData.observe(viewLifecycleOwner, newCountObserver)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.myJapaDetailsOutcome.collectLatest {
                        if(it is Outcome.Success) loadJapDetails(it.data)
                    }
                }

                launch {
                    viewModel.updateCounterOutcome.collectLatest {
                        if(it is Outcome.Success) {
                            if(it.data)  {
                                requireContext().toast(getString(R.string.msg_japa_counter_updated))
                                viewModel.getJapaDetails(japaName)
                            }
                        }
                    }
                }

                launch {
                    viewModel.completeJapaOutcome.collectLatest {
                        if(it is Outcome.Success) {
                            requireContext().toast(getString(R.string.msg_japa_completed))
                        } else if (it is Outcome.Failure) {
                            requireContext().toast(getString(R.string.err_msg_failure_action))
                        }
                    }
                }

                launch {
                    viewModel.deleteJapaOutcome.collectLatest {
                        if(it is Outcome.Success) {
                            requireContext().toast(getString(R.string.msg_japa_delete, it.data))
                        } else if (it is Outcome.Failure) {
                            requireContext().toast(getString(R.string.err_msg_failure_action))
                        }
                    }
                }
            }
        }

    }

    private fun loadJapDetails(entities: Entities) {
        with(binding) {
            labelJapaName.text = entities.name
            textCurrentCount.setText(entities.currentValue.toString())
            textGoal.setText(entities.goal.toString())
            textCurrentStatus.text = entities.status.name
            textLastUpdatedTime.text = entities.lastUpdatedTime
            if(entities.incrementValue != 0 || entities.decrementValue != 0) {
                textLastUpdatedValue.text =
                    if (entities.incrementValue > 0) "+" + entities.incrementValue else "-" + entities.decrementValue
            } else textLastUpdatedValue.text = "0"
        }
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            R.id.img_minus -> {
                var currentValue = binding.textCurrentCount.text.toString().toInt()
                if(currentValue > 0) {
                    currentValue -= 1
                    binding.textCurrentCount.setText(currentValue.toString())
                }
            }

            R.id.img_add -> {
                var currentValue = binding.textCurrentCount.text.toString().toInt()
                currentValue += 1
                binding.textCurrentCount.setText(currentValue.toString())
            }

            R.id.text_current_count -> {
                UpdateJapaDialog.newInstance().show(childFragmentManager,
                    TAG_SHOW_UPDATE_DIALOG
                )
            }

            R.id.btnUpdate -> {
                dialogRef?.dismiss()
                dialogRef = requireContext().showMaterialAlert {
                    titleResource = R.string.warning_title
                    messageResource = R.string.warning_update_counter_message
                    negativeButton(R.string.label_cancel) {
                        dialogRef?.dismiss()
                    }
                    positiveButton(R.string.label_yes) {
                        val entity = viewModel.myJapaDetailsOutcome.value
                        if (entity is Outcome.Success) {
                            viewModel.updateJapaCounter(
                                entity.data.name,
                                binding.textCurrentCount.text.toString().toInt(),
                                binding.textGoal.text.toString().toInt()
                            )
                        }
                    }
                }
            }

            R.id.btnMarkComplete -> {
                dialogRef?.dismiss()
                dialogRef = requireContext().showMaterialAlert {
                    titleResource = R.string.warning_title
                    messageResource = R.string.warning_complete_japa
                    negativeButton(R.string.label_cancel) {
                        dialogRef?.dismiss()
                    }
                    positiveButton(R.string.label_yes) {
                        viewModel.completeJapa(japaName)
                    }
                }
            }

            R.id.btnDelete -> {
                dialogRef?.dismiss()
                dialogRef = requireContext().showMaterialAlert {
                    titleResource = R.string.warning_title
                    messageResource = R.string.warning_delete_japa
                    negativeButton(R.string.label_cancel) {
                        dialogRef?.dismiss()
                    }
                    positiveButton(R.string.label_yes) {
                        viewModel.deleteJapa(japaName)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        dialogRef?.dismiss()
        dialogRef = null
        super.onDestroyView()
    }
}