package com.batararaja.userstory.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.batararaja.userstory.MainViewModel
import com.batararaja.userstory.Preferences
import com.batararaja.userstory.R
import com.batararaja.userstory.ViewModelFactory
import com.batararaja.userstory.adapter.LoadingStateAdapter
import com.batararaja.userstory.adapter.StoryListAdapter
import com.batararaja.userstory.databinding.ActivityListStoryBinding

class ListStoryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityListStoryBinding
    init {
        instance = this
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvStory.layoutManager = LinearLayoutManager(this)

        getData()

        binding.fabAddStory.setOnClickListener {
            val intent = Intent(this@ListStoryActivity, AddStoryActivity::class.java)
            startActivity(intent)
            finish()
        }

        setupAction()

        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager
    }

    private fun getData() {
        val adapter = StoryListAdapter()

        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )

        val factory: ViewModelFactory = ViewModelFactory.getInstance(baseContext)
        val mainViewModel: MainViewModel by viewModels {
            factory
        }

        mainViewModel.getStory().observe(this, {
            adapter.submitData(lifecycle, it)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.logout -> {
                val preferences = Preferences(this)
                preferences.deleteToken()
                val intent = Intent(this@ListStoryActivity, AuthActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            R.id.map -> {
                val intent = Intent(this@ListStoryActivity, MapsActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }

    private fun setupAction() {
        binding.settingImageView.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
    }

    companion object {
        private var instance: ListStoryActivity? = null

        val context: Context?
            get() = instance
    }
}