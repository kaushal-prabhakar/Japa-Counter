package com.kaushal.japacounter.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.kaushal.japacounter.R
import com.kaushal.japacounter.databinding.FragmentWelcomeBinding
import com.kaushal.japacounter.ext.toast
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class WelcomeFragment : Fragment(), View.OnClickListener {

    private val viewModel by activityViewModels<MainActivityViewModel>()
    private var _binding: FragmentWelcomeBinding? = null
    private val binding: FragmentWelcomeBinding
        get() = _binding!!


    companion object {
        fun newInstance() = WelcomeFragment()

        private const val TAG_SHOW_ADD_NEW_JAPA_DIALOG = "add_new_japa_dialog"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAdd.setOnClickListener(this)
        binding.btnJapaList.setOnClickListener(this)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.addNewJapaOutcome.collectLatest {
                        if (it) {
                           requireContext().toast(getString(R.string.msg_japa_added))
                        } else {
                            requireContext().toast(getString(R.string.err_msg_failure_action))
                        }
                    }
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btnAdd -> {
                AddNewJapaDialog.newInstance().show(parentFragmentManager, TAG_SHOW_ADD_NEW_JAPA_DIALOG)
            }

            R.id.btnJapaList -> {
                val japaListFragment: JapaListFragment = JapaListFragment.newInstance()
                parentFragmentManager.beginTransaction().replace(R.id.container, japaListFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
}