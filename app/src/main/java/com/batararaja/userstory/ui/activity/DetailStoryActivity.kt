package com.batararaja.userstory.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.batararaja.userstory.ListStoryItem
import com.batararaja.userstory.MainViewModel
import com.batararaja.userstory.StoryEntity
import com.batararaja.userstory.databinding.ActivityDetailStoryBinding
import com.bumptech.glide.Glide

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailStoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val data = intent.getParcelableExtra<StoryEntity>("DATA") as StoryEntity
        showData(data)
    }

    private fun showData(entity: StoryEntity?) {
        Glide.with(this)
            .load(entity?.photoUrl)
            .into(binding.ivDetailPhoto)
        binding.tvDetailName.text = entity?.name
        binding.tvDetailDescription.text = entity?.description
    }
}