package com.sun.kh_mvvm_architect_kotlin.util

import java.util.concurrent.Executor
import java.util.concurrent.Executors


/**
 * Created by KenHoang16CDTH12 on 13/04/2019.
 * hoang.duongminh0221@gmail.com
 */
class DiskIOThreadExecutor : Executor {

    private val diskIO = Executors.newSingleThreadExecutor()

    override fun execute(command: Runnable?) { diskIO.execute(command) }
}