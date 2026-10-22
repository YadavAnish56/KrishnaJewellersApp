package com.krishnajewellers.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.krishnajewellers.app.ui.navigation.AppNavHost
import com.krishnajewellers.app.ui.theme.KrishnaJewellersTheme
import com.krishnajewellers.app.ui.theme.PrimaryBg
import com.krishnajewellers.app.viewmodel.AdminViewModel
import com.krishnajewellers.app.viewmodel.ChatViewModel
import com.krishnajewellers.app.viewmodel.CustomRequestViewModel
import com.krishnajewellers.app.viewmodel.ExploreViewModel
import com.krishnajewellers.app.viewmodel.HomeViewModel
import com.krishnajewellers.app.viewmodel.MainViewModel
import com.krishnajewellers.app.viewmodel.ProductDetailViewModel
import com.krishnajewellers.app.viewmodel.SavedViewModel
import com.krishnajewellers.app.viewmodel.ViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = (application as KrishnaJewellersApp).repository
        val factory = ViewModelFactory(repository)

        val mainViewModel: MainViewModel by viewModels { factory }
        val homeViewModel: HomeViewModel by viewModels { factory }
        val exploreViewModel: ExploreViewModel by viewModels { factory }
        val productDetailViewModel: ProductDetailViewModel by viewModels { factory }
        val savedViewModel: SavedViewModel by viewModels { factory }
        val customRequestViewModel: CustomRequestViewModel by viewModels { factory }
        val chatViewModel: ChatViewModel by viewModels { factory }
        val adminViewModel: AdminViewModel by viewModels { factory }

        setContent {
            KrishnaJewellersTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = PrimaryBg
                ) {
                    val navController = rememberNavController()
                    AppNavHost(
                        navController = navController,
                        mainViewModel = mainViewModel,
                        homeViewModel = homeViewModel,
                        exploreViewModel = exploreViewModel,
                        productDetailViewModel = productDetailViewModel,
                        savedViewModel = savedViewModel,
                        customRequestViewModel = customRequestViewModel,
                        chatViewModel = chatViewModel,
                        adminViewModel = adminViewModel
                    )
                }
            }
        }
    }
}
