package com.kaushal.japacounter.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.kaushal.japacounter.R
import com.kaushal.japacounter.data.Entities
import com.kaushal.japacounter.data.Outcome
import com.kaushal.japacounter.databinding.FragmentJapaListBinding
import com.kaushal.japacounter.ext.toast
import com.kaushal.japacounter.ui.adapters.JapaListRecyclerAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class JapaListFragment : Fragment() {

    private val viewModel by activityViewModels<MainActivityViewModel>()
    private var _binding: FragmentJapaListBinding? = null
    private val binding: FragmentJapaListBinding
        get() = _binding!!


    companion object {
        fun newInstance() = JapaListFragment()
    }

    private val onItemSelectedListener: (String) -> Unit = {
        val japaDetailsFragment: JapaDetailsFragment = JapaDetailsFragment.newInstance(it)
        parentFragmentManager.beginTransaction().replace(R.id.container, japaDetailsFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentJapaListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val actionBar = requireActivity().actionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.getMyJapaList()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.myJapaListOutcome.collectLatest {
                        if (it is Outcome.Success) {
                            binding.recyclerView.adapter =
                                JapaListRecyclerAdapter(
                                    it.data as MutableList<Entities>,
                                    onItemSelectedListener
                                )
                        } else if (it is Outcome.Failure) {
                            requireContext().toast(getString(R.string.err_msg_failure_action))
                        }
                    }
                }
            }
        }
    }
}