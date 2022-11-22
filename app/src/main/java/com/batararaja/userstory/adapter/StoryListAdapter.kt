package com.batararaja.userstory.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.batararaja.userstory.api.entity.StoryEntity
import com.batararaja.userstory.databinding.ListStoryBinding
import com.batararaja.userstory.ui.activity.DetailStoryActivity
import com.bumptech.glide.Glide

class StoryListAdapter :
    PagingDataAdapter<StoryEntity, StoryListAdapter.ViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: StoryListAdapter.OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ViewHolder(var binding : ListStoryBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data : StoryEntity){
            binding.tvItemName.text = data.name
            binding.tvDescription.text = data.desc
            Glide.with(itemView.context)
                .load(data.url)
                .into(binding.ivItemPhoto)
            itemView.setOnClickListener {
                val intentToDetail = Intent(itemView.context, DetailStoryActivity::class.java)
                val entity = com.batararaja.userstory.StoryEntity(
                    data.id,
                    data.url,
                    data.name,
                    data.desc
                )
                intentToDetail.putExtra("DATA", entity)
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.ivItemPhoto, "picture"),
                        Pair(binding.tvItemName, "name"),
                        Pair(binding.tvDescription, "description")
                    )
                itemView.context.startActivity(intentToDetail, optionsCompat.toBundle())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data =getItem(position)
        if (data!=null){
            holder.bind(data)
        }
//        holder.binding.tvItemName.text = listStory[position].name
//        holder.binding.tvDescription.text = listStory[position].desc
//        Glide.with(holder.itemView.context)
//            .load(listStory[position].url)
//            .into(holder.binding.ivItemPhoto)
//        holder.itemView.setOnClickListener {
//            onItemClickCallback.onItemClicked(listStory[holder.absoluteAdapterPosition])
//            val intentToDetail = Intent(holder.itemView.context, DetailStoryActivity::class.java)
//            val entity = com.batararaja.userstory.StoryEntity(
//                listStory[holder.absoluteAdapterPosition].id,
//                listStory[holder.absoluteAdapterPosition].url,
//                listStory[holder.absoluteAdapterPosition].name,
//                listStory[holder.absoluteAdapterPosition].desc
//            )
//            intentToDetail.putExtra("DATA", entity)
//            val optionsCompat: ActivityOptionsCompat =
//                ActivityOptionsCompat.makeSceneTransitionAnimation(
//                    holder.itemView.context as Activity,
//                    Pair(holder.binding.ivItemPhoto, "picture"),
//                    Pair(holder.binding.tvItemName, "name"),
//                    Pair(holder.binding.tvDescription, "description")
//                )
//            holder.itemView.context.startActivity(intentToDetail, optionsCompat.toBundle())
//        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryEntity>() {
            override fun areItemsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: StoryEntity)
    }
}