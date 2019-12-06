package com.example.user.biblejournal.core.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.user.biblejournal.R

open class BaseFragment : Fragment() {

    protected lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navController = Navigation.findNavController(activity!!, R.id.main_nav_host_fragment)
    }

    fun navigate(navId : Int) {
        navController.navigate(navId)
    }
}