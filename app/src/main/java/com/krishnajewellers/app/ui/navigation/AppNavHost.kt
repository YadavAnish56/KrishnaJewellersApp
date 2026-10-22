package com.krishnajewellers.app.ui.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.krishnajewellers.app.ui.screens.admin.AdminAuthScreen
import com.krishnajewellers.app.ui.screens.admin.AdminDashboardScreen
import com.krishnajewellers.app.ui.screens.admin.ManageCustomRequestsScreen
import com.krishnajewellers.app.ui.screens.admin.ManageProductsScreen
import com.krishnajewellers.app.ui.screens.admin.ManageRatesScreen
import com.krishnajewellers.app.ui.screens.auth.LoginScreen
import com.krishnajewellers.app.ui.screens.chat.ChatScreen
import com.krishnajewellers.app.ui.screens.custom.CustomRequestScreen
import com.krishnajewellers.app.ui.screens.enquiry.EnquiryListScreen
import com.krishnajewellers.app.ui.screens.explore.ExploreScreen
import com.krishnajewellers.app.ui.screens.home.HomeScreen
import com.krishnajewellers.app.ui.screens.onboarding.OnboardingScreen
import com.krishnajewellers.app.ui.screens.product.ProductDetailScreen
import com.krishnajewellers.app.ui.screens.profile.ProfileScreen
import com.krishnajewellers.app.ui.screens.saved.SavedScreen
import com.krishnajewellers.app.ui.screens.splash.SplashScreen
import com.krishnajewellers.app.ui.theme.PrimaryBg
import com.krishnajewellers.app.viewmodel.AdminViewModel
import com.krishnajewellers.app.viewmodel.ChatViewModel
import com.krishnajewellers.app.viewmodel.CustomRequestViewModel
import com.krishnajewellers.app.viewmodel.ExploreViewModel
import com.krishnajewellers.app.viewmodel.HomeViewModel
import com.krishnajewellers.app.viewmodel.MainViewModel
import com.krishnajewellers.app.viewmodel.ProductDetailViewModel
import com.krishnajewellers.app.viewmodel.SavedViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    homeViewModel: HomeViewModel,
    exploreViewModel: ExploreViewModel,
    productDetailViewModel: ProductDetailViewModel,
    savedViewModel: SavedViewModel,
    customRequestViewModel: CustomRequestViewModel,
    chatViewModel: ChatViewModel,
    adminViewModel: AdminViewModel
) {
    val isOnboardingDone by mainViewModel.isOnboardingCompleted.collectAsState()
    val isLoggedIn by mainViewModel.isLoggedIn.collectAsState()
    val isGuest by mainViewModel.isGuest.collectAsState()
    val userName by mainViewModel.userName.collectAsState()
    val userPhone by mainViewModel.userPhone.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        // 1. Splash Screen
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateNext = {
                    if (!isOnboardingDone) {
                        navController.navigate(Screen.Onboarding.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    } else if (!isLoggedIn && !isGuest) {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Screen.Main.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                }
            )
        }

        // 2. Onboarding Screen
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onFinished = {
                    mainViewModel.completeOnboarding()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        // 3. Login Screen
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { name, email, phone ->
                    mainViewModel.loginDemo(name, email, phone)
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onContinueAsGuest = {
                    mainViewModel.continueAsGuest()
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToStaffPortal = {
                    navController.navigate(Screen.AdminAuth.route)
                }
            )
        }

        // 4. Main Customer Container with Bottom Navigation
        composable(Screen.Main.route) {
            var selectedTab by rememberSaveable { mutableStateOf("home") }
            val savedCount by mainViewModel.savedCount.collectAsState()
            val unreadCount by mainViewModel.unreadEnquiryCount.collectAsState()

            Scaffold(
                bottomBar = {
                    LuxuryBottomNavigationBar(
                        currentTab = selectedTab,
                        onTabSelected = { selectedTab = it },
                        savedCount = savedCount,
                        unreadEnquiries = unreadCount
                    )
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .background(PrimaryBg)
                ) {
                    AnimatedContent(
                        targetState = selectedTab,
                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                        label = "TabContent"
                    ) { tab ->
                        when (tab) {
                            "home" -> HomeScreen(
                                viewModel = homeViewModel,
                                onNavigateToProduct = { id ->
                                    navController.navigate(Screen.ProductDetail.createRoute(id))
                                },
                                onNavigateToExplore = { metal ->
                                    exploreViewModel.selectMetal(metal)
                                    selectedTab = "explore"
                                },
                                onNavigateToCustomRequest = {
                                    navController.navigate(Screen.CustomRequest.route)
                                },
                                onNavigateToSaved = {
                                    selectedTab = "saved"
                                },
                                onNavigateToProfile = {
                                    selectedTab = "profile"
                                },
                                onNavigateToEnquiryChat = { convId ->
                                    navController.navigate(Screen.Chat.createRoute(convId))
                                }
                            )
                            "explore" -> ExploreScreen(
                                viewModel = exploreViewModel,
                                onNavigateToProduct = { id ->
                                    navController.navigate(Screen.ProductDetail.createRoute(id))
                                },
                                onNavigateToEnquiryChat = { convId ->
                                    navController.navigate(Screen.Chat.createRoute(convId))
                                }
                            )
                            "saved" -> SavedScreen(
                                viewModel = savedViewModel,
                                onNavigateToProduct = { id ->
                                    navController.navigate(Screen.ProductDetail.createRoute(id))
                                },
                                onNavigateToExplore = {
                                    selectedTab = "explore"
                                },
                                onNavigateToEnquiryChat = { convId ->
                                    navController.navigate(Screen.Chat.createRoute(convId))
                                }
                            )
                            "enquiries" -> EnquiryListScreen(
                                viewModel = chatViewModel,
                                onNavigateToChat = { convId ->
                                    navController.navigate(Screen.Chat.createRoute(convId))
                                },
                                onNavigateToExplore = {
                                    selectedTab = "explore"
                                }
                            )
                            "profile" -> ProfileScreen(
                                mainViewModel = mainViewModel,
                                onNavigateToSaved = { selectedTab = "saved" },
                                onNavigateToEnquiries = { selectedTab = "enquiries" },
                                onNavigateToCustomRequests = {
                                    navController.navigate(Screen.CustomRequest.route)
                                },
                                onNavigateToAdminDashboard = {
                                    navController.navigate(Screen.AdminAuth.route)
                                },
                                onLogout = {
                                    mainViewModel.logout()
                                    navController.navigate(Screen.Login.route) {
                                        popUpTo(Screen.Main.route) { inclusive = true }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }

        // 5. Product Detail Screen
        composable(
            route = Screen.ProductDetail.route,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val pId = backStackEntry.arguments?.getString("productId") ?: ""
            ProductDetailScreen(
                productId = pId,
                viewModel = productDetailViewModel,
                onBackClick = { navController.popBackStack() },
                onNavigateToEnquiryChat = { convId ->
                    navController.navigate(Screen.Chat.createRoute(convId))
                }
            )
        }

        // 6. Chat Screen
        composable(
            route = Screen.Chat.route,
            arguments = listOf(navArgument("conversationId") { type = NavType.StringType })
        ) { backStackEntry ->
            val convId = backStackEntry.arguments?.getString("conversationId") ?: ""
            ChatScreen(
                conversationId = convId,
                viewModel = chatViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        // 7. Custom Gold Design Request Screen
        composable(Screen.CustomRequest.route) {
            CustomRequestScreen(
                viewModel = customRequestViewModel,
                userName = userName,
                userPhone = userPhone,
                onBackClick = { navController.popBackStack() }
            )
        }

        // 8. Admin Authentication Passcode Screen
        composable(Screen.AdminAuth.route) {
            AdminAuthScreen(
                onAuthSuccess = {
                    mainViewModel.toggleAdminMode(true)
                    navController.navigate(Screen.AdminDashboard.route) {
                        popUpTo(Screen.AdminAuth.route) { inclusive = true }
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        // 9. Admin Dashboard Screen
        composable(Screen.AdminDashboard.route) {
            AdminDashboardScreen(
                viewModel = adminViewModel,
                onBackClick = {
                    mainViewModel.toggleAdminMode(false)
                    navController.popBackStack()
                },
                onNavigateManageRates = { navController.navigate(Screen.AdminManageRates.route) },
                onNavigateManageProducts = { navController.navigate(Screen.AdminManageProducts.route) },
                onNavigateManageEnquiries = { navController.navigate(Screen.AdminManageEnquiries.route) },
                onNavigateManageCustomRequests = { navController.navigate(Screen.AdminManageCustomRequests.route) }
            )
        }

        // 9. Admin: Manage Rates Screen
        composable(Screen.AdminManageRates.route) {
            ManageRatesScreen(
                viewModel = adminViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        // 10. Admin: Manage Products Screen
        composable(Screen.AdminManageProducts.route) {
            ManageProductsScreen(
                viewModel = adminViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        // 11. Admin: Manage Enquiries Screen
        composable(Screen.AdminManageEnquiries.route) {
            EnquiryListScreen(
                viewModel = chatViewModel,
                onNavigateToChat = { convId ->
                    navController.navigate(Screen.Chat.createRoute(convId))
                },
                onNavigateToExplore = { navController.popBackStack() }
            )
        }

        // 12. Admin: Manage Custom Requests Screen
        composable(Screen.AdminManageCustomRequests.route) {
            ManageCustomRequestsScreen(
                viewModel = adminViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
