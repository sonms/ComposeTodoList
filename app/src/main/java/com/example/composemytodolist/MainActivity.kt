package com.example.composemytodolist

import android.Manifest.permission_group.CALENDAR
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.composemytodolist.calendar.CalendarScreen
import com.example.composemytodolist.home.HomeScreen
import com.example.composemytodolist.settings.SettingsScreen
import com.example.composemytodolist.ui.theme.ComposeMyTodoListTheme


class MainActivity : ComponentActivity() {
    sealed class BottomNavItem(
        val title: Int, val icon: Int, val screenRoute: String
    ) {
        object Calendar : BottomNavItem(R.string.text_calendar, R.drawable.baseline_calendar_month_24, com.example.composemytodolist.Calendar)
        object Home : BottomNavItem(R.string.text_home, R.drawable.baseline_home_24, com.example.composemytodolist.Home)
        object Settings : BottomNavItem(R.string.text_settings, R.drawable.baseline_settings_24, com.example.composemytodolist.Settings)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeMyTodoListTheme {

            }
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainContent(
    modifier: Modifier
) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigation(navController = navController)
        }
    ) {
        Box(Modifier.padding(it)){
            NavigationGraph(navController = navController)
        }
    }
}

@Composable
fun BottomNavigation(navController: NavController) {
    val items = listOf<MainActivity.BottomNavItem>(
        MainActivity.BottomNavItem.Home,
        MainActivity.BottomNavItem.Calendar,
        MainActivity.BottomNavItem.Settings,
    )

    androidx.compose.material3.NavigationBar (
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        containerColor = Color.White,
        contentColor = Color.Black
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route


        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.screenRoute,
                onClick = {
                    navController.navigate(item.screenRoute) {
                        //같은 탭을 한 번 더 탭하면 또 열리는 문제(백스택에 계속쌓이는)를 해결하기 위함
                        //탭을 선택시 백스택에 대상이 쌓이지않도록 그래프 시작대상으로 팝업
                        //아래 코드는 닫힐 때 어디로 이동될지 정해줌 여기서는 그래프의 가장시작점
                        //그래서 시작점 backstack은 없으므로 두 번 뒤로가기 시 앱종료
                        popUpTo(navController.graph.startDestinationId) {saveState = true}
                        //savestate특성에 의해 저장된 상태 복원여부 결정
                        //상태가 저장되지 않은경우 아무의미없음
                        //동일탭 하면 다시 로드x하고 이전 데이터와 사용자 상태 화면유지
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                //enabled = ,
                icon = {
                    Icon(
                        painterResource(id = item.icon),
                        contentDescription = item.title.toString(),
                        modifier = Modifier.fillMaxWidth(),
                        //tint = if (onClickedNavigationItem) Color.Blue else Color.Black
                    )
                },
                label = {
                    Text(
                        modifier = Modifier.wrapContentSize(),
                        text = item.title.toString(),
                        //color = if (onClickedNavigationItem) Color.Blue else Color.Black
                    )
                },
                colors = NavigationBarItemDefaults
                    .colors(
                        //여기가 이제 selected에 따른 icon색변경
                        selectedIconColor = Color.Blue,
                        //누를 때 나오던 둥근 그림자 효과
                        //현재는 표면과 색을 동일시하여 이펙트 색없앰
                        indicatorColor = MaterialTheme.colorScheme.surface //MaterialTheme.colorScheme.surfaceColorAtElevation(LocalAbsoluteTonalElevation.current)
                    )
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = MainActivity.BottomNavItem.Calendar.screenRoute) {
        composable(MainActivity.BottomNavItem.Calendar.screenRoute) {
            CalendarScreen()
        }
        composable(MainActivity.BottomNavItem.Home.screenRoute) {
            HomeScreen()
        }
        composable(MainActivity.BottomNavItem.Settings.screenRoute) {
            SettingsScreen()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeMyTodoListTheme {
        MainContent(modifier = Modifier)
    }
}
