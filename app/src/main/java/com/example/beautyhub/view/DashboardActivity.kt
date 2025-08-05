package com.example.beautyhub.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.beautyhub.R
import com.example.beautyhub.repository.UserRepositoryImpl
import com.example.beautyhub.ui.theme.*
import com.example.beautyhub.viewmodel.UserViewModel
import com.example.beautyhub.model.ProductModel
import com.example.beautyhub.repository.ProductRepositoryImpl
import coil.compose.AsyncImage

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BeautyHubTheme {
                DashboardScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen() {
    val context = LocalContext.current
    val repo = remember { UserRepositoryImpl() }
    val userViewModel = remember { UserViewModel(repo) }

    // State management
    var selectedTab by remember { mutableStateOf(0) }
    var userName by remember { mutableStateOf("User") }
    var userEmail by remember { mutableStateOf("") }
    var userPhone by remember { mutableStateOf("") }
    var userAddress by remember { mutableStateOf("") }
    var isUserDataLoaded by remember { mutableStateOf(false) }

    // Beauty Hub theme colors
    val primaryColor = PrimaryPeach
    val secondaryColor = PrimaryLightPeach
    val backgroundColor = BackgroundPeach
    val surfaceColor = SurfacePeach
    val onPrimaryColor = TextOnPeach
    val onSurfaceColor = PrimaryText
    val secondaryTextColor = SecondaryText

    // Data loading
    LaunchedEffect(Unit) {
        val currentUser = userViewModel.getCurrentUser()
        if (currentUser != null) {
            userViewModel.getUserByID(currentUser.uid)

            repo.getUserByID(currentUser.uid) { user, success, message ->
                if (success && user != null) {
                    userName = user.fullName.ifEmpty { "Beauty Lover" }
                    userEmail = user.email
                    userPhone = user.phoneNumber
                    userAddress = user.address
                    isUserDataLoaded = true
                }
            }
        }
    }

    fun navigateToAddProduct() {
        val intent = Intent(context, AddProductActivity::class.java)
        context.startActivity(intent)
    }

    fun navigateToViewAddedProduct() {
        val intent = Intent(context, ViewProductActivity::class.java)
        context.startActivity(intent)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when (selectedTab) {
                            0 -> "✨ Beauty Sales"
                            1 -> "🔍 Search"
                            2 -> "👤 Profile"
                            else -> "✨ Beauty Sales"
                        },
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                actions = {
                    if (selectedTab == 0) {
                        IconButton(onClick = { /* Handle notifications */ }) {
                            Icon(
                                Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = Color.White
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryColor
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                contentColor = primaryColor
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = primaryColor,
                        selectedTextColor = primaryColor,
                        unselectedIconColor = MediumGray,
                        unselectedTextColor = MediumGray
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.List, contentDescription = "Products") },
                    label = { Text("Products") },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = primaryColor,
                        selectedTextColor = primaryColor,
                        unselectedIconColor = MediumGray,
                        unselectedTextColor = MediumGray
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    label = { Text("Search") },
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = primaryColor,
                        selectedTextColor = primaryColor,
                        unselectedIconColor = MediumGray,
                        unselectedTextColor = MediumGray
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = primaryColor,
                        selectedTextColor = primaryColor,
                        unselectedIconColor = MediumGray,
                        unselectedTextColor = MediumGray
                    )
                )
            }
        }
    ) { paddingValues ->
        when (selectedTab) {
            0 -> HomeScreen(
                paddingValues = paddingValues,
                primaryColor = primaryColor,
                secondaryColor = PrimaryPeach,
                backgroundColor = BackgroundPeach,
                navigateToAddProduct = ::navigateToAddProduct,
                navigateToViewAddedProduct = ::navigateToViewAddedProduct,
                userName = userName
            )
            1 -> ProductListScreen()
            2 -> SearchScreen(paddingValues = paddingValues, primaryColor = primaryColor)
            3 -> ProfileScreen(
                paddingValues = paddingValues,
                primaryColor = primaryColor,
                userName = userName,
                userEmail = userEmail,
                userPhone = userPhone,
                userAddress = userAddress,
                isUserDataLoaded = isUserDataLoaded,
                userViewModel = userViewModel
            )
        }
    }
}

@Composable
fun HomeScreen(
    paddingValues: PaddingValues,
    primaryColor: Color,
    secondaryColor: Color,
    backgroundColor: Color,
    navigateToAddProduct: () -> Unit,
    navigateToViewAddedProduct: () -> Unit,
    userName: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(backgroundColor)
    ) {
        // Welcome Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = primaryColor),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Welcome Back, $userName! ",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Discover premium beauty collection",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .background(Color.White.copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.bp),
                        contentDescription = "Profile",
                        modifier = Modifier
                            .size(160.dp)
                            .clip(CircleShape)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))


        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.bbbb),
                    contentDescription = "Premium Beauty Collection",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Gradient overlay for better text visibility
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            androidx.compose.ui.graphics.Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.7f)
                                )
                            )
                        )
                )

                // Text overlay on image
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(20.dp)
                ) {
                    Text(
                        "Beauty Products",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        "Crafted with precision & elegance",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 2. View Added Product Button (Second Position)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable { navigateToViewAddedProduct() },
            colors = CardDefaults.cardColors(
                containerColor = primaryColor.copy(alpha = 0.1f)
            ),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(primaryColor.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.List,
                            contentDescription = "View Products",
                            tint = primaryColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            "View Added Products",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )
                        Text(
                            "Check your added beauty items",
                            fontSize = 12.sp,
                            color = DarkText.copy(alpha = 0.7f)
                        )
                    }
                }
                Icon(
                    Icons.Default.KeyboardArrowRight,
                    contentDescription = "Navigate",
                    tint = primaryColor,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Add Product Button
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Button(
                onClick = { navigateToAddProduct() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryColor
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 0.dp
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Add Product",
                        modifier = Modifier.size(28.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Add New Product",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            "Add products to your inventory",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(paddingValues: PaddingValues, primaryColor: Color) {
    val context = LocalContext.current
    val repo = remember { UserRepositoryImpl() }
    val userViewModel = remember { UserViewModel(repo) }

    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<ProductModel>>(emptyList()) }
    var allProducts by remember { mutableStateOf<List<ProductModel>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }

    // Load actual added products
    LaunchedEffect(Unit) {
        val currentUser = userViewModel.getCurrentUser()
        if (currentUser != null) {
            try {
                // Load actual products from repository
                val productRepo = ProductRepositoryImpl()
                productRepo.getAllProduct { products, success, message ->
                    if (success && products != null) {
                        allProducts = products.filterNotNull()
                    } else {
                        hasError = true
                    }
                    isLoading = false
                }
                isLoading = false

            } catch (e: Exception) {
                hasError = true
                isLoading = false
            }
        } else {
            isLoading = false
            hasError = true
        }
    }

    // Filter products based on search query
    LaunchedEffect(searchQuery, allProducts) {
        searchResults = if (searchQuery.isBlank()) {
            emptyList()
        } else {
            allProducts.filter { product ->
                product.productName.lowercase().contains(searchQuery.lowercase()) ||
                product.category.lowercase().contains(searchQuery.lowercase()) ||
                product.description.lowercase().contains(searchQuery.lowercase())
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp)
    ) {
        // Search Header
        Text(
            "🔍 Search Added Products",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = primaryColor,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            placeholder = { Text("Search your added products...") },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Search",
                    tint = primaryColor
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = "Clear",
                            tint = MediumGray
                        )
                    }
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = primaryColor,
                focusedLabelColor = primaryColor,
                cursorColor = primaryColor
            ),
            shape = RoundedCornerShape(12.dp),
            enabled = !isLoading
        )

        // Content based on state
        when {
            isLoading -> {
                // Loading state
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        color = primaryColor,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Loading your products...",
                        fontSize = 16.sp,
                        color = MediumGray
                    )
                }
            }

            hasError -> {
                // Error state
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = "Error",
                        tint = Color.Red,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Unable to load products",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = MediumGray
                    )
                    Text(
                        "Please check your connection and try again",
                        fontSize = 14.sp,
                        color = MediumGray,
                        textAlign = TextAlign.Center
                    )
                }
            }

            allProducts.isEmpty() && searchQuery.isEmpty() -> {
                // No products added yet
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "No Products Found",
                        tint = primaryColor.copy(alpha = 0.6f),
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "No Products Added",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryColor
                    )
                    Text(
                        "You haven't added any products yet.\nStart by adding some beauty products!",
                        fontSize = 16.sp,
                        color = MediumGray,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = {
                            val intent = Intent(context, AddProductActivity::class.java)
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add Product")
                    }
                }
            }

            searchQuery.isNotEmpty() -> {
                if (searchResults.isNotEmpty()) {
                    // Search results found
                    Text(
                        "Found ${searchResults.size} product(s)",
                        fontSize = 14.sp,
                        color = MediumGray,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(searchResults.size) { index ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { /* Handle product click */ },
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AsyncImage(
                                        model = searchResults[index].image,
                                        contentDescription = "Product Image",
                                        modifier = Modifier
                                            .size(50.dp)
                                            .clip(RoundedCornerShape(8.dp)),
                                        contentScale = ContentScale.Crop,
                                        error = painterResource(R.drawable.beauty), // Fallback image
                                        placeholder = painterResource(R.drawable.beauty)
                                    )
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            searchResults[index].productName,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = DarkText
                                        )
                                        Text(
                                            "₹${searchResults[index].price} • ${searchResults[index].category}",
                                            fontSize = 12.sp,
                                            color = MediumGray
                                        )
                                    }

                                    // View Details Button
                                    Button(
                                        onClick = {
                                            // View product details functionality
                                            Toast.makeText(
                                                context,
                                                "Viewing details for ${searchResults[index].productName}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        },
                                        modifier = Modifier
                                            .height(36.dp)
                                            .width(120.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = primaryColor
                                        ),
                                        shape = RoundedCornerShape(18.dp),
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            Icon(
                                                Icons.Default.Info,
                                                contentDescription = "View Details",
                                                modifier = Modifier.size(16.dp),
                                                tint = Color.White
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                "View Details",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = Color.White
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // No search results found
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "No Results",
                            tint = MediumGray,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "No Products Found",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = MediumGray
                        )
                        Text(
                            "No products match your search \"$searchQuery\"",
                            fontSize = 14.sp,
                            color = MediumGray,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Try different keywords or check spelling",
                            fontSize = 12.sp,
                            color = MediumGray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            else -> {
                // Default state - show all products or search prompt
                if (allProducts.isNotEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search",
                            tint = primaryColor.copy(alpha = 0.6f),
                            modifier = Modifier.size(80.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Search Your Products",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryColor
                        )
                        Text(
                            "You have ${allProducts.size} product(s) added",
                            fontSize = 16.sp,
                            color = MediumGray,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            "Enter keywords to find your beauty products",
                            fontSize = 14.sp,
                            color = MediumGray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileScreen(
    paddingValues: PaddingValues,
    primaryColor: Color,
    userName: String,
    userEmail: String,
    userPhone: String,
    userAddress: String,
    isUserDataLoaded: Boolean,
    userViewModel: UserViewModel
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Profile Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = primaryColor
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.bp),
                    contentDescription = null,
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape).padding(8.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    userName,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    "✨ Premium Beauty Member ✨",
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // User Details Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    "Account Information",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                if (isUserDataLoaded) {
                    // Email
                    UserInfoRow(
                        icon = Icons.Default.Email,
                        label = "Email",
                        value = userEmail.ifEmpty { "Not provided" },
                        primaryColor = primaryColor
                    )

                    // Phone
                    UserInfoRow(
                        icon = Icons.Default.Phone,
                        label = "Phone",
                        value = userPhone.ifEmpty { "Not added" },
                        primaryColor = primaryColor
                    )

                    // Address
                    UserInfoRow(
                        icon = Icons.Default.LocationOn,
                        label = "Address",
                        value = userAddress.ifEmpty { "Not added" },
                        primaryColor = primaryColor
                    )
                } else {
                    // Loading state
                    repeat(3) {
                        UserInfoRow(
                            icon = Icons.Default.Person,
                            label = "Loading...",
                            value = "Please wait...",
                            primaryColor = primaryColor
                        )
                        if (it < 2) HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Simple Logout Button - Placed prominently
        Button(
            onClick = {
                userViewModel.logout { success, message ->
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    if (success) {
                        val intent = Intent(context, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        context.startActivity(intent)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red.copy(alpha = 0.8f)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Default.ExitToApp,
                    contentDescription = "Logout",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Logout",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Edit Profile Button
        ProfileOption(
            icon = Icons.Default.Edit,
            title = "Edit Profile",
            subtitle = "Update your personal information",
            onClick = {
                val currentUser = userViewModel.getCurrentUser()
                currentUser?.let { user ->
                    val intent = Intent(context, EditProfileActivity::class.java).apply {
                        putExtra("USER_ID", user.uid)
                        putExtra("USER_NAME", userName)
                        putExtra("USER_EMAIL", userEmail)
                        putExtra("USER_PHONE", userPhone)
                        putExtra("USER_ADDRESS", userAddress)
                    }
                    context.startActivity(intent)
                }
            }
        )

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun UserInfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    primaryColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = label,
            tint = primaryColor,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                label,
                fontSize = 12.sp,
                color = MediumGray
            )
            Text(
                value,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = DarkText
            )
        }
    }
    if (label != "Address") {
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
    }
}

@Composable
fun ProfileOption(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = Gold80,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = DarkText
                )
                Text(
                    subtitle,
                    fontSize = 12.sp,
                    color = MediumGray
                )
            }
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = null,
                tint = MediumGray
            )
        }
    }
}
