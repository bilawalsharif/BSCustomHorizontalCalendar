package com.bs.bscustomhorizontalcalendar.Calendar

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bs.bscustomhorizontalcalendar.Calendar.CalendarFragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter

class CalendarPagerAdapter(
    fragmentManager: FragmentManager?,
    private val fragments: List<CalendarFragment>
) : FragmentStatePagerAdapter(
    fragmentManager!!
) {
    private val updatableView: View? = null
    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }
}