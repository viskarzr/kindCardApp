package com.example.kindcardapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kindcardapp.databinding.ActivityLogin2Binding
import com.example.kindcardapp.databinding.ActivityPerson2Binding

class ActivityPerson : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPerson2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.buttonSubmitRegister.setOnClickListener {
            onClickMain2()
        }
    }

    private  lateinit var binding: ActivityPerson2Binding
    fun onClickMain2() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}