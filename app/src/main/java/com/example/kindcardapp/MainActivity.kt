package com.example.kindcardapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kindcardapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.buttonStats.setOnClickListener {
            onClickStatus()
        }
        binding.buttonRegister.setOnClickListener {
            onClickLogin()
        }
        val toolbar = findViewById<Toolbar>(R.id.toolbar2)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Kind Card"

        val helper = NotificationHelper(this)
        helper.createChannels()

        binding.buttonStats.setOnClickListener {
            helper.showHeadsUpNotification(
                "Статистика!",
                "Продолжайте изучение слов!\nВы близки к успеху!"
            )
        }

    }

    private  lateinit var binding: ActivityMainBinding

    fun onClickStatus() {
        val intent = Intent(this, ActivityStatistic::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
    }

    fun onClickLogin() {
        val intent = Intent(this, ActivityLogin::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

//    fun onClickStatus(view: View) {
//        val intent = Intent(this, ActivityStatistic::class.java)
////        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
//        startActivity(intent)
//    }

//    Разработать переход на карточки
}