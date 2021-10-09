package com.bs.bscustomhorizontalcalendar.Calendar

import android.icu.util.LocaleData

interface CalendarListner {
    fun updatePosition(localeData: LocaleData?)
}