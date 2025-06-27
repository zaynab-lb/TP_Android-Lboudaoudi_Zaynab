package com.example.projetmobile.ui.product.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.projetmobile.data.Entities.Product
import com.example.projetmobile.ui.menu.component.AppMenu
import com.example.projetmobile.ui.product.ProductViewModel
import com.example.projetmobile.ui.user.AuthViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductScreen(
    productId: String,
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel(),
    viewModel: ProductViewModel = hiltViewModel()
) {
    var product by remember { mutableStateOf<Product?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(productId) {
        viewModel.getProductById(productId) {
            product = it
        }
    }

    product?.let { currentProduct ->
        var title by remember { mutableStateOf(currentProduct.productTitle ?: "") }
        var price by remember { mutableStateOf(currentProduct.productPrice.toString()) }
        var quantity by remember { mutableStateOf(currentProduct.productQuantity.toString()) }
        var description by remember { mutableStateOf(currentProduct.productDescription ?: "") }
        var category by remember { mutableStateOf(currentProduct.productCategory ?: "") }

        val categories = listOf(
            "Collier", "Bracelet", "Bague", "Boucles d’oreilles", "Montre", "Parure", "Cheville"
        )

        Scaffold(
            topBar = {
                Column {
                    AppMenu(
                        navController = navController,
                        authViewModel = authViewModel,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            containerColor = Color(0xFFE1F5FE) // fond harmonisé
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Titre") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Prix") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("Quantité") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )

                var expanded by remember { mutableStateOf(false) }

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Catégorie") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        categories.forEach {
                            DropdownMenuItem(
                                text = { Text(it) },
                                onClick = {
                                    category = it
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                viewModel.updateProduct(
                                    currentProduct.copy(
                                        productTitle = title,
                                        productPrice = price.toDoubleOrNull() ?: 0.0,
                                        productQuantity = quantity.toIntOrNull() ?: 0,
                                        productDescription = description,
                                        productCategory = category
                                    )
                                )
                                navController.popBackStack()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF0288D1),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Enregistrer")
                    }

                    Button(
                        onClick = { navController.popBackStack() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFD32F2F),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Annuler")
                    }


                }
            }
        }
    } ?: run {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color(0xFF0288D1))
        }
    }
}
