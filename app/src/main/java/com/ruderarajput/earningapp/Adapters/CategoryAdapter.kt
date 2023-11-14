package com.ruderarajput.earningapp.Adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ruderarajput.earningapp.Activity.QuizActivity
import com.ruderarajput.earningapp.Models.categoryModelClass
import com.ruderarajput.earningapp.databinding.CategoryItemBinding

class CategoryAdapter(
    var categoryList: ArrayList<categoryModelClass>,
    var requireActivity: FragmentActivity
):RecyclerView.Adapter<CategoryAdapter.MyCategoryViewHolder>() {
    class MyCategoryViewHolder(var binding:CategoryItemBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCategoryViewHolder {
       return MyCategoryViewHolder(CategoryItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount()=categoryList.size

    override fun onBindViewHolder(holder: MyCategoryViewHolder, position: Int) {
        var datalist=categoryList[position]
        holder.binding.cardImage.setImageResource(datalist.catImage)
        holder.binding.categoryText.text=datalist.catText

        holder.binding.categoryBtn.setOnClickListener {
            var intent=Intent(requireActivity,QuizActivity::class.java)
            intent.putExtra("categoryImage",datalist.catImage)
            intent.putExtra("questionsType",datalist.catText)
            requireActivity.startActivity(intent)
        }
    }
}

