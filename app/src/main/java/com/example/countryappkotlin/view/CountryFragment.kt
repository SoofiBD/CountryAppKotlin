package com.example.countryappkotlin.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.countryappkotlin.R
import com.example.countryappkotlin.adapter.CountryAdapter
import com.example.countryappkotlin.databinding.FragmentCountryBinding
import com.example.countryappkotlin.viewModel.CountryViewModel

class CountryFragment : Fragment() {

    private lateinit var viewModel: CountryViewModel
    private val countryAdapter = CountryAdapter(arrayListOf())

    // ViewBinding için binding sınıfını oluşturun
    private var _binding: FragmentCountryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCountryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(CountryViewModel::class.java)
        viewModel.refreshData()

        binding.countryList.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        binding.countryList.adapter = countryAdapter

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.countryList.visibility = View.GONE
            binding.errorMessage.visibility = View.GONE
            binding.progressBarCountry.visibility = View.VISIBLE
            viewModel.refreshFromAPI()
            binding.swipeRefreshLayout.isRefreshing = false
        }

        observeLiveData()
    }

    fun observeLiveData() {
        viewModel.countries.observe(viewLifecycleOwner) { countries ->
            countries?.let {
                binding.countryList.visibility = View.VISIBLE
                countryAdapter.updateCountryList(countries)
            }
        }

        viewModel.countryLoadError.observe(viewLifecycleOwner) { isError ->
            isError?.let {
                if (it) {
                    binding.errorMessage.visibility = View.VISIBLE
                } else {
                    binding.errorMessage.visibility = View.GONE
                }
            }

            viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
                isLoading?.let {
                    if (it) {
                        binding.progressBarCountry.visibility = View.VISIBLE
                        binding.errorMessage.visibility = View.GONE
                        binding.countryList.visibility = View.GONE
                    } else {
                        binding.progressBarCountry.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
