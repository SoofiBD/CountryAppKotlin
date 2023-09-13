package com.example.countryappkotlin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.countryappkotlin.databinding.ItemCountryBinding
import com.example.countryappkotlin.model.Model
import com.example.countryappkotlin.util.downloadFromUrl
import com.example.countryappkotlin.util.placeHolderProgressBar
import com.example.countryappkotlin.view.FeedFragmentDirections

class CountryAdapter(val countryList : ArrayList<Model>) : RecyclerView.Adapter<CountryAdapter.ViewHolder>() {

    class ViewHolder(var binding: ItemCountryBinding) : RecyclerView.ViewHolder(binding.root) {
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCountryBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return countryList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.countryName.text = countryList[position].countryName
        holder.binding.countryCapital.text = countryList[position].countryCapital

        holder.binding.imageView.setOnClickListener {
            val action = FeedFragmentDirections.actionFeedFragmentToCountryFragment(countryList[position].uuid)
            Navigation.findNavController(it).navigate(action)
        }
        holder.binding.imageView.downloadFromUrl(countryList[position].imageUrl, placeHolderProgressBar(holder.itemView.context))
    }


    fun updateCountryList(newCountryList : List<Model>){
        countryList.clear()
        countryList.addAll(newCountryList)
        notifyDataSetChanged()
    }
}