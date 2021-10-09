package com.bs.bscustomhorizontalcalendar.Calendar

import android.content.Context
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import android.widget.TextView
import com.bs.bscustomhorizontalcalendar.R
import android.view.LayoutInflater
import org.joda.time.Months
import android.view.MotionEvent
import org.joda.time.Days
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import org.joda.time.DurationFieldType
import androidx.core.content.ContextCompat
import android.graphics.PorterDuffColorFilter
import android.graphics.PorterDuff
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import org.joda.time.LocalDate
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar

/*
created by bilawal sharif
 */
class Calendar : LinearLayout, CalendarFragment.OnItemClickListener{
    private var fragments: MutableList<CalendarFragment>? = null
    private var dates: MutableList<LocalDate>? = null
    private var startYear = 0
    private var endYear = 0
    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: CalendarRecyclerViewAdapter? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var pager: ViewPager? = null
    private var tv_month_year: TextView? = null
    private var tv_today: TextView? = null
    private var pageAdapter: CalendarPagerAdapter? = null
    var startDate: LocalDate? = null
    var endDate: LocalDate? = null
    var todayDate: LocalDate? = null
    var changeDetected = false
    var scrollPager = true
    var clicked = false

    var lastSelectedItem = -1
    var lastSelectedposition = -1
    var monthYearTextColor = 0
        set(monthYearTextColor) {
            field = monthYearTextColor
            tv_month_year!!.setTextColor(monthYearTextColor)
        }
    var monthYearBackgroundColor = 0
        set(monthYearBackgroundColor) {
            field = monthYearBackgroundColor
            tv_month_year!!.setBackgroundColor(monthYearBackgroundColor)
        }
    var todayTextColor = 0
        set(todayTextColor) {
            field = todayTextColor
            tv_today!!.setTextColor(todayTextColor)
        }
    var todayBackgroundColor = 0
        set(todayBackgroundColor) {
            field = todayBackgroundColor
            tv_today!!.setBackgroundColor(todayBackgroundColor)
        }
    var expandedCalendarBackgroundColor = 0
        set(expandedCalendarBackgroundColor) {
            field = expandedCalendarBackgroundColor
            pageAdapter!!.notifyDataSetChanged()
        }
    var expandedCalendarTextColor = 0
        set(expandedCalendarTextColor) {
            field = expandedCalendarTextColor
            pageAdapter!!.notifyDataSetChanged()
        }
    var expandedCalendarSelectedColor = 0
        set(expandedCalendarSelectedColor) {
            field = expandedCalendarSelectedColor
            pageAdapter!!.notifyDataSetChanged()
        }
    var calendarBackground = 0
        set(calendarBackground) {
            field = calendarBackground
            mAdapter!!.notifyDataSetChanged()
        }
    var calendarTextColor = 0
        set(calendarTextColor) {
            field = calendarTextColor
            mAdapter!!.notifyDataSetChanged()
        }
    var calendarSelectedColor = 0
        set(calendarSelectedColor) {
            field = calendarSelectedColor
            mAdapter!!.notifyDataSetChanged()
        }
    var eventDotColor = 0
        set(eventDotColor) {
            field = eventDotColor
            pageAdapter!!.notifyDataSetChanged()
        }
    var selected: LocalDate? = null
        private set
    var calendarListener: CalendarListener? = null
    private var eventDates: ArrayList<LocalDate>? = null
    var identifier: String? = null
    private val lastSelectPosition = 0
    private var alreadybookAppointmentPosition = -1
    private var calendarType: String? = null
    private var selectedDateColor: String? = null
    private var isHighlightCurrentDate = false
    private var enableDot = false
    private var hightLightCurrentDate = false
    private var cornerRadius = 0
    private var setStroke = 0

    interface CalendarListener {
        fun onExpand()
        fun onCollapse()
        fun onDateChange(date: LocalDate?)
        fun onTodayClick(today: LocalDate?)
        fun onCalendarScroll(date: LocalDate?, newState: Int, position: Int, itemId: Long)
    }

    fun addEvent(date: LocalDate) {
        if (!eventDates!!.contains(date)) {
            eventDates!!.add(date)
            pageAdapter!!.notifyDataSetChanged()
        }
    }

    fun deleteEvent(date: LocalDate) {
        if (eventDates!!.contains(date)) {
            eventDates!!.remove(date)
            pageAdapter!!.notifyDataSetChanged()
        }
    }

    fun getEventDates(): ArrayList<LocalDate>? {
        return eventDates
    }

    fun setEventDates(eventDates: ArrayList<LocalDate>?) {
        if (eventDates != null) {
            this.eventDates = eventDates
        } else {
            this.eventDates!!.clear()
        }
    }

    constructor(context: Context) : super(context) {
        initControl(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.CalendarDateElement)
        identifier = a.getString(R.styleable.CalendarDateElement_identifier)
        calendarType = a.getString(R.styleable.CalendarDateElement_calendarType)
        isHighlightCurrentDate =
            a.getBoolean(R.styleable.CalendarDateElement_borderCurrentDate, false)
        enableDot = a.getBoolean(R.styleable.CalendarDateElement_enableDot, false)
        hightLightCurrentDate =
            a.getBoolean(R.styleable.CalendarDateElement_hightLightCurrentDate, false)
        selectedDateColor = a.getString(R.styleable.CalendarDateElement_selectedDateColor)
        cornerRadius = a.getInt(R.styleable.CalendarDateElement_bgcornerRadius, 10)
        setStroke = a.getInt(R.styleable.CalendarDateElement_setStroke, 1)
        startYear = a.getInteger(R.styleable.CalendarDateElement_startYear, 2021)
        endYear = a.getInteger(R.styleable.CalendarDateElement_endYear, 2030)
        val current = currentDate
        val date = current.split("-").toTypedArray()
        val month = date[1].toInt()
        val day = date[2].toInt()
        startDate = LocalDate(startYear, month, day)
        endDate = LocalDate(endYear, 12, 31)
        //To resolve bug where all the dates are selected by default
        //todayDate = new LocalDate();
        todayDate = LocalDate(startDate)
        initControl(context)
    }

    fun setSelectedDate(month: Int, day: Int, year: Int) {
        selected = LocalDate(year, month, day)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initControl(context)
    }

    fun func(context: Context, selectedDate: String?) {
        if (selectedDate != null) {
            getAlreadybookedDate(selectedDate)
            setUpPager(context)
            setUpRecyclerView(context)
        }
    }

    fun initControl(context: Context) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.custom_calendar, this)
        eventDates = ArrayList()
        mRecyclerView = findViewById<View>(R.id.my_recycler_view) as RecyclerView
        pager = findViewById<View>(R.id.viewpager) as ViewPager
        tv_month_year = findViewById<View>(R.id.monthYear) as TextView
        tv_today = findViewById<View>(R.id.date) as TextView
        // tv_month_year.setTextColor(monthYearTextColor);
        //// tv_month_year.setBackgroundColor(monthYearBackgroundColor);
        //  mRecyclerView.setBackgroundColor(expandedCalendarBackgroundColor);
        // tv_today.setTextColor(todayTextColor);
        // tv_today.setBackgroundColor(todayBackgroundColor);
        if (todayDate!!.isBefore(endDate) && todayDate!!.isAfter(startDate) || todayDate!!.isEqual(
                startDate
            ) || todayDate!!.isEqual(endDate)
        ) {
            //tv_today.setVisibility(VISIBLE);
        } else {
            tv_today!!.visibility = INVISIBLE
        }
        setUpPager(context)
        setUpRecyclerView(context)
        //setUpListeners(context);
    }

    private fun setUpListeners(context: Context) {
        tv_month_year!!.setOnClickListener(OnClickListener {
            if (pager!!.visibility == VISIBLE) {
                pager!!.visibility = GONE
                if (calendarListener != null) {
                    calendarListener!!.onCollapse()
                }
                tv_month_year!!.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_drop_down_black_24dp,
                    0
                )
            } else {
                val date = tv_month_year!!.text.toString().split(",").toTypedArray()
                val currentSelectedDate =
                    LocalDate(date[1].trim { it <= ' ' } + "-" + getMonthValue(
                        date[0].trim { it <= ' ' }) + "-" + "01")
                val months = Months.monthsBetween(startDate, currentSelectedDate).months
                changeDetected = true
                pager!!.currentItem = months
                pager!!.visibility = VISIBLE
                tv_month_year!!.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_drop_up_black_24dp,
                    0
                )
                changeDetected = false
                if (calendarListener != null) {
                    calendarListener!!.onExpand()
                }
            }
        })
        tv_today!!.setOnTouchListener(object : OnTouchListener {
            override fun onTouch(view: View, event: MotionEvent): Boolean {
                clicked = false
                setMonthYearText(getMonthName(todayDate!!.monthOfYear), "" + todayDate!!.year)
                if (pager!!.visibility == VISIBLE) {
                    pager!!.currentItem = Months.monthsBetween(startDate, todayDate).months
                }
                val t = LocalDate()
                if (selected != null && !selected!!.isEqual(t)) {
                    selected = t
                    if (calendarListener != null) {
                        calendarListener!!.onDateChange(selected)
                    }
                } else if (selected == null) {
                    selected = t
                }
                if (calendarListener != null) {
                    calendarListener!!.onTodayClick(selected)
                }
                mAdapter!!.notifyDataSetChanged()
                mLayoutManager!!.scrollToPositionWithOffset(
                    Days.daysBetween(
                        startDate,
                        todayDate
                    ).days - 1, 0
                )
                val s = todayDate!!.dayOfMonth.toString() + ""
                highlightDate(
                    fragments!![pager!!.currentItem],
                    todayDate!!.dayOfMonth.toString() + "",
                    context
                )
                return true
            }
        })
    }

    private fun setUpPager(context: Context) {
        val activity = context as FragmentActivity
        fragments = ArrayList()
        for (i in startYear..endYear) {
            for (j in 1..12) {
                fragments!!.add(CalendarFragment.newInstance(i, j, "client", identifier))
            }
        }
        pageAdapter = CalendarPagerAdapter(activity.supportFragmentManager, fragments!!)
        pager!!.adapter = pageAdapter
        pager!!.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                var position = position
                if (!changeDetected) {
                    position = position + 1
                    var year = position / 12
                    var month = position % 12
                    if (month == 0) {
                        year = year - 1
                        month = 12
                    }
                    year = startYear + year
                    setMonthYearText(getMonthName(month), "" + year)
                    val s = LocalDate("$year-$month-01")
                    val numberOfdays = Days.daysBetween(startDate, s).days
                    mLayoutManager!!.scrollToPositionWithOffset(numberOfdays - 1, 0)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        }
        )
        pager!!.visibility = GONE
        tv_month_year!!.setCompoundDrawablesWithIntrinsicBounds(
            0,
            0,
            R.drawable.ic_arrow_drop_down_black_24dp,
            0
        )
    }

    private fun setUpRecyclerView(context: Context) {
        mLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        mRecyclerView!!.layoutManager = mLayoutManager
        //mRecyclerView.addOnScrollListener(new HorizontalCalendarScrollListener());
        dates = ArrayList()
        val days = Days.daysBetween(startDate, endDate).days
        for (i in 0 until days) {
            val d = startDate!!.withFieldAdded(DurationFieldType.days(), i)
            dates!!.add(d)
        }
        mAdapter = CalendarRecyclerViewAdapter(
            dates!!,
            this,
            calendarType,
            selectedDateColor!!,
            isHighlightCurrentDate,
            enableDot,
            cornerRadius,
            setStroke,
            hightLightCurrentDate
        )
        val date = todayDate.toString()
        //bilawal for default selected date
        if (date.equals(currentDate, ignoreCase = true)) {
            clicked = true
            selected = LocalDate.parse(date)
        }
        mRecyclerView!!.isHorizontalScrollBarEnabled = false
        mRecyclerView!!.adapter = mAdapter
        mLayoutManager!!.scrollToPositionWithOffset(
            Days.daysBetween(startDate, todayDate).days - 1,
            0
        )
        mRecyclerView!!.addOnItemTouchListener(
            RecyclerItemClickListener(
                context,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                        if (position != -1) selected = LocalDate(
                            dates!!.get(position).year.toString() + "-" + dates!!.get(position).monthOfYear + "-" + dates!!.get(
                                position
                            ).dayOfMonth
                        )
                        if (alreadybookAppointmentPosition != -1) if (alreadybookAppointmentPosition == position) {
                            mAdapter!!.setValue(true)
                        }
                        mAdapter!!.setSelectedPosition(position)
                        mAdapter!!.notifyDataSetChanged()
                        // mLayoutManager.scrollToPositionWithOffset(position - 1, 0);
                        pageAdapter!!.notifyDataSetChanged()
                        if (calendarListener != null) {
                            calendarListener!!.onDateChange(selected)
                        }
                        clicked = true
                        lastSelectedposition = position
                        if (calendarListener != null) {
                            if (position == 0) {
                                calendarListener!!.onTodayClick(selected)
                            }
                        }
                    }
                })
        )
        mRecyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
/*                    if (lastSelectPosition >= 0 && lastSelectPosition <= 365)
                        selected = new LocalDate(dates.get(lastSelectPosition).getYear() + "-" + dates.get(lastSelectPosition).getMonthOfYear() + "-" + dates.get(lastSelectPosition).getDayOfMonth());
                       mAdapter.notifyDataSetChanged();

                    int position = getCurrentItem();*/

                    /*   if (calendarListener != null) {
                        calendarListener.onCalendarScroll(dates.get(newState), newState, newState, newState);
                    }*/
                }
                super.onScrollStateChanged(recyclerView, newState)
            }

            private val currentItem: Int
                private get() = (mRecyclerView!!.layoutManager as LinearLayoutManager?)
                    ?.findFirstVisibleItemPosition()!!

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val currentPosition = mLayoutManager!!.findFirstVisibleItemPosition()
                if (scrollPager) {
                    if (calendarListener != null) {
                        calendarListener!!.onCalendarScroll(
                            dates!!.get(currentPosition),
                            currentPosition,
                            currentPosition,
                            currentPosition.toLong()
                        )
                    }
                } else {
                    scrollPager = true
                }
                /*                if (scrollPager) {
                    int position = mLayoutManager.findFirstVisibleItemPosition() + 1;
                    LocalDate d = dates.get(position);
                    int year = d.getYear();
                    int month = d.getMonthOfYear();
                    setMonthYearText(getMonthName(month), "" + year);
                    changeDetected = true;
                    pager.setCurrentItem(Months.monthsBetween(startDate, d).getMonths());
                    lastSelectPosition = position;

                   ((LinearLayoutManager)recyclerView.getLayoutManager()).scrollToPositionWithOffset(dates.size(),0);


                    lastSelectPosition = lastSelectPosition + dx;
                    if (lastSelectPosition>=0 && lastSelectPosition<=365)
                    selected = new LocalDate(dates.get(lastSelectPosition).getYear() + "-" + dates.get(lastSelectPosition).getMonthOfYear() + "-" + dates.get(lastSelectPosition).getDayOfMonth());
                    mAdapter.notifyDataSetChanged();
                    //  mLayoutManager.scrollToPositionWithOffset(lastSelectPosition+1, 1);
                    // pageAdapter.notifyDataSetChanged();


                    if (clicked) {
                        //  highlightDate(fragments.get(Months.monthsBetween(startDate, d).getMonths()), "" + d.getDayOfMonth(), context);
                        clicked = false;
                    }
                    changeDetected = false;
                } else {
                    scrollPager = true;
                }*/
            }
        })
        //  setSwipeForRecyclerView();
    }

    fun setCalendarType(type: String?) {
        calendarType = type
    }

    fun stepsforword() {
        val totaldays = Days.daysBetween(todayDate, endDate).days - 1
        // if(totaldays<(lastSelectedposition-5))
        val lastVisibleItemIndex = mLayoutManager!!.findLastVisibleItemPosition()
        mLayoutManager!!.smoothScrollToPosition(mRecyclerView, null, lastVisibleItemIndex + 5)
    }

    fun stepsbackword() {
        //Days.daysBetween(startDate, todayDate).getDays() - 1
        //  if(lastSelectedposition>5) {
        // mLayoutManager.scrollToPosition((lastSelectPosition-5));
        val lastVisibleItemIndex = mLayoutManager!!.findFirstVisibleItemPosition()
        mLayoutManager!!.smoothScrollToPosition(mRecyclerView, null, lastVisibleItemIndex - 5)
        //  }
    }

    fun getAlreadybookedDate(date: String) {
        val selected_date = date.split("/").toTypedArray()
        var m2 = selected_date[0]
        var d2 = selected_date[1]
        val y2 = selected_date[2]
        if (d2.startsWith("0")) {
            d2 = d2.replace("0", "")
        }
        if (m2.startsWith("0")) {
            m2 = m2.replace("0", "")
        }
        if (y2.startsWith("0")) {
            m2 = m2.replace("0", "")
        }
        val selectedDate = m2 + d2 + y2
        for (i in dates!!.indices) {
            val y = dates!![i].year.toString()
            val m = dates!![i].monthOfYear.toString()
            val d = dates!![i].dayOfMonth.toString()
            val date1 = m + d + y
            if (date1.trim { it <= ' ' }
                    .equals(selectedDate.trim { it <= ' ' }, ignoreCase = true)) {
                alreadybookAppointmentPosition = i
                break
            }
        }
    }

    fun scrollToSpecificDate(date2: String) {
        var position = 0
        val selected_date = date2.split("/").toTypedArray()
        var m2 = selected_date[0]
        var d2 = selected_date[1]
        val y2 = selected_date[2]
        if (d2.startsWith("0")) {
            d2 = d2.replace("0", "")
        }
        if (m2.startsWith("0")) {
            m2 = m2.replace("0", "")
        }
        if (y2.startsWith("0")) {
            m2 = m2.replace("0", "")
        }
        val selectedDate = m2 + d2 + y2
        for (i in dates!!.indices) {
            val y = dates!![i].year.toString()
            val m = dates!![i].monthOfYear.toString()
            val d = dates!![i].dayOfMonth.toString()
            val date1 = m + d + y
            if (date1.trim { it <= ' ' }
                    .equals(selectedDate.trim { it <= ' ' }, ignoreCase = true)) {
                position = i
                break
            }
        }
        val finalPosition = position
        Handler().postDelayed(object : Runnable {
            override fun run() {
                //    mLayoutManager.smoothScrollToPosition(mRecyclerView, null, (finalPosition));
                mLayoutManager!!.scrollToPositionWithOffset(finalPosition, 20)
            }
        }, 200)
    }// getting date in this format

    // to get the date
    private val currentDate: String
        private get() {
            val date = Date() // to get the date
            val df =
                SimpleDateFormat("yyyy-MM-dd") // getting date in this format
            return df.format(date.time)
        }
    private fun getMonthName(month: Int): String? {
        when (month) {
            1 -> return "Jan"
            2 -> return "Feb"
            3 -> return "Mar"
            4 -> return "Apr"
            5 -> return "May"
            6 -> return "Jun"
            7 -> return "Jul"
            8 -> return "Aug"
            9 -> return "Sep"
            10 -> return "Oct"
            11 -> return "Nov"
            12 -> return "Dec"
        }
        return null
    }

    private fun highlightDate(f: CalendarFragment, date: String, context: Context) {
        val mDrawable = ContextCompat.getDrawable(context, R.drawable.background_green_ring)
        mDrawable!!.colorFilter =
            PorterDuffColorFilter(expandedCalendarSelectedColor, PorterDuff.Mode.MULTIPLY)
        val v = f.gridView
        if (v != null) {
            for (i in 0 until v.childCount) {
                val l = v.getChildAt(i) as LinearLayout
                val v2 = l.getChildAt(0) as TextView
                if (v2.text.toString().equals(date, ignoreCase = true)) {
                    l.background = mDrawable
                } else {
                    v.getChildAt(i).background = null
                }
            }
        }
    }

    override fun OnItemClicked(date: Int) {
        scrollPager = false
        val cdate = tv_month_year!!.text.toString().split(",").toTypedArray()
        val currentSelectedDate = LocalDate(cdate[1].trim { it <= ' ' } + "-" + getMonthValue(
            cdate[0].trim { it <= ' ' }) + "-" + date)
        selected = currentSelectedDate
        mAdapter!!.notifyDataSetChanged()
        mLayoutManager!!.scrollToPositionWithOffset(
            Days.daysBetween(
                startDate,
                currentSelectedDate
            ).days - 1, 0
        )
        pager!!.visibility = GONE
        tv_month_year!!.setCompoundDrawablesWithIntrinsicBounds(
            0,
            0,
            R.drawable.ic_arrow_drop_down_black_24dp,
            0
        )
        //        pageAdapter.notifyDataSetChanged();
        if (calendarListener != null) {
            calendarListener!!.onDateChange(currentSelectedDate)
        }
    }

    private fun setMonthYearText(month: String?, year: String) {
        tv_month_year!!.text = "$month, $year"
    }

    private inner class HorizontalCalendarScrollListener internal constructor() :
        RecyclerView.OnScrollListener() {
        val selectedItemRefresher: Runnable = SelectedItemRefresher()
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            //On Scroll, agenda is refresh to update background colors
            post(selectedItemRefresher)
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                if (calendarListener != null) {
                    calendarListener!!.onCalendarScroll(
                        dates!![lastSelectPosition], newState, newState, mAdapter!!.getItemId(
                            lastSelectedItem
                        )
                    )
                }
            }
            super.onScrollStateChanged(recyclerView, newState)
        }

        private inner class SelectedItemRefresher internal constructor() : Runnable {
            override fun run() {
/*                final int positionOfCenterItem = lastSelectPosition;
                selected = new LocalDate(dates.get(lastSelectPosition).getYear() + "-" + dates.get(lastSelectPosition).getMonthOfYear() + "-" + dates.get(lastSelectPosition).getDayOfMonth());
                mAdapter.notifyDataSetChanged();
                //   mLayoutManager.scrollToPositionWithOffset(lastSelectPosition - 1, 0);
                pageAdapter.notifyDataSetChanged();
                if (calendarListener != null) {
                    calendarListener.onDateChange(selected);
                }*/
                // clicked = true;

/*                if ((lastSelectedItem == -1) || (lastSelectedItem != positionOfCenterItem)) {
                    //On Scroll, agenda is refresh to update background colors
                    refreshItemsSelector(positionOfCenterItem);
                    if (lastSelectedItem != -1) {
                        refreshItemsSelector(lastSelectedItem);
                    }
                    lastSelectedItem = positionOfCenterItem;
                }*/
            }
        }
    }

    fun refreshItemsSelector(position1: Int, vararg positions: Int) {
        mAdapter!!.notifyItemChanged(position1, "UPDATE_SELECTOR")
        if (positions != null && positions.size > 0) {
            for (pos in positions) {
                mAdapter!!.notifyItemChanged(pos, "UPDATE_SELECTOR")
            }
        }
    }
        private fun getMonthValue(month: String): Int {
            return try {
                val date = SimpleDateFormat("MMMM").parse(month)
                val cal = Calendar.getInstance()
                cal.time = date
                cal[Calendar.MONTH] + 1
            } catch (p: ParseException) {
                1
            }
        }
    }