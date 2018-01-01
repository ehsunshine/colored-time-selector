package de.ehsun.smartbooking.ui.main

import de.ehsun.smartbooking.entities.Room
import de.ehsun.smartbooking.entities.RoomFilter
import de.ehsun.smartbooking.entities.RoomOrder
import de.ehsun.smartbooking.ui.base.BasePresenterContract
import de.ehsun.smartbooking.ui.base.BaseViewContract

interface MainContract {

    interface View : BaseViewContract {
        fun showRooms(rooms: List<Room>, showSections: Boolean)
        fun showLoading()
        fun hideLoading()
    }

    interface Presenter : BasePresenterContract<View> {
        fun onSortChanged(sort: RoomOrder)
        fun onFilterChanged(roomFilter: RoomFilter)
        fun onAddOrRemoveFilter(filter: String)
        fun getCurrentSortOrder(): RoomOrder
        fun getCurrentBookFilter(): RoomFilter
        fun onAddOrRemoveIsAvailableFilter(isAdded: Boolean)
        fun onRefreshBooks()
        fun onDateChanged(timestamp: Long)
    }
}