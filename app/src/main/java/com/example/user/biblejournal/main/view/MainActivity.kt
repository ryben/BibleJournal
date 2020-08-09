package com.example.user.biblejournal.main.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.user.biblejournal.R
import com.example.user.biblejournal.writer.view.WriterFragment

class MainActivity : AppCompatActivity(), WriterFragment.EditNoteListener {
    override fun onBackPressed() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_nav_host_fragment)
        val fragmentWithBackPressedListener = navHostFragment!!.childFragmentManager.fragments.stream().filter { f: Fragment? -> f is MainActivityBackPressListener }.findFirst()
        if (fragmentWithBackPressedListener.isPresent) {
            val listener: MainActivityBackPressListener = fragmentWithBackPressedListener.get() as MainActivityBackPressListener
            listener.onBackPressed()
        } else if (navController.currentDestination!!.id == R.id.noteListFragment) {
            super.moveTaskToBack(false)
        } else {
            navController.popBackStack()
        }
    }

    override fun backPreviousScreen() {
        navController.popBackStack()
    }

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = Navigation.findNavController(this, R.id.main_nav_host_fragment)
    }

}
