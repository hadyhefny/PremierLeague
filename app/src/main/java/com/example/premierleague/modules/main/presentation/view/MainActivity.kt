package com.example.premierleague.modules.main.presentation.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.premierleague.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}