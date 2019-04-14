package com.sun.kh_mvvm_architect_kotlin.util


/**
 * Created by KenHoang16CDTH12 on 13/04/2019.
 * hoang.duongminh0221@gmail.com
 */
object EspressoIdlingResource {

    private val RESOURCE = "GLOBAL"

    @JvmField val countingIdlingResource = SimpleCountingIdlingResource(RESOURCE)

    fun increment() { countingIdlingResource.increment() }

    fun decrement() { countingIdlingResource.decrement() }
}