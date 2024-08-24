package com.example.composemytodolist

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.composemytodolist.presentation.EditTodoScreen
import com.example.composemytodolist.presentation.calendar.CalendarScreen
import com.example.composemytodolist.presentation.home.FakeHomeScreen
import com.example.composemytodolist.presentation.home.HomeScreen
import com.example.composemytodolist.presentation.settings.SettingsScreen
import com.example.composemytodolist.ui.theme.ComposeMyTodoListTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    sealed class BottomNavItem(
        val title: Int, val icon: Int, val screenRoute: String
    ) {
        object Calendar : BottomNavItem(R.string.text_calendar, R.drawable.baseline_calendar_month_24, "calendar")
        object Home : BottomNavItem(R.string.text_home, R.drawable.baseline_home_24, "home")
        object Settings : BottomNavItem(R.string.text_settings, R.drawable.baseline_settings_24, "settings")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeMyTodoListTheme {
                MainContent() // Include the MainContent directly
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainContent() {
    val navController = rememberNavController()

    // 현재 라우트를 가져옵니다.
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Scaffold(
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            // 특정 라우트에서는 BottomNavigation을 숨깁니다.
            if (currentRoute != "edit_todo") {
                FloatingActionButton(
                    onClick = {
                        navController.navigate("edit_todo")
                    },
                    modifier = Modifier.padding(end = 10.dp),
                ) {
                    Icon(Icons.Default.Create, contentDescription = "Delete")
                }
            }
        },
        bottomBar = {
            // 특정 라우트에서는 BottomNavigation을 숨깁니다.
            if (currentRoute != "edit_todo") {
                BottomNavigation(navController = navController)
            }
        },
    ) {
        Box(Modifier.padding(it)) {
            NavigationGraph(navController = navController)
        }
    }
}

@Composable
fun BottomNavigation(navController: NavController) {
    val items = listOf(
        MainActivity.BottomNavItem.Home,
        MainActivity.BottomNavItem.Calendar,
        MainActivity.BottomNavItem.Settings,
    )

    androidx.compose.material3.NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        containerColor = Color.White,
        contentColor = Color.Black
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val context = LocalContext.current
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.screenRoute,
                onClick = {
                    navController.navigate(item.screenRoute) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(
                        painterResource(id = item.icon),
                        contentDescription = item.title.toString(),
                        modifier = Modifier.wrapContentSize(),
                    )
                },
                label = {
                    Text(
                        modifier = Modifier.wrapContentSize(),
                        text = context.getString(item.title),
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Blue,
                    indicatorColor = MaterialTheme.colorScheme.background,
                    selectedTextColor = Color.Blue
                )
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = MainActivity.BottomNavItem.Home.screenRoute) {
        composable(MainActivity.BottomNavItem.Calendar.screenRoute) {
            CalendarScreen()
        }
        composable(MainActivity.BottomNavItem.Home.screenRoute) {
            HomeScreen()
        }
        composable(MainActivity.BottomNavItem.Settings.screenRoute) {
            SettingsScreen()
        }
        composable("edit_todo") {
            EditTodoScreen(navController)
        }
    }
}

/*
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeMyTodoListTheme(darkTheme = false) {
        // Use a simpler Composable function that doesn't require ViewModels or context-specific operations
        MainContent()
    }
}
*/
