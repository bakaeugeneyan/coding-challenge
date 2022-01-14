package com.example.codingchallenge.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.codingchallenge.R
import com.example.codingchallenge.adapters.UsersAdapter
import com.example.codingchallenge.databinding.FragmentHomeBinding
import com.example.codingchallenge.utils.NetworkResult
import com.example.codingchallenge.utils.observeOnce
import com.example.codingchallenge.viewmodel.MainViewModel
import com.example.codingchallenge.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment()
{
    private  var _binding: FragmentHomeBinding? = null
    private val binding get () = _binding!!

    private lateinit var mainViewModel: MainViewModel
    private lateinit var userViewModel: UserViewModel
    private val mAdapter by lazy { UsersAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel  = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =  FragmentHomeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel

        setHasOptionsMenu(true)

        setupRecycleView()
        readDatabase()

        return binding.root
    }

    private fun setupRecycleView(){
        binding.recyclerView.adapter = mAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun readDatabase() {
        showShimmerEffect()
        lifecycleScope.launch {
            mainViewModel.readUserList.observeOnce(viewLifecycleOwner, { database ->
                if (database.isNotEmpty()) {
                    Log.d("homeFragment", "readDatabase called!")
                    mAdapter.setData(database[0].userList)
                    hideShimmerEffect()
                }else {
                    requestApiData()
                }
            })
        }
    }

    private fun requestApiData() {
        Log.d("homeFragment", "requestApiData called!")
        mainViewModel.getItunesList(userViewModel.applyQueries())
        mainViewModel.userResponse.observe(viewLifecycleOwner, { response ->
            when(response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { mAdapter.setData(it) }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }
        })
    }

    private fun showShimmerEffect() {
        binding.recyclerView.showShimmer()
    }

    private fun hideShimmerEffect() {
        binding.recyclerView.hideShimmer()
    }


}