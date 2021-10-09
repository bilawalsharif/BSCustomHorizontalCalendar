package com.bs.bscustomhorizontalcalendar

import androidx.appcompat.app.AppCompatActivity
import com.bs.bscustomhorizontalcalendar.Calendar.Calendar.CalendarListener
import android.widget.TextView
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bs.bscustomhorizontalcalendar.Calendar.Calendar
import org.joda.time.LocalDate

class MainActivity : AppCompatActivity(), CalendarListener {
    private var calendar: Calendar? = null
    private var txtMonthName: TextView? = null
    private var txtYear: TextView? = null
    private var ivLeft: ImageView? = null
    private var ivRight: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        calendar = findViewById(R.id.customCalendar)
        txtMonthName = findViewById(R.id.txtMonthName)
        txtYear = findViewById(R.id.txtYear)
        ivLeft = findViewById(R.id.ivLeft)
        ivRight = findViewById(R.id.ivRight)
        calendar!!.calendarListener = this
        ivRight!!.setOnClickListener({ v: View? -> calendar!!.stepsforword() })
        ivLeft!!.setOnClickListener({ v: View? -> calendar!!.stepsbackword() })
    }

    override fun onExpand() {
    }

    override fun onCollapse() {
    }

    override fun onDateChange(date: LocalDate?) {
        txtMonthName!!.text = date!!.monthOfYear().asText
        txtYear!!.text = date.year.toString()
    }

    override fun onTodayClick(today: LocalDate?) {
    Toast.makeText(applicationContext,today.toString(),Toast.LENGTH_SHORT).show()
    }

    override fun onCalendarScroll(date: LocalDate?, newState: Int, position: Int, itemId: Long) {
        txtMonthName!!.text = date!!.monthOfYear().asText
        txtYear!!.text = date.year.toString()
    }

}