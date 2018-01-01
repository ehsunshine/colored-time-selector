package de.ehsun.smartbooking.utils.scheduler

import io.reactivex.Scheduler

interface SchedulerProvider {

    val ioScheduler: Scheduler

    val computationScheduler: Scheduler

    val mainScheduler: Scheduler

}