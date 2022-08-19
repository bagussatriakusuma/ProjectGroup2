package com.example.projectgroup2.ui.onboarding.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class OnBoardingAdapter(fragmentManager: FragmentManager, val listFragment: List<Fragment>):
    FragmentStatePagerAdapter(fragmentManager) {
    override fun getCount(): Int {
        return listFragment.size
    }

    override fun getItem(position: Int): Fragment {
        return listFragment[position]
    }
}