package com.example.countryappkotlin.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider

import com.example.countryappkotlin.databinding.FragmentFeedBinding
import com.example.countryappkotlin.util.downloadFromUrl
import com.example.countryappkotlin.util.placeHolderProgressBar
import com.example.countryappkotlin.viewModel.FeedViewModel

class FeedFragment : Fragment() {

    private lateinit var viewModel: FeedViewModel
    private var countryUuid: Int? = null

    // ViewBinding için binding sınıfını oluşturun
    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            countryUuid = FeedFragmentArgs.fromBundle(it).countryUuid
        }

        viewModel = ViewModelProvider(this).get(FeedViewModel::class.java)
        viewModel.getDataFromRoom(countryUuid!!)



        observeLiveData()
    }

    fun observeLiveData(){
        viewModel.countryLiveData.observe(viewLifecycleOwner) { country ->
            country?.let {

                
                binding.countryName.text = country.countryName
                binding.countryCapital.text = country.countryCapital
                binding.countryRegion.text = country.countryRegion
                binding.countryCurrency.text = country.countryCurrency
                binding.countryLanguage.text = country.countryLanguage
                binding.countryImageView.downloadFromUrl(country.imageUrl, placeHolderProgressBar(requireContext()))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
