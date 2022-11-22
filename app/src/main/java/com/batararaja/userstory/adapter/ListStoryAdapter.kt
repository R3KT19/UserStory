package com.batararaja.userstory.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.batararaja.userstory.ListStoryItem
import com.batararaja.userstory.StoryEntity
import com.batararaja.userstory.databinding.ListStoryBinding
import com.batararaja.userstory.ui.activity.DetailStoryActivity
import com.bumptech.glide.Glide

class ListStoryAdapter (private val listStory : ArrayList<ListStoryItem>) : RecyclerView.Adapter<ListStoryAdapter.ViewHolder>(){
    private lateinit var onItemClickCallback : OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ViewHolder(var binding : ListStoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvItemName.text = listStory[position].name
        holder.binding.tvDescription.text = listStory[position].description
        Glide.with(holder.itemView.context)
            .load(listStory[position].photoUrl)
            .into(holder.binding.ivItemPhoto)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listStory[holder.absoluteAdapterPosition])
            val intentToDetail = Intent(holder.itemView.context, DetailStoryActivity::class.java)
            val entity = StoryEntity(
                listStory[holder.absoluteAdapterPosition].id,
                listStory[holder.absoluteAdapterPosition].photoUrl,
                listStory[holder.absoluteAdapterPosition].name,
                listStory[holder.absoluteAdapterPosition].description
            )
            intentToDetail.putExtra("DATA", entity)
            val optionsCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    holder.itemView.context as Activity,
                    Pair(holder.binding.ivItemPhoto, "picture"),
                    Pair(holder.binding.tvItemName, "name"),
                    Pair(holder.binding.tvDescription, "description")
                )
            holder.itemView.context.startActivity(intentToDetail, optionsCompat.toBundle())
        }
    }

    override fun getItemCount(): Int {
        return listStory.size
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ListStoryItem)
    }
}