package de.ehsun.smartbooking.ui.roomdetails

import de.ehsun.smartbooking.entities.Attendee
import de.ehsun.smartbooking.entities.BookingInfo
import de.ehsun.smartbooking.entities.BookingResult
import de.ehsun.smartbooking.ui.base.BasePresenterContract
import de.ehsun.smartbooking.ui.base.BaseViewContract

interface RoomDetailsContract {
    interface View : BaseViewContract {
        fun showLoading()
        fun hideLoading()
        fun showInvalidAttendeeFieldValues()
        fun showInvalidSubmitRequest()
        fun addAttendee(attendee: Attendee)
        fun showSuccessfulMessage(bookingResult: BookingResult)
    }

    interface Presenter : BasePresenterContract<View> {
        fun onAddAttendeeRequested(name: String, email: String, phone: String)
        fun onSubmitBookingRequested(bookingInfo: BookingInfo)
    }
}