package com.demo.moviehub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.demo.moviehub.ui.screens.details.MovieDetailsScreen
import com.demo.moviehub.ui.screens.details.MovieDetailsViewModel
import com.demo.moviehub.ui.screens.favorites.FavoritesScreen
import com.demo.moviehub.ui.screens.home.HomeScreen
import com.demo.moviehub.ui.theme.MovieHubTheme
import com.demo.moviehub.ui.theme.YellowRating
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovieHubTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: ""
    
    Scaffold(
        bottomBar = {
            val showBottomBar = when (currentRoute) {
                ROUTE_HOME -> true
                ROUTE_FAVORITES -> true
                ROUTE_SETTINGS -> true
                else -> false
            }
            if (showBottomBar) {
                BottomNavigationBar(navController, currentRoute)
            } else {
                // Return an empty composable when bottom bar is not needed
                Box {}
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { 
                HomeScreen(
                    onMovieClick = { movieId ->
                        navController.navigate(Screen.MovieDetails.createRoute(movieId))
                    }
                ) 
            }
            composable(Screen.Favorites.route) { 
                FavoritesScreen(
                    onMovieClick = { movieId ->
                        navController.navigate(Screen.MovieDetails.createRoute(movieId))
                    }
                ) 
            }
            composable(Screen.Settings.route) { 
                SettingsScreen() 
            }
            composable(
                route = Screen.MovieDetails.route,
                arguments = listOf(
                    navArgument("movieId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val movieId = backStackEntry.arguments?.getInt("movieId") ?: return@composable
                val viewModel: MovieDetailsViewModel = hiltViewModel(
                    backStackEntry
                )
                
                LaunchedEffect(movieId) {
                    viewModel.fetchMovieDetails(movieId)
                }
                
                MovieDetailsScreen(
                    navController = navController,
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun BottomNavigationBar(navController: NavHostController, currentRoute: String) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        val bottomNavScreens = Screen.bottomNavItems
        
        bottomNavScreens.forEach { screen ->
            val selected = when (screen.route) {
                ROUTE_HOME -> currentRoute == ROUTE_HOME || 
                            currentRoute.startsWith("$ROUTE_HOME/")
                ROUTE_FAVORITES -> currentRoute == ROUTE_FAVORITES || 
                                 currentRoute.startsWith("$ROUTE_FAVORITES/")
                ROUTE_SETTINGS -> currentRoute == ROUTE_SETTINGS || 
                                currentRoute.startsWith("$ROUTE_SETTINGS/")
                else -> false
            }
                
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = screen.title,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = { 
                    Text(
                        text = screen.title,
                        style = MaterialTheme.typography.labelSmall
                    ) 
                },
                selected = selected,
                onClick = {
                    if (!selected) {
                        navController.navigate(screen.route) {
                            popUpTo(ROUTE_HOME) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = YellowRating,
                    selectedTextColor = YellowRating,
                    indicatorColor = YellowRating.copy(alpha = 0.2f),
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f),
                    disabledTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f)
                )
            )
        }
    }
}

@Composable
fun SettingsScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = Screen.Settings.title,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

// Screen routes as constants to avoid initialization issues
private const val ROUTE_HOME = "home"
private const val ROUTE_FAVORITES = "favorites"
private const val ROUTE_SETTINGS = "settings"
private const val ROUTE_MOVIE_DETAILS = "movie/{movieId}"

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val isBottomNavItem: Boolean = true
) {
    object Home : Screen(
        route = ROUTE_HOME,
        title = "Home",
        icon = Icons.Default.Home
    )
    
    object Favorites : Screen(
        route = ROUTE_FAVORITES,
        title = "Favorites",
        icon = Icons.Default.Favorite
    )
    
    object Settings : Screen(
        route = ROUTE_SETTINGS,
        title = "Settings",
        icon = Icons.Default.Settings
    )
    
    object MovieDetails : Screen(
        route = ROUTE_MOVIE_DETAILS,
        title = "Movie Details",
        icon = Icons.Default.FavoriteBorder,
        isBottomNavItem = false
    ) {
        fun createRoute(movieId: Int) = "movie/$movieId"
    }
    
    companion object {
        val bottomNavItems by lazy {
            listOf(Home, Favorites, Settings).filter { it.isBottomNavItem }
        }
    }
}