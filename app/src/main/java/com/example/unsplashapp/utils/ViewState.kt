package com.example.unsplashapp.utils

sealed class ViewState

object SuccessState: ViewState()

object ErrorState: ViewState()