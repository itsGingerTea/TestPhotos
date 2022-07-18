package com.example.unsplashapp.utils


sealed class ApiState<out Any>

class Success<T>(val data: T) : ApiState<T>()

object Error : ApiState<Nothing>()

object Loading : ApiState<Nothing>()
