package com.example.projetmobile.ui.product.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.projetmobile.data.Entities.Product
import com.example.projetmobile.ui.product.ProductViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductScreen(
    productId: String,
    navController: NavController,
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
            topBar = { TopAppBar(title = { Text("Modifier Produit") }) }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Titre") })
                OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Prix") }, keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number))
                OutlinedTextField(value = quantity, onValueChange = { quantity = it }, label = { Text("Quantité") }, keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number))
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") })

                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Catégorie") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        categories.forEach {
                            DropdownMenuItem(text = { Text(it) }, onClick = {
                                category = it
                                expanded = false
                            })
                        }
                    }
                }

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
                        }
                    ) {
                        Text("Enregistrer")
                    }

                    OutlinedButton(
                        onClick = {
                            navController.popBackStack() // Annuler les modifications
                        }
                    ) {
                        Text("Annuler")
                    }
                }
            }
        }
    } ?: run {
        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
    }
}
