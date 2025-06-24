package com.example.projetmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import com.example.projetmobile.ui.product.ProductViewModel
import com.example.projetmobile.nav.AppNavigation
import com.example.projetmobile.ui.theme.ProjetMobileTheme
import com.example.projetmobile.ui.user.AuthViewModel
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels<AuthViewModel>()
    private val productViewModel: ProductViewModel by viewModels<ProductViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        enableEdgeToEdge()
        setContent {
            ProjetMobileTheme {
                Surface {
                    AppNavigation(authViewModel, productViewModel)
                }
            }
        }
    }
}