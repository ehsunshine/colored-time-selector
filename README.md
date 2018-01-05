[![Download](https://api.bintray.com/packages/ehsun/maven/colored-time-range-selector/images/download.svg)](https://bintray.com/ehsun/maven/colored-time-range-selector/_latestVersion)
[![Android Arsenal]( https://img.shields.io/badge/Android%20Arsenal-colored--time--selector-green.svg?style=flat )]( https://android-arsenal.com/details/1/6634 )
[![GitHub license](https://img.shields.io/github/license/ehsunshine/colored-time-selector.svg)](https://github.com/ehsunshine/colored-time-selector/blob/master/LICENSE)
[![Build Status](https://travis-ci.org/ehsunshine/colored-time-selector.svg?branch=master)](https://travis-ci.org/ehsunshine/colored-time-selector)

# Colored Time Range Selector
A smart colored time range selector. Users can select just free time with a handy colorful range selector.

## Screen Shots
<img src="https://github.com/ehsunshine/colored-time-selector/blob/master/screenshots/screen1.jpg" alt="Colored Time Range Bar Selector" width="256">
<img src="https://github.com/ehsunshine/colored-time-selector/blob/master/screenshots/screen2.jpg" alt="Colored Time Range Bar Selector" width="256">

## Features

- Select a `time range` between 2 hours
- Highlight `available time range` with your desire color
- Select `just free times` in a range
- Two different type of view, with `Selector` and without it

## Use it

**build.gradle**
```gradle
dependencies {
    implementation 'de.ehsun.coloredtimebar:coloredtimebar:1.0'
}
```

In your layout use the ColoredTimeBar as below:

```xml
<de.ehsun.coloredtimebar.TimelineView
            android:id="@+id/timelineView"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:barColorAvailable="@color/available_time_default_color"
            app:barColorHighlight="@color/selector_default_color"
            app:barColorNotAvailable="@color/timeline_default_color"
            app:barWidth="16dp"
            app:fractionLineColor="@color/separatorDark"
            app:fractionLineLength="8dp"
            app:fractionLineWidth="1dp"
            app:fractionPrimaryTextColor="@color/fraction_default_color"
            app:fractionSecondaryTextColor="@color/fraction_default_color"
            app:fractionTextInterval="2"
            app:fractionTextSize="8sp"
            app:timeRange="07:00-19:00" />
```
In your code you can easily set the available time by passing an array of time range string as below:

```kotlin
timelineView.setAvailableTimeRange(listOf("07:00 - 10:15", "12:00 - 15:00"))
```
And to enable picker handles use it as below:

```xml
<de.ehsun.coloredtimebar.TimelinePickerView
                android:id="@+id/timelinePicker"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                app:barColorAvailable="@color/available_time_default_color"
                app:barColorHighlight="@color/selector_default_color"
                app:barColorNotAvailable="@color/timeline_default_color"
                app:barWidth="24dp"
                app:fractionLineColor="@color/separatorDark"
                app:fractionLineLength="8dp"
                app:fractionLineWidth="1dp"
                app:fractionPrimaryTextColor="@color/colorGrey900"
                app:fractionSecondaryTextColor="@color/colorBlueGrey500"
                app:fractionTextInterval="1"
                app:fractionTextSize="8sp"
                app:stepSize="15"
                app:timeRange="07:00-19:00" />
```

```kotlin
timelinePicker.setOnSelectedTimeRangeChangedListener { from, to ->
            selectedTimeTextView.text = getString(R.string.from_to, from.format(), to.format())
        }
```

<img src="https://github.com/ehsunshine/colored-time-selector/blob/master/screenshots/screen3.jpg" alt="Colored Time Range Bar Selector">

## Do you like it?
Use it and develop it as you like and buy me a cup of tea :)

<a href='https://ko-fi.com/D1D775AP' target='_blank'><img height='36' style='border:0px;height:36px;' src='https://az743702.vo.msecnd.net/cdn/kofi1.png?v=0' border='0' alt='Buy Me a Coffee at ko-fi.com' /></a>