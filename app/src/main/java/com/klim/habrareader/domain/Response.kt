package com.klim.habrareader.domain

sealed class Response<D>() {

    class Success<D>(val data: D) : Response<D>()

    class Error<D>(val code: Int) : Response<D>()
}