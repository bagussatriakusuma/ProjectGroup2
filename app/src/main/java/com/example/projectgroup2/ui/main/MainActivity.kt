package com.example.projectgroup2.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.projectgroup2.R
import com.example.projectgroup2.databinding.ActivityMainBinding
import com.example.projectgroup2.ui.main.akun.AkunFragment
import com.example.projectgroup2.ui.main.daftarjual.DaftarJualFragment
import com.example.projectgroup2.ui.main.home.HomeFragment
import com.example.projectgroup2.ui.main.jual.JualFragment
import com.example.projectgroup2.ui.main.notif.NotifFragment
import com.example.projectgroup2.utils.lightStatusBar
import com.example.projectgroup2.utils.setFullScreen
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val homeFragment = HomeFragment()
//        val notifikasiFragment = NotifFragment()
//        val jualFragment = JualFragment()
//        val daftarjualFragment = DaftarJualFragment()
//        val akunFragment = AkunFragment()
//
//        setCurrentFragment(homeFragment)
//
//        binding.bottomNavigationView.setOnItemSelectedListener { id ->
//            when(id){
//                R.id.homeFragment -> setCurrentFragment(homeFragment)
//                R.id.notifFragment -> setCurrentFragment(notifikasiFragment)
//                R.id.jualFragment -> {
//                    setCurrentFragment(jualFragment)
//                    binding.bottomNavigationView.visibility = View.GONE
//                }
//                R.id.daftarJualFragment -> setCurrentFragment(daftarjualFragment)
//            }
//        }

        binding.bottomNavigationView.itemIconTintList = null

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.flfragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.jualFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }
                R.id.loginActivity -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }
                R.id.registerActivity -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }
                R.id.splashActivity2 -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }
                R.id.firstOnBoardingFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }
                R.id.secondOnBoardingFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }
                R.id.thirdOnBoardingFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }
                R.id.detailsFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }
                R.id.previewFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }
                R.id.editProfileFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }
                R.id.editProductFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }
                R.id.infoPenawarFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }
                else -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                }
            }
        }
    }

//    private fun setCurrentFragment(fragment: Fragment) = supportFragmentManager.beginTransaction().apply {
//        replace(R.id.flfragment,fragment)
//        commit()
//    }
}