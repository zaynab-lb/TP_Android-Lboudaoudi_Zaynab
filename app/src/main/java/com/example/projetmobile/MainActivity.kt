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

class MainActivity : ComponentActivity() {
    private val viewModel: ProductViewModel by viewModels<ProductViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjetMobileTheme {
                Surface {
                    AppNavigation(viewModel)
                }
            }
        }
    }
}