
# Horizontal Calendar

This is Horizontal Calendar with fully customizable you can add or remove any features from the project.And you can also add this project as a module to your project.
Available multiple attributes including colors listers and styles.

# Attributes under attr
<attr name="identifier" format="string" />
        <attr name="calendarType" format="string" />
        <attr name="selectedDateColor" format="string"/>
        <attr name="borderCurrentDate" format="boolean"/>
        <attr name="enableDot" format="boolean"/>
        <attr name="bgcornerRadius" format="integer"/>
        <attr name="setStroke" format="integer"/>
        <attr name="rescheduledate" format="string" />
        <attr name="hightLightCurrentDate" format="boolean" />
        <attr name="startYear" format="integer" />
        <attr name="endYear" format="integer" />
        <attr name="monthYearTextColor" format="color" />
        <attr name="monthYearBackgroundColor" format="color" />
        <attr name="todayTextColor" format="color" />
        <attr name="expandedCalendarBackgroundColor" format="color" />
        <attr name="expandedCalendarTextColor" format="color" />
        <attr name="expandedCalendarSelectedColor" format="color" />
        <attr name="calendarBackground" format="color" />
        <attr name="calendarTextColor" format="color" />
        <attr name="calendarSelectedColor" format="color" />
        <attr name="eventDotColor" format="color" />
        
# Styles available for Calendar
 BOTTOMBAR, SQUARE, DOT, SQUAREDATE, CIRCLEDATE, CIRCLE

## Deployment

To deploy this project Add Following to build.gradle

  1)Add allprojects { repositories {maven { url 'https://jitpack.io' } } }
 
  2)Add  dependencies { implementation 'com.github.bilawalsharif:BSCustomHorizontalCalendar:LatestVesrion' }

Screenshots
![IMG_20211110_232140](https://user-images.githubusercontent.com/26865741/141945996-5dcfbc94-bd2e-478a-83d0-f3e0e086fb28.jpg)

