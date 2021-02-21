package com.klim.habrareader.domain

abstract class UseCaseBase {

    open class CompleteStatus(val isSuccessful: Boolean = false, val data: Any? = null) {

    }
}
