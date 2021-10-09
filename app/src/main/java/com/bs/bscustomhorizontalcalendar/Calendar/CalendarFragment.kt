package com.bs.bscustomhorizontalcalendar.Calendar

import android.widget.AdapterView
import android.widget.GridView
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bs.bscustomhorizontalcalendar.R
import android.os.Build
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import org.joda.time.Chronology
import org.joda.time.DateTime
import org.joda.time.chrono.ISOChronology
import org.joda.time.LocalDate
import java.lang.ClassCastException
import java.lang.Exception
import java.util.ArrayList

/*
created by bilawal sharif 11349
 */   class CalendarFragment : Fragment(), AdapterView.OnItemClickListener {
    var year = 0
    var month = 0
    var identifier: String? = null
    var array: ArrayList<String>? = null
    var gridView: GridView? = null
    var mListener: OnItemClickListener? = sDummyCallbacks
    var role: String? = null
    var calendar: Calendar? = null
    var calendar_id = 0

    interface OnItemClickListener {
        fun OnItemClicked(position: Int)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val res = resources
        identifier = requireArguments().getString("identifier")
        if (identifier == null) {
            throw ClassCastException("Calendar in " + requireActivity().localClassName + " must have a identifier attribute")
        }
        calendar_id = res.getIdentifier(identifier, "id", requireContext().packageName)
        calendar = requireActivity().findViewById<View>(calendar_id) as Calendar
        year = requireArguments().getInt("year", -1)
        month = requireArguments().getInt("month", -1)
        role = requireArguments().getString("role")
        mListener = calendar
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_month, container, false)
        array = ArrayList()
        gridView = v.findViewById<View>(R.id.gridview) as GridView
        gridView!!.setBackgroundColor(calendar!!.expandedCalendarBackgroundColor)
        val dt = DateTime().withYear(year).withMonthOfYear(month)
        val start = dt.withDayOfMonth(1).withTimeAtStartOfDay()
        val end = start.plusMonths(1).minusMillis(1)
        val startDay = LocalDate(start)
        val endDay = LocalDate(end)
        for (i in 0..48) {
            array!!.add(" ")
        }
        array!![0] = "S"
        array!![1] = "M"
        array!![2] = "T"
        array!![3] = "W"
        array!![4] = "T"
        array!![5] = "F"
        array!![6] = "S"
        var i = 0
        when (startDay.dayOfWeek) {
            7 -> i = 7
            1 -> i = 8
            2 -> i = 9
            3 -> i = 10
            4 -> i = 11
            5 -> i = 12
            6 -> i = 13
        }
        var value = 1
        for (j in i until getDaysInMonth(year, month) + i) {
            array!![j] = value++.toString() + ""
        }
        val aa = CalendarMonthGridViewAdapter(
            v.context,
            R.layout.calendar_list_item,
            array!!,
            year,
            month,
            calendar!!
        )
        gridView!!.adapter = aa
        gridView!!.onItemClickListener = this
        return v
    }

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        for (i in 0 until parent.childCount) {
            parent.getChildAt(i).background = null
        }
        try {
            val a = array!![position].toInt()
            val mDrawable = ContextCompat.getDrawable(
                requireActivity().applicationContext,
                R.drawable.background_green_ring
            )
            val sdk = Build.VERSION.SDK_INT
            if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                view.setBackgroundDrawable(mDrawable)
            } else {
                view.background = mDrawable
            }
            mListener!!.OnItemClicked(a)
        } catch (e: Exception) {
        }
    }

    companion object {
        fun newInstance(
            year: Int,
            month: Int,
            role: String?,
            identifier: String?
        ): CalendarFragment {
            val myFragment = CalendarFragment()
            val args = Bundle()
            args.putInt("year", year)
            args.putInt("month", month)
            args.putString("role", role)
            args.putString("identifier", identifier)
            myFragment.arguments = args
            return myFragment
        }

        fun getDaysInMonth(year: Int, month: Int): Int {
            val chrono: Chronology = ISOChronology.getInstance()
            val dayField = chrono.dayOfMonth()
            val monthDate = LocalDate(year, month, 1)
            return dayField.getMaximumValue(monthDate)
        }

        private val sDummyCallbacks: OnItemClickListener = object : OnItemClickListener {
            override fun OnItemClicked(position: Int) {}
        }
    }
}