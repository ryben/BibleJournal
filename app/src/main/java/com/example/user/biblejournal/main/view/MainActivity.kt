package com.example.user.biblejournal.main.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.user.biblejournal.R
import com.example.user.biblejournal.writer.view.WriterFragment

class MainActivity : AppCompatActivity(), WriterFragment.EditNoteListener {
    override fun backPreviousScreen() {

    }

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = Navigation.findNavController(this, R.id.main_nav_host_fragment)
    }

}
