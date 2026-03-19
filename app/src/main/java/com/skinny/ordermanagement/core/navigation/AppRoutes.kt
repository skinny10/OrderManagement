package com.skinny.ordermanagement.core.navigation

sealed class AppRoutes(val route: String) {

    sealed class Auth(route: String) : AppRoutes(route) {
        object Login    : Auth("auth/login")
        object Register : Auth("auth/register")
    }
}