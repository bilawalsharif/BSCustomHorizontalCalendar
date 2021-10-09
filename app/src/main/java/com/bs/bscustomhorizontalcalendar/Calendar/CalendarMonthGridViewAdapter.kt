package com.bs.bscustomhorizontalcalendar.Calendar

import android.content.Context
import android.widget.BaseAdapter
import android.widget.TextView
import android.view.ViewGroup
import android.view.LayoutInflater
import com.bs.bscustomhorizontalcalendar.R
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import org.joda.time.LocalDate
import java.lang.Exception
import java.util.ArrayList

class CalendarMonthGridViewAdapter(
    private val context: Context,
    resource: Int,
    objects: ArrayList<String>,
    year: Int,
    month: Int,
    calendar: Calendar
) : BaseAdapter() {
    private var list = ArrayList<String>()
    var year: Int
    var month: Int
    var calendar: Calendar

    internal inner class ViewHolder {
        var v: TextView? = null
        var dot: TextView? = null
    }

    override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        var convertView = convertView
        var holder: ViewHolder? = null
        if (convertView == null) {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.calendar_list_item, null)
            holder = ViewHolder()
            holder!!.v = convertView.findViewById<View>(R.id.text) as TextView
            holder.dot = convertView.findViewById<View>(R.id.dot) as TextView
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        val s = list[position]
        var a: Int
        holder!!.dot!!.visibility = View.INVISIBLE
        try {
            a = s.toInt()
        } catch (e: Exception) {
            a = -1
            holder.dot!!.visibility = View.INVISIBLE
        }
        if (a != -1) {
            try {
                val currentSelectedDate = LocalDate(year.toString() + "-" + month + "-" + s)
                if (calendar.getEventDates()!!.contains(currentSelectedDate)) {
                    holder.dot!!.visibility = View.VISIBLE
                } else {
                    holder.dot!!.visibility = View.INVISIBLE
                }
                if (currentSelectedDate.isEqual(calendar.selected)) {
                    val mDrawable =
                        ContextCompat.getDrawable(context, R.drawable.background_green_ring)
                    val sdk = Build.VERSION.SDK_INT
                    if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                        convertView.setBackgroundDrawable(mDrawable)
                    } else {
                        convertView.background = mDrawable
                    }
                }
            } catch (e: Exception) {
                Log.d("Exception", e.stackTrace.toString())
            }
        } else {
            holder.dot!!.visibility = View.INVISIBLE
        }
        holder.v!!.text = s
        return convertView
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    init {
        list = objects
        this.year = year
        this.month = month
        this.calendar = calendar
    }
}