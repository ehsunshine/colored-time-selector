package de.ehsun.smartbooking.ui.main

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PopupMenu
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import de.ehsun.smartbooking.R
import de.ehsun.smartbooking.entities.Room
import de.ehsun.smartbooking.entities.RoomOrder
import de.ehsun.smartbooking.ui.base.BaseActivity
import de.ehsun.smartbooking.ui.main.adapter.RoomAdapter
import de.ehsun.smartbooking.ui.roomdetails.RoomDetailsActivity
import de.ehsun.smartbooking.utils.extension.afterTextChanged
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.popup_filter.view.*
import org.joda.time.DateTime
import java.util.*
import javax.inject.Inject

class MainActivity : BaseActivity(), MainContract.View, PopupMenu.OnMenuItemClickListener, RoomAdapter.RoomEventListener {

    @Inject lateinit var presenter: MainContract.Presenter
    private lateinit var adapter: RoomAdapter
    private var filterPopupWindow: PopupWindow? = null

    private var calendar = Calendar.getInstance(Locale.GERMANY)
    private lateinit var currentDate: DateTime
    private var date = DatePickerDialog.OnDateSetListener { _, year, monthOdYear, dayOfMonth ->
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, monthOdYear)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        currentDate = DateTime(calendar.time)
        displayDate(currentDate)
        presenter.onDateChanged(currentDate.millis)
    }

    companion object {
        @JvmStatic
        fun create(context: Context): Intent = Intent(context, MainActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun injectDependencies() {
        setTheme(R.style.MaterialTheme)
        applicationComponent.inject(this)
        presenter.onBindView(this)
    }

    override fun initViews() {
        adapter = RoomAdapter(this)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        title = ""

        sortTextView.setOnClickListener({ showSortPopup(sortImageButton) })
        initDatePicker()
        val layoutManager = LinearLayoutManager(this)
        filterViewGroup.setOnClickListener({ showFilterPopup(app_bar_layout) })
        swipeRefreshContainer.setOnRefreshListener({ presenter.onRefreshBooks() })
        booksRecyclerView.layoutManager = layoutManager
        booksRecyclerView.adapter = adapter

        presenter.onViewInitialized()
    }

    private fun initDatePicker() {
        currentDate = DateTime.now()
        displayDate(currentDate)
        previousDateImageButton.setOnClickListener {
            currentDate = currentDate.minusDays(1)
            displayDate(currentDate)

        }
        nextDateImageButton.setOnClickListener {
            currentDate = currentDate.plusDays(1)
            displayDate(currentDate)
        }
        currentDateTextView.afterTextChanged { presenter.onDateChanged(currentDate.millis) }
        currentDateTextView.setOnClickListener {
            DatePickerDialog(this, date,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get((Calendar.DAY_OF_MONTH)))
                    .show()
        }
    }

    private fun displayDate(dateTime: DateTime) {
        currentDate = dateTime
        currentDateTextView.text = dateTime.toLocalDate().toString()
    }

    override fun onMenuItemClick(item: MenuItem?) = when (item?.itemId) {
        R.id.menuSortByLocation -> {
            presenter.onSortChanged(RoomOrder.LOCATION)
            true
        }
        R.id.menuSortByName -> {
            presenter.onSortChanged(RoomOrder.NAME)
            true
        }
        R.id.menuSortBySize -> {
            presenter.onSortChanged(RoomOrder.SIZE)
            true
        }
        R.id.menuSortByCapacity -> {
            presenter.onSortChanged(RoomOrder.CAPACITY)
            true
        }
        else -> false
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onViewDestroyed()
    }

    private fun showSortPopup(v: View) {
        val popup = PopupMenu(this, v)
        popup.setOnMenuItemClickListener(this)
        val inflater = popup.menuInflater
        inflater.inflate(R.menu.sort_actions, popup.menu)
        popup.show()
    }

    @SuppressLint("InflateParams")
    private fun showFilterPopup(v: ViewGroup) {
        val popupWindow = this.filterPopupWindow ?: layoutInflater.inflate(R.layout.popup_filter, null).run {
            val popupWindow = PopupWindow(
                    this,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            )
            popupWindow.isOutsideTouchable = true
            if (Build.VERSION.SDK_INT >= 21) {
                popupWindow.elevation = 8.0f
            }

            filterByNameTextBox.afterTextChanged { presenter.onAddOrRemoveFilter(it) }
            isAvailableCheckbox.setOnCheckedChangeListener { _, checked -> presenter.onAddOrRemoveIsAvailableFilter(checked) }
            popupWindow
        }

        val bookFilter = presenter.getCurrentBookFilter()
        with(popupWindow) {
            contentView.filterByNameTextBox.setText(bookFilter.name)
            contentView.isAvailableCheckbox.isChecked = bookFilter.isAvailable
            isFocusable = true
            update()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            popupWindow.showAsDropDown(v, Gravity.BOTTOM, 0, 0)
        } else {
            popupWindow.showAsDropDown(v, 0, 0)
        }
    }

    override fun onRoomItemClick(room: Room) {
        val intent = RoomDetailsActivity.create(this, currentDate.millis, room)
        startActivity(intent)
    }

    override fun showRooms(rooms: List<Room>, showSections: Boolean) {
        adapter.set(rooms, showSections)
    }

    override fun showLoading() {
        loadingContainer.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loadingContainer.visibility = View.GONE
        swipeRefreshContainer.isRefreshing = false
    }
}