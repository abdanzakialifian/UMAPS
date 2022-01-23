package com.sig.umaps.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.sig.umaps.databinding.ImageSliderBinding
import com.smarteist.autoimageslider.SliderViewAdapter

class SliderImageAdapter : SliderViewAdapter<SliderImageAdapter.ViewHolder>() {

    private var sliderItems = ArrayList<Int>()

    fun renewItems(sliderItems: ArrayList<Int>) {
        this.sliderItems = sliderItems
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ImageSliderBinding) :
        SliderViewAdapter.ViewHolder(binding.root) {
        val image = binding.imgSliderLayout
    }

    override fun getCount(): Int = sliderItems.size

    override fun onCreateViewHolder(parent: ViewGroup?): ViewHolder =
        ImageSliderBinding.inflate(LayoutInflater.from(parent?.context), parent, false).run {
            ViewHolder(this)
        }


    override fun onBindViewHolder(viewHolder: ViewHolder?, position: Int) {
        Glide.with(viewHolder?.itemView?.context!!)
            .load(sliderItems[position])
            .into(viewHolder.image)
    }
}