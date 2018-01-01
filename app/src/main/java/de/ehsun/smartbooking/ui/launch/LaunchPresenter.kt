package de.ehsun.smartbooking.ui.launch

class LaunchPresenter : LaunchContract.Presenter {

    private lateinit var view: LaunchContract.View


    override fun onBindView(view: LaunchContract.View) {
        this.view = view
    }

    override fun onViewInitialized() {
        view.showHomeScreen()
    }

    override fun onViewDestroyed() {
    }

}