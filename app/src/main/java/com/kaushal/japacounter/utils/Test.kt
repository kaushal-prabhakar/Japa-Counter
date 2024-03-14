package com.kaushal.japacounter.utils

import timber.log.Timber


interface Test {

    fun check() {
        Timber.i("test 1")
    }
    fun attend()

    fun applyFor()
}
