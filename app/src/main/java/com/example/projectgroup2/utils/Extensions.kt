package com.example.exercise_project.UI.onBoarding

import android.os.Handler
import androidx.viewpager.widget.ViewPager
import dagger.hilt.android.AndroidEntryPoint

//method ini digunakan untuk scroll otomatis di dalam viewpager dengan interval yg spesifik
fun ViewPager.autoScroll(interval: Long) {

    val handler = Handler()
    var scrollPosition = 0
    val runnable = object : Runnable {
        override fun run() {

            //mengkalkulasi posisi scroll dengan adapter pages count & current value dari scrollPosititon
            val count = adapter?.count ?: 0
            setCurrentItem(scrollPosition++ % count, true)
            handler.postDelayed(this, interval)
        }
    }

    addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

        override fun onPageSelected(position: Int) {
            //update "posisi scroll" jika user menggeser atau scroll secara manual
            scrollPosition = position + 1
        }

        override fun onPageScrollStateChanged(state: Int) {
        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) { }
    })

    handler.post(runnable)
}