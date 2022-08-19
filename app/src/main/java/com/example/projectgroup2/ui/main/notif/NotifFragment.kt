package com.example.projectgroup2.ui.main.notif

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.projectgroup2.R
import com.example.projectgroup2.data.api.main.category.CategoryResponse
import com.example.projectgroup2.data.api.main.notification.NotifResponse
import com.example.projectgroup2.databinding.FragmentNotifBinding
import com.example.projectgroup2.ui.main.home.adapter.CategoryAdapter
import com.example.projectgroup2.ui.main.home.adapter.ProductAdapter
import com.example.projectgroup2.ui.main.notif.adapter.NotifAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotifFragment : Fragment() {
    private var _binding: FragmentNotifBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NotifViewModel by viewModels()
    private lateinit var notifAdapter: NotifAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotifBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
        bindView()
        viewModel.getNotification()
    }

    private fun bindViewModel(){
        viewModel.showNotif.observe(viewLifecycleOwner){
            notifAdapter.submitData(it)
        }
    }

    private fun bindView(){
        notifAdapter = NotifAdapter(object: NotifAdapter.OnClickListener{
            override fun onClickItem(data: NotifResponse) {
                Toast.makeText(requireContext(), "Notif Id = ${data.id}", Toast.LENGTH_SHORT).show()
            }
        })
        binding.rvNotification.adapter = notifAdapter
    }
}