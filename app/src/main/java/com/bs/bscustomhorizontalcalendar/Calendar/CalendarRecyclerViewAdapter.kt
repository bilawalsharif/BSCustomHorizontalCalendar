package com.bs.bscustomhorizontalcalendar.Calendar

import android.content.Context
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.bs.bscustomhorizontalcalendar.Calendar.CalendarListner
import android.widget.TextView
import android.widget.RelativeLayout
import com.bs.bscustomhorizontalcalendar.R
import android.view.ViewGroup
import android.view.LayoutInflater
import com.bs.bscustomhorizontalcalendar.Calendar.CalendarRecyclerViewAdapter
import com.bs.bscustomhorizontalcalendar.Calendar.CalendarType
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import org.joda.time.LocalDate

class CalendarRecyclerViewAdapter     // Provide a suitable constructor (depends on the kind of dataset)
    (
    private val mDataset: List<LocalDate>,
    private val calendar: Calendar,
    private var calendarType: String?,
    private val colorName: String,
    var borderCurrentDate: Boolean,
    private val enableDot: Boolean,
    private val cornerRadius: Int,
    private val stroke: Int,
    private val hightLightCurrentDate: Boolean
) : RecyclerView.Adapter<CalendarRecyclerViewAdapter.ViewHolder?>() {
    private var calendarListner: CalendarListner? = null
    var selectedDate = -1
        private set
    private val types = arrayOf("largeRectangle", "smallRectangle", "addBottomBar")

    class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        // each data item is just a string in this case
        var date: TextView
        var day: TextView
        var month: TextView
        var tvDot: TextView
        var bottomView: View
        var calendarParentLayout: RelativeLayout

        init {
            date = view.findViewById(R.id.date)
            day = view.findViewById(R.id.day)
            month = view.findViewById(R.id.month)
            calendarParentLayout = view.findViewById(R.id.calendarParentLayout)
            bottomView = view.findViewById(R.id.bottomView)
            tvDot = view.findViewById(R.id.tvDot)
        }
    }

    fun setValue(istrue: Boolean) {
        // this.isHighlightCurrentDate = istrue;
    }

    fun setSelectedPosition(pos: Int) {
        selectedDate = pos
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // create a new view
        /*        calendar_recycler_view_list_item*/
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_calendar_view_dashboard, parent, false)
        val v2 = LayoutInflater.from(parent.context)
            .inflate(R.layout.calendar_recycler_view_list_item, parent, false)
        val v3 = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_calendar_view_provider_profile, parent, false)
        var vh: ViewHolder? = null
        if (calendarType != null) {
            vh = if (calendarType.equals(types[1], ignoreCase = true)) {
                ViewHolder(v)
            } else {
                ViewHolder(v3)
            }
        }
        context = parent.context
        return vh!!
    }
    private fun setCalendarViews(holder: ViewHolder?, position: Int) {
        val whichView = CalendarType.valueOf(calendarType!!)
        when (whichView) {
            CalendarType.DOT -> if (selectedDate == position) {
                val color = Color.parseColor(colorName)
                holder!!.tvDot.visibility = View.VISIBLE
                val shape = context!!.resources.getDrawable(R.drawable.tv_circle_shape)
                (shape as GradientDrawable).setColor(color)
                holder.tvDot.background = shape
                holder.calendarParentLayout.background = null
                holder.date.setTextColor(color)
                holder.day.setTextColor(color)
                holder.month.setTextColor(color)
            } else {
                if (hightLightCurrentDate && position == 0) {
                    val color = Color.parseColor(colorName)
                    holder!!.tvDot.visibility = View.VISIBLE
                    val shape = context!!.resources.getDrawable(R.drawable.tv_circle_shape)
                    (shape as GradientDrawable).setColor(color)
                    holder.tvDot.background = shape
                    holder.calendarParentLayout.background = null
                    holder.date.setTextColor(color)
                    holder.day.setTextColor(color)
                    holder.month.setTextColor(color)
                } else {
                    holder!!.tvDot.visibility = View.GONE
                    holder.calendarParentLayout.background = null
                    holder.date.setTextColor(context!!.resources.getColor(R.color.black))
                    holder.day.setTextColor(context!!.resources.getColor(R.color.black))
                    holder.month.setTextColor(context!!.resources.getColor(R.color.black))
                }
            }
            CalendarType.SQUARE -> {
                if (borderCurrentDate && position == 0) {
                    if (selectedDate == position) {
                        val shape = context!!.resources.getDrawable(R.drawable.bg_solid_shape)
                        val color = Color.parseColor(colorName)
                        (shape as GradientDrawable).setColor(color)
                        shape.cornerRadius = cornerRadius.toFloat()
                        holder!!.calendarParentLayout.background = shape
                        holder.date.setTextColor(context!!.resources.getColor(R.color.white))
                        holder.day.setTextColor(context!!.resources.getColor(R.color.white))
                        holder.month.setTextColor(context!!.resources.getColor(R.color.white))
                    } else {
                        val shape = context!!.resources.getDrawable(R.drawable.bg_corner_shape)
                        val color = Color.parseColor(colorName)
                        (shape as GradientDrawable).setStroke(stroke, color)
                        shape.cornerRadius = cornerRadius.toFloat()
                        holder!!.calendarParentLayout.background = shape
                        holder.date.setTextColor(color)
                        holder.day.setTextColor(color)
                        holder.month.setTextColor(color)
                    }
                } else {
                    val shape = context!!.resources.getDrawable(R.drawable.bg_solid_shape)
                    val color = Color.parseColor(colorName)
                    (shape as GradientDrawable).setColor(color)
                    shape.cornerRadius = cornerRadius.toFloat()
                    holder!!.calendarParentLayout.background = null
                    holder.date.background = null
                    holder.date.setTextColor(context!!.resources.getColor(R.color.white))
                    holder.day.setTextColor(context!!.resources.getColor(R.color.white))
                    holder.month.setTextColor(context!!.resources.getColor(R.color.white))
                }
                if (borderCurrentDate) {
                    if (position > 0) {
                        onSetSelectedDate(holder, position, false)
                    }
                } else {
                    if (position >= 0) {
                        onSetSelectedDate(holder, position, false)
                    }
                }
            }
            CalendarType.BOTTOMBAR -> {
                holder!!.calendarParentLayout.background = null
                if (selectedDate == position && enableDot) {
                    holder.tvDot.visibility = View.VISIBLE
                    val shape = context!!.resources.getDrawable(R.drawable.tv_circle_shape)
                    val color = Color.parseColor(colorName)
                    (shape as GradientDrawable).setColor(color)
                    holder.tvDot.background = shape
                } else {
                    holder.tvDot.visibility = View.GONE
                }
                if (selectedDate == position) {
                    val color = Color.parseColor(colorName)
                    holder.bottomView.visibility = View.VISIBLE
                    holder.bottomView.setBackgroundColor(color)
                    holder.date.setTextColor(color)
                    holder.day.setTextColor(color)
                    holder.month.setTextColor(color)
                } else {
                    if (hightLightCurrentDate && position == 0 && enableDot) {
                        val color = Color.parseColor(colorName)
                        holder.tvDot.visibility = View.VISIBLE
                        holder.bottomView.visibility = View.VISIBLE
                        holder.bottomView.setBackgroundColor(color)
                        val shape = context!!.resources.getDrawable(R.drawable.tv_circle_shape)
                        (shape as GradientDrawable).setColor(color)
                        holder.tvDot.background = shape
                        holder.date.setTextColor(color)
                        holder.day.setTextColor(color)
                        holder.month.setTextColor(color)
                    } else if (hightLightCurrentDate && position == 0) {
                        val color = Color.parseColor(colorName)
                        holder.bottomView.visibility = View.VISIBLE
                        holder.tvDot.visibility = View.GONE
                        holder.bottomView.setBackgroundColor(color)
                        val shape = context!!.resources.getDrawable(R.drawable.tv_circle_shape)
                        (shape as GradientDrawable).setColor(color)
                        holder.tvDot.background = shape
                        holder.date.setTextColor(color)
                        holder.day.setTextColor(color)
                        holder.month.setTextColor(color)
                    } else {
                        holder.bottomView.visibility = View.VISIBLE
                        holder.bottomView.setBackgroundColor(context!!.resources.getColor(R.color.white))
                        holder.date.setTextColor(context!!.resources.getColor(R.color.black))
                        holder.day.setTextColor(context!!.resources.getColor(R.color.black))
                        holder.month.setTextColor(context!!.resources.getColor(R.color.black))
                    }
                }
            }
            CalendarType.SQUAREDATE -> {
                holder!!.calendarParentLayout.background = null
                if (borderCurrentDate && position == 0) {
                    if (selectedDate == position) {
                        val shape = context!!.resources.getDrawable(R.drawable.bg_solid_shape)
                        val color = Color.parseColor(colorName)
                        (shape as GradientDrawable).setStroke(stroke, color)
                        shape.cornerRadius = cornerRadius.toFloat()
                        holder.date.background = shape
                        holder.date.setTextColor(context!!.resources.getColor(R.color.white))
                        holder.day.setTextColor(color)
                        holder.month.setTextColor(context!!.resources.getColor(R.color.white))
                    } else {
                        val shape = context!!.resources.getDrawable(R.drawable.bg_corner_shape)
                        val color = Color.parseColor(colorName)
                        (shape as GradientDrawable).setStroke(stroke, color)
                        shape.cornerRadius = cornerRadius.toFloat()
                        holder.date.background = shape
                        holder.date.setTextColor(color)
                        holder.day.setTextColor(color)
                        holder.month.setTextColor(color)
                    }
                } else {
                    val shape = context!!.resources.getDrawable(R.drawable.bg_solid_shape)
                    val color = Color.parseColor(colorName)
                    (shape as GradientDrawable).setStroke(stroke, color)
                    shape.cornerRadius = cornerRadius.toFloat()
                    holder.date.background = shape
                    holder.date.setTextColor(context!!.resources.getColor(R.color.white))
                    holder.day.setTextColor(context!!.resources.getColor(R.color.white))
                    holder.month.setTextColor(context!!.resources.getColor(R.color.white))
                }
                if (borderCurrentDate) {
                    if (position > 0) {
                        onSetSelectedDate(holder, position, true)
                    }
                } else {
                    if (position >= 0) {
                        onSetSelectedDate(holder, position, true)
                    }
                }
            }
            CalendarType.CIRCLEDATE -> {
                holder!!.calendarParentLayout.background = null
                if (borderCurrentDate && position == 0) {
                    if (selectedDate == position) {
                        val shape = context!!.resources.getDrawable(R.drawable.tv_circle_shape)
                        val color = Color.parseColor(colorName)
                        (shape as GradientDrawable).setColor(color)
                        shape.cornerRadius = cornerRadius.toFloat()
                        holder.date.background = shape
                        holder.date.setTextColor(context!!.resources.getColor(R.color.white))
                        holder.day.setTextColor(color)
                        holder.month.setTextColor(context!!.resources.getColor(R.color.white))
                    } else {
                        val shape =
                            context!!.resources.getDrawable(R.drawable.tv_circle_border_shape)
                        val color = Color.parseColor(colorName)
                        (shape as GradientDrawable).setStroke(stroke, color)
                        shape.cornerRadius = cornerRadius.toFloat()
                        holder.date.background = shape
                        holder.date.setTextColor(color)
                        holder.day.setTextColor(color)
                        holder.month.setTextColor(color)
                    }
                } else {
                    val shape = context!!.resources.getDrawable(R.drawable.tv_circle_shape)
                    val color = Color.parseColor(colorName)
                    (shape as GradientDrawable).setStroke(stroke, color)
                    shape.cornerRadius = cornerRadius.toFloat()
                    holder.date.background = shape
                    holder.date.setTextColor(context!!.resources.getColor(R.color.white))
                    holder.day.setTextColor(color)
                    holder.month.setTextColor(context!!.resources.getColor(R.color.white))
                }
                if (borderCurrentDate) {
                    if (position > 0) {
                        onSetSelectedDate(holder, position, true)
                    }
                } else {
                    if (position >= 0) {
                        onSetSelectedDate(holder, position, true)
                    }
                }
            }
            CalendarType.CIRCLE -> {
                if (borderCurrentDate && position == 0) {
                    if (selectedDate == position) {
                        val shape = context!!.resources.getDrawable(R.drawable.tv_circle_shape)
                        val color = Color.parseColor(colorName)
                        (shape as GradientDrawable).setColor(color)
                        shape.cornerRadius = cornerRadius.toFloat()
                        holder!!.calendarParentLayout.background = shape
                        holder.date.setTextColor(context!!.resources.getColor(R.color.white))
                        holder.day.setTextColor(context!!.resources.getColor(R.color.white))
                        holder.month.setTextColor(context!!.resources.getColor(R.color.white))
                    } else {
                        val shape =
                            context!!.resources.getDrawable(R.drawable.tv_circle_border_shape)
                        val color = Color.parseColor(colorName)
                        (shape as GradientDrawable).setStroke(stroke, color)
                        shape.cornerRadius = cornerRadius.toFloat()
                        holder!!.calendarParentLayout.background = shape
                        holder.date.setTextColor(color)
                        holder.day.setTextColor(color)
                        holder.month.setTextColor(color)
                    }
                } else {
                    val shape = context!!.resources.getDrawable(R.drawable.tv_circle_shape)
                    val color = Color.parseColor(colorName)
                    (shape as GradientDrawable).setColor(color)
                    shape.cornerRadius = cornerRadius.toFloat()
                    holder!!.calendarParentLayout.background = null
                    holder.date.background = null
                    holder.date.setTextColor(context!!.resources.getColor(R.color.white))
                    holder.day.setTextColor(context!!.resources.getColor(R.color.white))
                    holder.month.setTextColor(context!!.resources.getColor(R.color.white))
                }
                if (borderCurrentDate) {
                    if (position > 0) {
                        onSetSelectedDate(holder, position, false)
                    }
                } else {
                    if (position >= 0) {
                        onSetSelectedDate(holder, position, false)
                    }
                }
            }
        }
    }

    private fun onSetSelectedDate(holder: ViewHolder?, position: Int, isDateSquare: Boolean) {
        val whichView = CalendarType.valueOf(calendarType!!)
        var shape: Drawable? = null
        if (calendar.selected!!.dayOfYear == mDataset[position].dayOfYear && calendar.selected!!.year == mDataset[position].year) {
            holder!!.date.setTextColor(context!!.resources.getColor(R.color.white))
            if (calendarType.equals(
                    whichView.toString(),
                    ignoreCase = true
                )
            ) holder.day.setTextColor(
                context!!.resources.getColor(R.color.black)
            ) else holder.day.setTextColor(
                context!!.resources.getColor(R.color.white)
            )
            holder.month.setTextColor(context!!.resources.getColor(R.color.black))
            if (calendarType.equals("CIRCLEDATE", ignoreCase = true)) {
                shape = context!!.resources.getDrawable(R.drawable.bg_solid_shape)
                val color = Color.parseColor(colorName)
                (shape as GradientDrawable?)!!.setColor(color)
                holder.date.background = shape
            } else {
                shape = if (calendarType.equals("CIRCLEDATE", ignoreCase = true)) {
                    context!!.resources.getDrawable(R.drawable.tv_circle_shape)
                } else if (calendarType.equals("CIRCLE", ignoreCase = true)) {
                    holder.day.setTextColor(context!!.resources.getColor(R.color.white))
                    context!!.resources.getDrawable(R.drawable.tv_circle_shape)
                } else if (calendarType.equals("SQUAREDATE", ignoreCase = true)) {
                    holder.day.setTextColor(context!!.resources.getColor(R.color.black))
                    context!!.resources.getDrawable(R.drawable.bg_solid_shape)
                } else {
                    holder.day.setTextColor(context!!.resources.getColor(R.color.white))
                    context!!.resources.getDrawable(R.drawable.bg_solid_shape)
                }
                val color = Color.parseColor(colorName)
                (shape as GradientDrawable?)!!.setColor(color)
                if (isDateSquare) {
                    holder.date.background = shape
                } else {
                    holder.calendarParentLayout.background = shape
                }
            }
        } else {
            holder!!.date.setTextColor(context!!.resources.getColor(R.color.black))
            holder.day.setTextColor(context!!.resources.getColor(R.color.black))
            holder.month.setTextColor(context!!.resources.getColor(R.color.black))
            if (calendarType.equals(whichView.toString(), ignoreCase = true)) {
                holder.date.background = null
                if (selectedDate == 0) holder.date.setTextColor(context!!.resources.getColor(R.color.black))
            } else holder.calendarParentLayout.background = null
        }
    }

    private fun getMonthName(monthOfYear: Int): String {
        var month = ""
        when (monthOfYear) {
            1 -> month = "Jan"
            2 -> month = "Feb"
            3 -> month = "March"
            4 -> month = "April"
            5 -> month = "May"
            6 -> month = "June"
            7 -> month = "July"
            8 -> month = "Aug"
            9 -> month = "Sep"
            10 -> month = "Oct"
            11 -> month = "Nov"
            12 -> month = "Dec"
        }
        return month
    }

    override fun getItemCount(): Int {
        return mDataset.size
    }

    fun setOnClick(listner: CalendarListner?): CalendarRecyclerViewAdapter {
        calendarListner = listner
        return this
    }

    fun updateType(type: String?) {
        calendarType = type
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    companion object {
        var context: Context? = null
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //  GradientDrawable bgShape = (GradientDrawable) holder.view.getBackground();
        // bgShape.setColor(calendar.getCalendarBackground());
        val currentDate = calendar.todayDate.toString()
        holder!!.date.text = "" + mDataset[position].dayOfMonth
        val day = mDataset[position].dayOfWeek
        val m = mDataset[position].monthOfYear
        holder.month.text = getMonthName(m)
        if (calendar.selected != null) {
            setCalendarViews(holder, position)
        }
        if (day == 1) {
            holder.day.text = "Mon"
        } else if (day == 2) {
            holder.day.text = "Tue"
        } else if (day == 3) {
            holder.day.text = "Wed"
        } else if (day == 4) {
            holder.day.text = "Thu"
        } else if (day == 5) {
            holder.day.text = "Fri"
        } else if (day == 6) {
            holder.day.text = "Sat"
        } else if (day == 7) {
            holder.day.text = "Sun"
        }
    }
}