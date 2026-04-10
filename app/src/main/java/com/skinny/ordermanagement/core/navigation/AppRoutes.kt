package com.skinny.ordermanagement.core.navigation

sealed class AppRoutes(val route: String) {

    sealed class Auth(route: String) : AppRoutes(route) {
        object Login    : Auth("auth/login")
        object Register : Auth("auth/register")
    }

    sealed class Admin(route: String) : AppRoutes(route) {
        object Dashboard : Admin("admin/dashboard")
        object Orders    : Admin("admin/orders")
        object Clients   : Admin("admin/clients")
        object Users     : Admin("admin/users")
    }

    sealed class Seller(route: String) : AppRoutes(route) {
        object Home : Seller("seller/home")
    }

    sealed class Delivery(route: String) : AppRoutes(route) {
        object Home : Delivery("delivery/home")
    }
}

