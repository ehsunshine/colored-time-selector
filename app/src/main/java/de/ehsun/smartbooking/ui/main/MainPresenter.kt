package de.ehsun.smartbooking.ui.main

import de.ehsun.smartbooking.entities.RoomFilter
import de.ehsun.smartbooking.entities.RoomOrder
import de.ehsun.smartbooking.entities.RoomRequest
import de.ehsun.smartbooking.model.repository.room.RoomRepository
import de.ehsun.smartbooking.utils.scheduler.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import org.joda.time.DateTime

class MainPresenter(private var roomRepository: RoomRepository,
                    private var schedulerProvider: SchedulerProvider) : MainContract.Presenter {

    private lateinit var view: MainContract.View
    private val compositeDisposable = CompositeDisposable()

    private var sortOrder: RoomOrder = RoomOrder.LOCATION
    private var roomFilter: RoomFilter = RoomFilter.ALL_BOOKS

    override fun onBindView(view: MainContract.View) {
        this.view = view
    }

    override fun onViewInitialized() {
        showBooks(timestamp, sortOrder, roomFilter, requestFreshDate = true)
    }

    private fun showBooks(timestamp: Long, sortOrder: RoomOrder, roomFilter: RoomFilter, requestFreshDate: Boolean) {
        view.showLoading()
        compositeDisposable += roomRepository.getRooms(RoomRequest(timestamp), sortOrder, roomFilter, requestFreshDate)
                .subscribeOn(schedulerProvider.ioScheduler)
                .observeOn(schedulerProvider.mainScheduler)
                .subscribe({ rooms ->
                    view.showRooms(rooms, sortOrder == RoomOrder.LOCATION)
                    view.hideLoading()
                }, { throwable -> view.showErrorMessage(throwable.localizedMessage) })
    }

    override fun onViewDestroyed() {
        compositeDisposable.clear()
    }

    private var timestamp: Long = DateTime().millis

    override fun onRefreshBooks() {
        showBooks(timestamp, sortOrder, roomFilter, requestFreshDate = true)
    }

    override fun onSortChanged(sort: RoomOrder) {
        this.sortOrder = sort
        showBooks(timestamp, sort, roomFilter, requestFreshDate = false)
    }

    override fun onFilterChanged(roomFilter: RoomFilter) {
        this.roomFilter = roomFilter
        showBooks(timestamp, sortOrder, roomFilter, requestFreshDate = false)
    }

    override fun onAddOrRemoveFilter(filter: String) {
        roomFilter = roomFilter.copy(name = filter)
        showBooks(timestamp, sortOrder, roomFilter, requestFreshDate = false)

    }

    override fun onAddOrRemoveIsAvailableFilter(isAdded: Boolean) {
        roomFilter = roomFilter.copy(isAvailable = isAdded)
        showBooks(timestamp, sortOrder, roomFilter, requestFreshDate = false)
    }

    override fun onDateChanged(timestamp: Long) {
        this.timestamp = timestamp
        showBooks(timestamp, sortOrder, roomFilter, requestFreshDate = true)
    }

    override fun getCurrentSortOrder(): RoomOrder = sortOrder

    override fun getCurrentBookFilter(): RoomFilter = roomFilter
}