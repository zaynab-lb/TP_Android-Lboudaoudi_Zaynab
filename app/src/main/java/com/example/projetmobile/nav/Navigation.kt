package com.example.projetmobile.nav


import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.projetmobile.data.Entities.Product
import com.example.projetmobile.ui.product.ProductViewModel
import com.example.projetmobile.ui.product.component.DetailsScreen
import com.example.projetmobile.ui.product.screens.HomeScreen

object Routes {
    const val Home = "home"
    const val ProductDetails = "productDetails"
}
@Composable
fun AppNavigation(viewModel: ProductViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.Home) {
        composable(Routes.Home) {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToDetails = { productId ->
                    navController.navigate("${Routes.ProductDetails}/$productId")
                }
            )
        }

        composable(
            "${Routes.ProductDetails}/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            var product by remember { mutableStateOf<Product?>(null) }
            var isLoading by remember { mutableStateOf(true) }

            LaunchedEffect(productId) {
                isLoading = true
                viewModel.getProductById(productId) { result ->
                    product = result
                    isLoading = false
                }
            }


            //val product = viewModel.getProductById(productId)
            /*if (product != null) {
                DetailsScreen(product = product, navController = navController)
            }*/

            when {
                isLoading -> {
                    CircularProgressIndicator()
                }

                product != null -> {
                    DetailsScreen(product = product!!, navController = navController)
                }
            }
        }
    }
}


