package com.batararaja.userstory.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.batararaja.userstory.ListStoryItem
import com.batararaja.userstory.MainViewModel
import com.batararaja.userstory.Preferences
import com.batararaja.userstory.R
import com.batararaja.userstory.adapter.ListStoryAdapter
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

        val mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java)

        mainViewModel.getStory()
        mainViewModel.listStory.observe(this, { data ->
            if (data != null) {
                setAdapterData(data)
            }
        })

        mainViewModel.isLoading.observe(this, {
            showLoading(it)
        })

        mainViewModel.message.observe(this, {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(
                    this,
                    it,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        binding.fabAddStory.setOnClickListener {
            val intent = Intent(this@ListStoryActivity, AddStoryActivity::class.java)
            startActivity(intent)
        }

        setupAction()

        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager
    }

    override fun onResume() {
        super.onResume()
        Thread.sleep(100)
        val mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java)
        mainViewModel.getStory()
        mainViewModel.listStory.observe(this, { data ->
            setAdapterData(data)
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
        }
        return true
    }

    private fun setAdapterData(data: List<ListStoryItem>) {
        val listStory = ArrayList<ListStoryItem>()
        for (story in data) {
            listStory.add(story)
        }
        val adapter = ListStoryAdapter(listStory)
        binding.rvStory.setHasFixedSize(true)
        binding.rvStory.adapter = adapter
        adapter.setOnItemClickCallback(object : ListStoryAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListStoryItem) {

            }

        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setupAction() {
        binding.settingImageView.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
    }

    companion object {
        private const val TAG = "ListStoryActivity"
        private var instance: ListStoryActivity? = null

        val context: Context?
            get() = instance
    }
}