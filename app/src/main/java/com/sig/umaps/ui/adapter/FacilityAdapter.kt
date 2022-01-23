package com.sig.umaps.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sig.umaps.R
import com.sig.umaps.databinding.ListItemDataBinding
import com.sig.umaps.model.FacilitiesResponseItem

class FacilityAdapter(var listBuilding: ArrayList<FacilitiesResponseItem>) : RecyclerView.Adapter<FacilityAdapter.ViewHolder>(), Filterable {

    private var listBuildingFiltered = ArrayList<FacilitiesResponseItem>()

    init {
        listBuildingFiltered.clear()
        listBuildingFiltered.addAll(listBuilding)
    }

    private lateinit var onItemClickCallback: IOnItemClickCallback

    fun setItemClickCallBack(onItemClickCallback: IOnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ViewHolder(private val binding: ListItemDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(building: FacilitiesResponseItem) {
            binding.apply {
                Glide.with(itemView)
                    .load(building.landscapeImageUrl)
                    .into(imgData)
                tvNameData.text = building.name
            }
            itemView.setOnClickListener { onItemClickCallback.onItemClicked(listBuildingFiltered[adapterPosition]) }
            itemView.animation =
                AnimationUtils.loadAnimation(itemView.context, R.anim.recycler_anim)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ListItemDataBinding.inflate(LayoutInflater.from(parent.context), parent, false).run {
            ViewHolder(this)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listBuildingFiltered[position])
    }

    override fun getItemCount(): Int = listBuildingFiltered.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                listBuildingFiltered = if (charSearch.isEmpty()) {
                    listBuilding
                } else {
                    val resultList = ArrayList<FacilitiesResponseItem>()
                    for (row in listBuilding) {
                        if (row.name?.lowercase()?.contains(constraint.toString().lowercase())!!) {
                            resultList.add(row)
                        }
                    }
                    resultList
                }
                val filterResult = FilterResults()
                filterResult.values = listBuildingFiltered
                return filterResult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                listBuildingFiltered = results?.values as ArrayList<FacilitiesResponseItem>
                notifyDataSetChanged()
            }

        }
    }

    interface IOnItemClickCallback {
        fun onItemClicked(facility: FacilitiesResponseItem)
    }
}