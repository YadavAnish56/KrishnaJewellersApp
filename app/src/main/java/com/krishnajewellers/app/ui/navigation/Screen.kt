package com.krishnajewellers.app.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Login : Screen("login")
    object Main : Screen("main")

    object ProductDetail : Screen("product_detail/{productId}") {
        fun createRoute(productId: String) = "product_detail/$productId"
    }

    object Chat : Screen("chat/{conversationId}") {
        fun createRoute(conversationId: String) = "chat/$conversationId"
    }

    object CustomRequest : Screen("custom_request")
    object AdminAuth : Screen("admin_auth")
    object AdminDashboard : Screen("admin_dashboard")
    object AdminManageRates : Screen("admin_manage_rates")
    object AdminManageProducts : Screen("admin_manage_products")
    object AdminManageEnquiries : Screen("admin_manage_enquiries")
    object AdminManageCustomRequests : Screen("admin_manage_custom_requests")
}

sealed class BottomBarTab(
    val route: String,
    val title: String,
    val outlinedIconRes: Int,
    val filledIconRes: Int
) {
    object Home : BottomBarTab("tab_home", "Home", 0, 0)
    object Explore : BottomBarTab("tab_explore", "Explore", 0, 0)
    object Saved : BottomBarTab("tab_saved", "Saved", 0, 0)
    object Enquiries : BottomBarTab("tab_enquiries", "Enquiries", 0, 0)
    object Profile : BottomBarTab("tab_profile", "Profile", 0, 0)
}
