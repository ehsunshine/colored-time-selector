package de.ehsun.smartbooking.ui.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import de.ehsun.smartbooking.ApplicationComponent
import de.ehsun.smartbooking.BookLibraryApplication

abstract class BaseActivity : AppCompatActivity(), BaseViewContract {

    protected val applicationComponent: ApplicationComponent by lazy {
        BookLibraryApplication.instance.applicationComponent
    }

    protected abstract fun injectDependencies()

    protected abstract fun initViews()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies()
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        initViews()
    }

    override fun showErrorMessage(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
    }

    override fun showErrorMessage(errorMessageResourceId: Int) {
        Toast.makeText(this, errorMessageResourceId, Toast.LENGTH_LONG).show()
    }

}