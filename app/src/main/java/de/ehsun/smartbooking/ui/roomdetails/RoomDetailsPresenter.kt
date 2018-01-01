package de.ehsun.smartbooking.ui.roomdetails

import android.text.TextUtils
import de.ehsun.smartbooking.entities.Attendee
import de.ehsun.smartbooking.entities.BookingInfo
import de.ehsun.smartbooking.model.repository.booking.BookingRepository
import de.ehsun.smartbooking.utils.ValidationUtil
import de.ehsun.smartbooking.utils.scheduler.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class RoomDetailsPresenter(private var bookingRepository: BookingRepository,
                           private var schedulerProvider: SchedulerProvider) : RoomDetailsContract.Presenter {

    private lateinit var view: RoomDetailsContract.View
    private val compositeDisposable = CompositeDisposable()

    override fun onBindView(view: RoomDetailsContract.View) {
        this.view = view
    }

    override fun onViewInitialized() {
    }

    override fun onViewDestroyed() {
    }

    override fun onAddAttendeeRequested(name: String, email: String, phone: String) {
        if (!isInputFieldsValid(name, email, phone)) {
            view.showInvalidAttendeeFieldValues()
            return
        }

        val attendee = Attendee()
        attendee.name = name
        attendee.email = email
        attendee.phone = phone

        view.addAttendee(attendee)
    }

    override fun onSubmitBookingRequested(bookingInfo: BookingInfo) {
        if (bookingInfo.attendees.isEmpty()) {
            view.showInvalidSubmitRequest()
            return
        }
        view.showLoading()
        compositeDisposable += bookingRepository.saveBookingInfo(bookingInfo)
                .subscribeOn(schedulerProvider.ioScheduler)
                .observeOn(schedulerProvider.mainScheduler)
                .subscribe({ result ->
                    view.showSuccessfulMessage(result)
                    view.hideLoading()
                }, { throwable -> view.showErrorMessage(throwable.localizedMessage) })
    }


    private fun isInputFieldsValid(name: String, email: String, phone: String) =
            !TextUtils.isEmpty(name) &&
                    !TextUtils.isEmpty(phone) &&
                    ValidationUtil.isValidEmail(email)

}