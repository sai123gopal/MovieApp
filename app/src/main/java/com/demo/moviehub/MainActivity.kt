package com.demo.moviehub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.demo.moviehub.ui.theme.MovieHubTheme

sealed class Screen(val route: String, val titleResId: Int) {
    object Home : Screen("home", R.string.home)
    object Favorites : Screen("favorites", R.string.favorites)
    object Settings : Screen("settings", R.string.settings)
}

data class Movie(
    val id: Int,
    val title: String,
    val rating: Float,
    val posterPath: String? = null,
    val releaseDate: String = "",
    val overview: String = "",
    val isFavorite: Boolean = false
)

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val items = listOf(
        Screen.Home,
        Screen.Favorites,
        Screen.Settings
    )
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Home.route
    
    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = {
                            when (screen) {
                                is Screen.Home -> Icon(
                                    Icons.Default.Home,
                                    contentDescription = stringResource(id = screen.titleResId)
                                )
                                is Screen.Favorites -> Icon(
                                    Icons.Default.Favorite,
                                    contentDescription = stringResource(id = screen.titleResId)
                                )
                                is Screen.Settings -> Icon(
                                    Icons.Default.Settings,
                                    contentDescription = stringResource(id = screen.titleResId)
                                )
                            }
                        },
                        label = { Text(stringResource(id = screen.titleResId)) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen() }
            composable(Screen.Favorites.route) { FavoritesScreen() }
            composable(Screen.Settings.route) { SettingsScreen() }
        }
    }
}

@Composable
fun HomeScreen() {
    // In a real app, this would be fetched from a ViewModel
    val popularMovies = remember {
        listOf(
            Movie(
                id = 1,
                title = "Inception",
                rating = 8.8f,
                posterPath = "/9gk7adHYeDvHkCSEqAvQNLV5Uge.jpg",
                isFavorite = true
            ),
            Movie(
                id = 2,
                title = "Interstellar",
                rating = 8.6f,
                posterPath = "/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg"
            ),
            Movie(
                id = 3,
                title = "The Dark Knight",
                rating = 9.0f,
                posterPath = "/qJ2tW6WMUDux911r6m7haRef0WH.jpg"
            ),
            Movie(
                id = 4,
                title = "Pulp Fiction",
                rating = 8.9f,
                posterPath = "/d5iIlFn5s0ImszYzBPb8JPIfbXD.jpg"
            ),
            Movie(
                id = 5,
                title = "Fight Club",
                rating = 8.8f,
                posterPath = "/pB8BM7pdSp6B6Ih7QZ4DrQ3PmJK.jpg"
            )
        )
    }
    
    var selectedTimeRange by remember { mutableStateOf(0) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.most_popular),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            
            Row {
                listOf(
                    stringResource(id = R.string.today),
                    stringResource(id = R.string.this_week)
                ).forEachIndexed { index, text ->
                    FilterChip(
                        selected = selectedTimeRange == index,
                        onClick = { selectedTimeRange = index },
                        label = { Text(text) },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(popularMovies) { movie ->
                MovieItem(movie = movie)
            }
        }
    }
}

@Composable
fun MovieItem(movie: Movie) {
    Box(
        modifier = Modifier
            .width(150.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f / 3f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Gray.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text("X", style = MaterialTheme.typography.headlineMedium)
                
                IconButton(
                    onClick = { /* Toggle favorite - will be implemented with ViewModel */ },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f))
                ) {
                    Icon(
                        imageVector = if (movie.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = stringResource(
                            id = if (movie.isFavorite) R.string.remove_from_favorites else R.string.add_to_favorites
                        ),
                        tint = if (movie.isFavorite) Color.Red else Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = movie.title,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Rating",
                    tint = Color(0xFFFFD700),
                    modifier = Modifier.size(16.dp)
                )
                
                Spacer(modifier = Modifier.width(4.dp))
                
                Text(
                    text = movie.rating.toString(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun FavoritesScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = R.string.favorites),
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
fun SettingsScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = R.string.settings),
            style = MaterialTheme.typography.titleLarge
        )
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovieHubTheme {
                MainScreen()
            }
        }
    }
}

@Preview()
@Composable
fun PreviewHomeScreen() {
    MovieHubTheme {
        HomeScreen()
    }
}