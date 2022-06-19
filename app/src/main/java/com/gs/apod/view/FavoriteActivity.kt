package com.gs.apod.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.gs.apod.databinding.ActivityFavoriteBinding
import com.gs.apod.viewmodel.NasaApodViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private val nasaApodViewModel: NasaApodViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);

        binding.recyclerview.layoutManager = LinearLayoutManager(this)

        nasaApodViewModel.getFavoriteList().observe(this) {
            binding.recyclerview.adapter = FavoriteAdapter(it)
        }
    }
}