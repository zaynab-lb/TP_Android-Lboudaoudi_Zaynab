package com.example.projetmobile.ui.order.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.projetmobile.nav.Routes
import com.example.projetmobile.ui.cart.CartViewModel
import com.example.projetmobile.ui.product.ProductViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar

@Composable
fun CheckoutScreen(
    cartViewModel: CartViewModel,
    productViewModel: ProductViewModel,
    navController: NavController
) {
    var name by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }

    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    val cartItems by cartViewModel.cartItems.collectAsState()
    val errorMessage by cartViewModel.errorMessage.collectAsState()

    var nameError by remember { mutableStateOf(false) }
    var addressError by remember { mutableStateOf(false) }
    var cardNumberError by remember { mutableStateOf(false) }
    var expiryDateError by remember { mutableStateOf(false) }
    var cvvError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Informations de livraison", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
                nameError = false
            },
            label = { Text("Nom complet") },
            modifier = Modifier.fillMaxWidth(),
            isError = nameError,
            supportingText = { if (nameError) Text("Ce champ est obligatoire") }
        )

        OutlinedTextField(
            value = address,
            onValueChange = {
                address = it
                addressError = false
            },
            label = { Text("Adresse complète") },
            modifier = Modifier.fillMaxWidth(),
            isError = addressError,
            supportingText = { if (addressError) Text("Ce champ est obligatoire") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Informations de paiement", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = cardNumber,
            onValueChange = {
                val digitsOnly = it.filter { c -> c.isDigit() }
                if (digitsOnly.length <= 16) {
                    cardNumber = digitsOnly
                    cardNumberError = false
                }
            },
            label = { Text("Numéro de carte") },
            modifier = Modifier.fillMaxWidth(),
            isError = cardNumberError,
            supportingText = {
                when {
                    cardNumberError && cardNumber.isEmpty() -> Text("Ce champ est obligatoire")
                    cardNumberError -> Text("Numéro de carte invalide (16 chiffres)")
                    else -> {}
                }
            },
            visualTransformation = CreditCardFilter,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = expiryDate,
                onValueChange = { newValue ->
                    val digitsOnly = newValue.filter { it.isDigit() }
                    when {
                        digitsOnly.isEmpty() -> {
                            expiryDate = ""
                            expiryDateError = false
                        }
                        digitsOnly.length <= 4 -> {
                            val formatted = when {
                                digitsOnly.length <= 2 -> digitsOnly
                                else -> "${digitsOnly.take(2)}/${digitsOnly.drop(2)}"
                            }
                            expiryDate = formatted
                            expiryDateError = false
                        }
                    }
                },
                label = { Text("Date d'expiration (MM/AA)") },
                modifier = Modifier.weight(1f),
                isError = expiryDateError,
                supportingText = {
                    when {
                        expiryDateError && expiryDate.isEmpty() -> Text("Ce champ est obligatoire")
                        expiryDateError -> Text("Format invalide ou carte expirée")
                        else -> {}
                    }
                },
                visualTransformation = ExpiryDateFilter,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                placeholder = { Text("MM/AA") }
            )

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedTextField(
                value = cvv,
                onValueChange = {
                    if (it.length <= 3 && it.all { c -> c.isDigit() }) {
                        cvv = it
                        cvvError = false
                    }
                },
                label = { Text("CVV") },
                modifier = Modifier.weight(1f),
                isError = cvvError,
                supportingText = {
                    when {
                        cvvError && cvv.isEmpty() -> Text("Ce champ est obligatoire")
                        cvvError -> Text("3 chiffres requis")
                        else -> {}
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                // Validation
                nameError = name.isBlank()
                addressError = address.isBlank()
                cardNumberError = cardNumber.length != 16
                expiryDateError = !isValidExpiryDate(expiryDate)
                cvvError = cvv.length != 3

                if (!nameError && !addressError && !cardNumberError &&
                    !expiryDateError && !cvvError
                ) {
                    cartViewModel.validateOrder(
                        userId = userId,
                        name = name,
                        address = address
                    ) {
                        navController.popBackStack(Routes.ClientHome, false)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Confirmer la commande")
        }

        errorMessage?.let { message ->
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

val CreditCardFilter = VisualTransformation { text ->
    val original = text.text.take(16)
    val formatted = StringBuilder()
    val offsetMap = mutableListOf<Int>()

    var currentIndex = 0
    for (i in original.indices) {
        if (i > 0 && i % 4 == 0) {
            formatted.append(' ')
            currentIndex++
        }
        formatted.append(original[i])
        offsetMap.add(i + currentIndex)
    }

    val offsetMapping = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            if (offset <= 0) return 0
            if (offset >= offsetMap.size) return formatted.length
            return offsetMap[offset]
        }

        override fun transformedToOriginal(offset: Int): Int {
            val index = offsetMap.indexOfFirst { it >= offset }
            return if (index == -1) original.length else index
        }
    }

    TransformedText(AnnotatedString(formatted.toString()), offsetMapping)
}

val ExpiryDateFilter = VisualTransformation { text ->
    val digits = text.text.filter { it.isDigit() }.take(4)
    val formatted = when {
        digits.isEmpty() -> ""
        digits.length <= 2 -> digits
        else -> "${digits.take(2)}/${digits.drop(2)}"
    }
    TransformedText(AnnotatedString(formatted), OffsetMapping.Identity)
}

fun isValidExpiryDate(date: String): Boolean {
    if (!date.matches(Regex("^\\d{2}/\\d{2}$"))) return false

    val parts = date.split("/")
    val month = parts[0].toIntOrNull() ?: return false
    val year = parts[1].toIntOrNull() ?: return false

    // Validation du mois (1-12)
    if (month !in 1..12) return false

    // Validation par rapport à l'année courante
    val currentYear = Calendar.getInstance().get(Calendar.YEAR) % 100
    val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1

    return when {
        year > currentYear -> true
        year == currentYear -> month >= currentMonth
        else -> false
    }
}