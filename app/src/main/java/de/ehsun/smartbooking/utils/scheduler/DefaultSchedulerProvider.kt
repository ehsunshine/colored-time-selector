package de.ehsun.smartbooking.utils.scheduler

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DefaultSchedulerProvider : SchedulerProvider {

    override val ioScheduler: Scheduler
        get() = Schedulers.io()

    override val computationScheduler: Scheduler
        get() = Schedulers.computation()

    override val mainScheduler: Scheduler
        get() = AndroidSchedulers.mainThread()
}