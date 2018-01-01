package de.ehsun.smartbooking.ui.launch

import de.ehsun.smartbooking.ui.base.BasePresenterContract
import de.ehsun.smartbooking.ui.base.BaseViewContract

interface LaunchContract {

    interface View : BaseViewContract {
        fun showHomeScreen()
    }

    interface Presenter : BasePresenterContract<View>
}