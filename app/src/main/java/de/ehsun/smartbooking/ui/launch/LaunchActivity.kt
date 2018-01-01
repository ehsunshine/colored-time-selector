package de.ehsun.smartbooking.ui.launch

import android.os.Bundle
import de.ehsun.smartbooking.R
import de.ehsun.smartbooking.ui.base.BaseActivity
import de.ehsun.smartbooking.ui.main.MainActivity
import javax.inject.Inject

class LaunchActivity : BaseActivity(), LaunchContract.View {

    @Inject lateinit var presenter: LaunchContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)
    }

    override fun injectDependencies() {
        applicationComponent.inject(this)
        presenter.onBindView(this)
    }

    override fun initViews() {
        presenter.onViewInitialized()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onViewDestroyed()
    }

    override fun showHomeScreen() {
        startActivity(MainActivity.create(this))
        finish()
    }
}