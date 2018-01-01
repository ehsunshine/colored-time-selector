package de.ehsun.smartbooking.ui.roomdetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import de.ehsun.coloredtimebar.SimpleTime
import de.ehsun.smartbooking.R
import de.ehsun.smartbooking.entities.Attendee
import de.ehsun.smartbooking.entities.BookingInfo
import de.ehsun.smartbooking.entities.BookingResult
import de.ehsun.smartbooking.entities.Room
import de.ehsun.smartbooking.ui.base.BaseActivity
import de.ehsun.smartbooking.ui.roomdetails.adapter.AttendeeAdapter
import de.ehsun.smartbooking.ui.roomdetails.adapter.EquipmentAdapter
import de.ehsun.smartbooking.ui.roomdetails.adapter.ImageSliderAdapter
import de.ehsun.smartbooking.utils.ValidationUtil
import kotlinx.android.synthetic.main.activity_room_details.*
import kotlinx.android.synthetic.main.view_submit_event.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class RoomDetailsActivity : BaseActivity(),
        RoomDetailsContract.View,
        AttendeeAdapter.AttendeeEventListener {

    @Inject lateinit var presenter: RoomDetailsContract.Presenter

    private lateinit var equipmentAdapter: EquipmentAdapter
    private lateinit var attendeeAdapter: AttendeeAdapter
    private var submitDialog: AlertDialog? = null
    private var listOfAttendees = mutableListOf<Attendee>()
    private val bookingInfo = BookingInfo()

    companion object {
        private const val KEY_ROOM_DETAILS = "KEY_ROOM_DETAILS"
        private const val KEY_CURRENT_DATE = "KEY_CURRENT_DATE"
        private val TIME_FORMATTER = SimpleDateFormat("HH:mm", Locale.GERMAN)

        @JvmStatic
        fun create(context: Context, currentDate: Long, room: Room) = Intent(context, RoomDetailsActivity::class.java).apply {
            this.currentDate = currentDate
            this.room = room
        }

        private var Intent.currentDate: Long
            get() = this.getLongExtra(KEY_CURRENT_DATE, 0)
            set(value) {
                this.putExtra(KEY_CURRENT_DATE, value)
            }
        private var Intent.room: Room
            get() = this.getParcelableExtra(KEY_ROOM_DETAILS)
            set(value) {
                this.putExtra(KEY_ROOM_DETAILS, value)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_details)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            finish()
            true
        }
        else -> false
    }

    override fun injectDependencies() {
        applicationComponent.inject(this)
        presenter.onBindView(this)
    }

    override fun initViews() {
        equipmentAdapter = EquipmentAdapter()
        attendeeAdapter = AttendeeAdapter(this)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        showRooms()

        val equipmentLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        equipmentRecyclerView.layoutManager = equipmentLayoutManager
        equipmentRecyclerView.adapter = equipmentAdapter

        val attendeeLayoutManager = LinearLayoutManager(this)
        attendeesRecyclerView.layoutManager = attendeeLayoutManager
        attendeesRecyclerView.adapter = attendeeAdapter

        addAttendeeButton.setOnClickListener { _ ->
            presenter.onAddAttendeeRequested(
                    attendeeNameEditText.text.toString(),
                    attendeeEmailEditText.text.toString(),
                    attendeePhoneEditText.text.toString()
            )
        }
        submitButton.setOnClickListener {
            showSubmitDialog()
        }
        presenter.onViewInitialized()
    }

    private fun showSubmitDialog() {
        submitDialog = AlertDialog.Builder(this)
                .setTitle(R.string.dialog_title)
                .setView(R.layout.view_submit_event)
                .create()

        submitDialog?.setCanceledOnTouchOutside(false)

        submitDialog?.setOnShowListener {
            submitDialog?.submitEventButton?.setOnClickListener {
                bookingInfo.attendees = listOfAttendees
                bookingInfo.booking.date = intent.currentDate / 1000L
                bookingInfo.booking.room = intent.room.name
                bookingInfo.booking.title = submitDialog?.eventTitleEditText?.text.toString()
                bookingInfo.booking.description = submitDialog?.eventDescriptionEditText?.text.toString()
                bookingInfo.booking.startTime = timelinePicker.selectedTimeRange.start.toTimeStamp()
                bookingInfo.booking.endTime = timelinePicker.selectedTimeRange.endInclusive.toTimeStamp()
                presenter.onSubmitBookingRequested(bookingInfo)
            }
        }
        submitDialog?.show()
    }

    private fun showRooms() {
        val room = intent.room
        title = getString(R.string.room_number, room.name)
        roomNameTextView.text = getString(R.string.room_number_and_floor, room.name, room.location)
        equipmentAdapter.set(room.equipment)
        sizeTextView.text = getString(R.string.room_capacity_text_view, room.size, room.capacity)
        imageViewPager.adapter = ImageSliderAdapter(this, room.images)
        timelinePicker.setAvailableTimeRange(room.available)
        timelinePicker.setOnSelectedTimeRangeChangedListener { from, to ->
            selectedTimeTextView.text = getString(R.string.from_to, from.format(), to.format())
        }
        indicator.setViewPager(imageViewPager)
    }

    override fun showSuccessfulMessage(bookingResult: BookingResult) {
        submitDialog?.dismiss()
        AlertDialog.Builder(this)
                .setMessage(when (bookingResult.success) {
                    true -> R.string.submit_completed
                    false -> R.string.submit_faild
                })
                .setPositiveButton(R.string.ok, null)
                .show()
    }

    override fun showLoading() {
        loadingContainer.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loadingContainer.visibility = View.GONE
    }

    override fun showInvalidSubmitRequest() {
        val thereIsNoAttendee = bookingInfo.attendees.isEmpty()

        if (thereIsNoAttendee) {
            AlertDialog.Builder(this)
                    .setMessage(R.string.there_is_noe_attendee)
                    .setPositiveButton(R.string.ok, null)
                    .show()
        }
    }

    override fun showInvalidAttendeeFieldValues() {
        val isNameValid = !TextUtils.isEmpty(attendeeNameEditText.text.toString())
        val isPhoneValid = !TextUtils.isEmpty(attendeePhoneEditText.text.toString())
        val isEmailValid = ValidationUtil.isValidEmail(attendeeEmailEditText.text.toString())

        attendeeNameEditText.error = null
        attendeePhoneEditText.error = null
        attendeeEmailEditText.error = null

        if (!isNameValid) {
            attendeeNameEditText.error = getString(R.string.error_invalid_name)
            return
        }
        if (!isEmailValid) {
            attendeeEmailEditText.error = getString(R.string.error_invalid_email)
            return
        }
        if (!isPhoneValid) {
            attendeePhoneEditText.error = getString(R.string.error_invalid_phone)
            return
        }
    }

    override fun addAttendee(attendee: Attendee) {
        listOfAttendees.add(attendee)
        attendeeAdapter.set(listOfAttendees)
    }

    override fun onAttendeeItemClick(attendee: Attendee) {
        AlertDialog.Builder(this)
                .setMessage(getString(R.string.attendee_details,
                        attendee.name,
                        attendee.email,
                        attendee.phone))
                .setPositiveButton(R.string.ok, null)
                .show()
    }

    override fun onAttendeeItemCloseClick(attendee: Attendee) {
        listOfAttendees.remove(attendee)
        attendeeAdapter.set(listOfAttendees)
    }


    override fun onDestroy() {
        super.onDestroy()
        presenter.onViewDestroyed()
    }

    private fun SimpleTime.format() = Date().let {
        it.hours = this.hour
        it.minutes = this.minute
        TIME_FORMATTER.format(it)
    }
}