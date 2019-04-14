package com.sun.kh_mvvm_architect_kotlin.util

import androidx.test.espresso.IdlingResource
import java.util.concurrent.atomic.AtomicInteger


/**
 * Created by KenHoang16CDTH12 on 13/04/2019.
 * hoang.duongminh0221@gmail.com
 */
class SimpleCountingIdlingResource(private val resourceName: String) : IdlingResource {

    private val counter = AtomicInteger(0)

    // written from main thread, read from any thread.
    @Volatile private var resourceCallback: IdlingResource.ResourceCallback? = null

    override fun getName() = resourceName

    override fun isIdleNow() = counter.get() == 0

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.resourceCallback = resourceCallback
    }

    fun increment() { counter.getAndIncrement() }

    fun decrement() {
        val counterVal = counter.decrementAndGet()
        if (counterVal == 0)
            // we've gone from non-zero to zero. That means we're idle now! Tell espresso.
            resourceCallback?.onTransitionToIdle()
        else if (counterVal < 0)
            throw IllegalAccessException("Counter has been corrupted!")
    }
}