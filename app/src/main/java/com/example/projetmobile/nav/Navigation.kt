package com.example.projetmobile.nav


import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.projetmobile.data.Entities.Product
import com.example.projetmobile.ui.cart.CartViewModel
import com.example.projetmobile.ui.cart.screens.CartScreen
import com.example.projetmobile.ui.order.screens.CheckoutScreen
import com.example.projetmobile.ui.product.ProductViewModel
import com.example.projetmobile.ui.product.component.DetailsScreen
import com.example.projetmobile.ui.product.screens.HomeScreen
import com.example.projetmobile.ui.user.screens.AdminHomeScreen
import com.example.projetmobile.ui.user.screens.LoginScreen
import com.example.projetmobile.ui.user.screens.SignupScreen
import com.google.firebase.auth.FirebaseAuth

object Routes {
    const val Home = "home"
    const val Login = "login"
    const val AdminHome = "adminHome"
    const val ClientHome = "clientHome"
    const val ProductDetails = "productDetails"
    const val CartScreen = "cart"
    const val Checkout = "checkout"
}
@Composable
fun AppNavigation(productViewModel: ProductViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.Login) {

        composable(Routes.Login) {
            LoginScreen(
                onClientLogin = { navController.navigate(Routes.ClientHome) },
                onAdminLogin = { navController.navigate(Routes.AdminHome) },
                navController = navController
            )
        }

        composable("signup") {
            SignupScreen(
                onSignupSuccess = { navController.navigate(Routes.ClientHome) },
                navController = navController
            )
        }

        composable(Routes.ClientHome) {
            HomeScreen(
                viewModel = productViewModel,
                authViewModel = hiltViewModel(),
                navController = navController,
                onNavigateToDetails = { productId ->
                    navController.navigate("${Routes.ProductDetails}/$productId")
                }
            )
        }

        composable(Routes.AdminHome) {
            AdminHomeScreen(navController = navController)
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
                productViewModel.getProductById(productId) { result ->
                    product = result
                    isLoading = false
                }
            }




            when {
                isLoading -> {
                    CircularProgressIndicator()
                }

                product != null -> {
                    DetailsScreen(product = product!!, navController = navController)
                }
            }
        }

        composable(Routes.CartScreen) {
            val cartViewModel: CartViewModel = hiltViewModel()
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
            LaunchedEffect(Unit) { cartViewModel.loadCart(userId) }
            CartScreen(cartViewModel = cartViewModel, navController = navController)
        }

        composable(Routes.Checkout) {
            val cartViewModel: CartViewModel = hiltViewModel()
            CheckoutScreen(
                cartViewModel = cartViewModel,
                productViewModel = productViewModel,
                navController = navController
            )
        }
    }
}


