package de.ehsun.smartbooking.ui.base

interface BasePresenterContract<in T : BaseViewContract> {

    fun onBindView(view: T)

    fun onViewInitialized()

    fun onViewDestroyed()
}