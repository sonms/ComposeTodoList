package com.example.composemytodolist.presentation.calendar

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.composemytodolist.presentation.home.TodoItem
import com.example.composemytodolist.presentation.home.todoList
import com.example.composemytodolist.roomDB.TodoEntity
import com.example.composemytodolist.ui.theme.Purple80
import com.example.composemytodolist.ui.theme.blue0
import com.example.composemytodolist.ui.theme.blue2
import com.example.composemytodolist.ui.theme.blue3
import com.example.composemytodolist.viewmodel.MainTodoViewModel
import kotlinx.coroutines.delay
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth


@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
    currentDate: LocalDate = LocalDate.now(),
    onSelectedDate: (LocalDate) -> Unit,//LocalDate를 Unit형태로 반환한다
    todoViewModel: MainTodoViewModel = hiltViewModel()
) {
    val calendarEvents by todoViewModel.calendarTodoList.observeAsState(emptyList())
    val calendarMonthEvents by todoViewModel.selectedMonthEvents.observeAsState(emptyList())


    val yearRange: IntRange = IntRange(1970, 2100)
    // 현재 연도와 월에 해당하는 페이지를 나타냅니다.
    val initialPage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        (currentDate.year - yearRange.first) * 12 + currentDate.monthValue - 1
    } else {
        TODO("VERSION.SDK_INT < O")
    }
    var currentPage by remember { mutableStateOf(initialPage) } //현재 페이지
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var currentSelectedDate by remember { mutableStateOf(currentDate) }

    val localDate = LocalDate.now()
    val yearMonth = YearMonth.from(localDate)
    val lastDayOfMonth = yearMonth.lengthOfMonth() //월의 마지막 날짜


    //페이지의 초기 위치를 설정합니다. 위에서 계산한 initialPage 값을 사용하여 현재 연도와 월에 해당하는 페이지로 설정합니다.
    val pagerState = rememberPagerState(
        initialPage = initialPage,
        //,-0l.5~0.5
        //이 값은 초기 페이지의 시작 위치를 설정하는 데 사용됩니다
        //한 마디로 달력(HorizontalPager)의 위치
        initialPageOffsetFraction = 0.0f
    ) {
        // 월 단위로 페이징되는 경우 각 월의 일 수에 따라 페이지 수가 다를 수 있습니다. 그걸 동적으로 세팅하는곳
        //lastDayOfMonth //(config.yearRange.last - config.yearRange.first) * 12
        (yearRange.last - yearRange.first) * 12
    }

    //첫화면 구성 시 사용
    LaunchedEffect(Unit) {
        todoViewModel.fetchEventsByDate(LocalDate.now().toString())
        todoViewModel.fetchEventsByMonth(YearMonth.now())
        Log.d("calendarMonthEvents", "$calendarMonthEvents 1")
    }

    /*LaunchedEffect(currentMonth) {

        Log.e("calendarMonthEvents", "$calendarMonthEvents 2")
    }*/

    LaunchedEffect(pagerState.currentPage) {
        val addMonth = (pagerState.currentPage - currentPage).toLong()
        currentMonth = currentMonth.plusMonths(addMonth)
        Log.d("launched", currentMonth.toString())

        todoViewModel.fetchEventsByMonth(currentMonth)

        // Delay to wait for LiveData update, or alternatively you could just rely on `observeAsState`
        Log.d("calendarMonthEvents", "$calendarMonthEvents")
        currentPage = pagerState.currentPage
    }

    LaunchedEffect(currentSelectedDate) {
        onSelectedDate(currentSelectedDate)
        todoViewModel.fetchEventsByDate(currentSelectedDate.toString())
    }

    //페이지 유지 변수의 현재 페이지 값이 변경될때 실행됨
    /*LaunchedEffect(pagerState.currentPage) {
        //추가될 월
        val addMonth = (pagerState.currentPage - currentPage).toLong()
        //현재 월 체인지\
        currentMonth = currentMonth.plus(addMonth).toInt()
        currentPage = pagerState.currentPage
    }*/

    //val pageCount = (yearRange.last - yearRange.first) * 12

    Column(modifier = modifier.fillMaxSize()) {
        val headerText = "${currentMonth.year}년 ${currentMonth.monthValue}월"
        CalendarHeader(
            text = headerText
        )
        HorizontalPager(
            state = pagerState
        ) { page ->
            val displayedDate = YearMonth.of(
                yearRange.first + page / 12,
                (page % 12) + 1
            ).atDay(1)
            if (page in pagerState.currentPage - 1..pagerState.currentPage + 1) { // 페이징 성능 개선을 위한 조건문
                CalendarBody(
                    currentDate = displayedDate,
                    selectedDate = currentSelectedDate,
                    onSelectedDate = { date ->
                        currentSelectedDate = date
                    },
                    calendarEvents = calendarMonthEvents // 이벤트 데이터를 넘김
                )
            }
        }
        LazyColumn {
            itemsIndexed(
                items = calendarEvents,
                key = { _ ,item ->
                    item.id

                }
            ) { index, item ->
                TodoItem(todo = item,
                    onDelete = { todoViewModel.deleteTodoById(it.id) },
                    onItemClick = { clickedTodo ->
                        // Handle the click event here
                        Log.d("CalendarScreen", "Clicked item: ${clickedTodo.title}")

                    }
                )
            }
        }
    }
}

@Composable
fun CalendarHeader(text: String) {
    val textColor = MaterialTheme.colorScheme.onPrimary
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)) {
        Text(
            text = text,
            color = textColor,
        )
    }
}

@Composable
fun CalendarDayOfWeek( //요일 표시
    modifier: Modifier = Modifier
) {
    Row(modifier) {
        DayOfWeek.entries.forEach { dayOfWeek ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.7f),
                    text = dayOfWeek.getDisplayName(java.time.format.TextStyle.NARROW, java.util.Locale.KOREAN),
                    style = TextStyle.Default,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarBody(
    currentDate: LocalDate,
    selectedDate: LocalDate,
    onSelectedDate: (LocalDate) -> Unit,
    calendarEvents: List<TodoEntity> // 추가: 이벤트 리스트를 파라미터로 받음
) {
    val lastDay = currentDate.lengthOfMonth()
    val firstDayOfWeek = currentDate.withDayOfMonth(1).dayOfWeek.value
    val days = (1..lastDay).toList()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        CalendarDayOfWeek()
        LazyVerticalGrid(
            modifier = Modifier.wrapContentSize(),
            columns = GridCells.Fixed(7)
        ) {
            for (i in 1 until firstDayOfWeek) {
                item {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .padding(top = 10.dp)
                    )
                }
            }
            items(days) { day ->
                val date = currentDate.withDayOfMonth(day)
                //Log.e("date", "$date")
                val isSelected = selectedDate == date
                val hasEvent = hasEventForDate(calendarEvents, date) // 날짜에 이벤트가 있는지 확인
                CalendarDayItem(
                    date = date,
                    isToday = date == LocalDate.now(),
                    isSelected = isSelected,
                    currentDate = currentDate,
                    selectedDate = selectedDate,
                    onSelectedDate = onSelectedDate,
                    hasEvent = hasEvent
                )
            }
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
fun hasEventForDate(events: List<TodoEntity>, date: LocalDate): Boolean {
    Log.e("events", "$events")
    Log.e("events", "$date")
    Log.e("evnets", "${events.any { it.eventDate == date.toString() }}")
    return events.any { it.eventDate == date.toString() }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarDayItem(
    date: LocalDate,
    isToday : Boolean,
    currentDate: LocalDate,
    selectedDate: LocalDate,
    isSelected: Boolean,
    onSelectedDate: (LocalDate) -> Unit,
    hasEvent: Boolean // 추가: 이벤트가 있는지 여부를 파라미터로 전달
) {
    val backgroundColor = when {
        isToday -> blue3
        isSelected -> blue2
        hasEvent -> Color.Yellow
        else -> Color.White
    }

    Column(modifier = Modifier
        .wrapContentSize()
        .padding(top = 5.dp)
        .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
        .size(48.dp)
        .clip(shape = RoundedCornerShape(10.dp))
        .background(backgroundColor)
        //.background(backgroundColor)
        .noRippleClickable { onSelectedDate(date) },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            modifier = Modifier,
            text = date.dayOfMonth.toString(),
            color = if (isToday) {
                Color.White
            } else {
                Color.Black
            },
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}


/*@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarDayItem(
    modifier: Modifier = Modifier,
    date: LocalDate,
    isToday: Boolean,
    isSelected: Boolean,
    onSelectedDate: (LocalDate) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(10.dp))
            .conditional(isToday) {
                background(blue3)
            }
            .conditional(isSelected) {
                background(blue2)
            }
            .conditional(!isToday && !isSelected) {
                background(Color.White)
            }
            .noRippleClickable { onSelectedDate(date) },
    ) {
        Text(
            modifier = Modifier,
            textAlign = TextAlign.Center,
            text = date.dayOfMonth.toString(),
            color = Color.Black
        )
    }
}*/

inline fun Modifier.noRippleClickable(crossinline onClick: () -> Unit): Modifier = composed {
    clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}

fun Modifier.conditional(condition: Boolean, modifier: Modifier.() -> Modifier): Modifier {
    return if (condition) {
        then(modifier(Modifier))
    } else {
        this
    }
}
/*
@Composable
fun HorizontalCalendar() {
    LaunchedEffect(pagerState.currentPage) {
        val addMonth = (pagerState.currentPage - currentPage).toLong()
        currentMonth = currentMonth.plusMonths(addMonth)
        currentPage = pagerState.currentPage
    }

    LaunchedEffect(currentSelectedDate) {
        onSelectedDate(currentSelectedDate)
    }

    Column(modifier = modifier) {
        val headerText = currentMonth.dateFormat("yyyy년 M월")
        val pageCount = (config.yearRange.last - config.yearRange.first) * 12
        CalendarHeader(
            modifier = Modifier.padding(20.dp),
            text = headerText
        )
        HorizontalPager(
            pageCount = pageCount,
            state = pagerState
        ) { page ->
            val date = LocalDate.of(
                config.yearRange.first + page / 12,
                page % 12 + 1,
                1
            )
            if (page in pagerState.currentPage - 1..pagerState.currentPage + 1) { // 페이징 성능 개선을 위한 조건문
                CalendarMonthItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    currentDate = date,
                    selectedDate = currentSelectedDate,
                    onSelectedDate = { date ->
                        currentSelectedDate = date
                    }
                )
            }
        }
    }
}

@Composable
fun CalendarHeader(
    modifier: Modifier = Modifier,
    text: String,
) {
    Box(modifier = modifier) {
        Text(
            text = text,
            style = BlackN12
        )
    }
}

@Composable
fun CalendarMonthItem(
    modifier: Modifier = Modifier,
    currentDate: LocalDate,
    selectedDate: LocalDate,
    onSelectedDate: (LocalDate) -> Unit
) {
    val lastDay by remember { mutableStateOf(currentDate.lengthOfMonth()) }
    val firstDayOfWeek by remember { mutableStateOf(currentDate.dayOfWeek.value) }
    val days by remember { mutableStateOf(IntRange(1, lastDay).toList()) }

    Column(modifier = modifier) {
        DayOfWeek()
        LazyVerticalGrid(
            modifier = Modifier.height(260.dp),
            columns = GridCells.Fixed(7)
        ) {
            for (i in 1 until firstDayOfWeek) { // 처음 날짜가 시작하는 요일 전까지 빈 박스 생성
                item {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .padding(top = 10.dp)
                    )
                }
            }
            items(days) { day ->
                val date = currentDate.withDayOfMonth(day)
                val isSelected = remember(selectedDate) {
                    selectedDate.compareTo(date) == 0
                }
                CalendarDay(
                    modifier = Modifier.padding(top = 10.dp),
                    date = date,
                    isToday = date == LocalDate.now(),
                    isSelected = isSelected,
                    onSelectedDate = onSelectedDate
                )
            }
        }
    }
}


@Composable
fun CalendarDay(
    modifier: Modifier = Modifier,
    date: LocalDate,
    isToday: Boolean,
    isSelected: Boolean,
    onSelectedDate: (LocalDate) -> Unit
) {
    val hasEvent = false // TODO
    Column(
        modifier = modifier
            .wrapContentSize()
            .size(30.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .conditional(isToday) {
                background(gray07)
            }
            .conditional(isSelected) {
                background(gray0)
            }
            .conditional(!isToday && !isSelected) {
                background(gray08)
            }
            .noRippleClickable { onSelectedDate(date) },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        val textColor = if (isSelected) gray09 else gray0
        Text(
            modifier = Modifier,
            textAlign = TextAlign.Center,
            text = date.dayOfMonth.toString(),
            style = BoldN12,
            color = textColor
        )
        if (hasEvent) {
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .clip(shape = RoundedCornerShape(4.dp))
                    .conditional(isSelected) {
                        background(gray09)
                    }
                    .conditional(!isSelected) {
                        background(gray0)
                    }
            )
        }
    }
}

@Composable
fun DayOfWeek(
    modifier: Modifier = Modifier
) {
    Row(modifier) {
        DayOfWeek.values().forEach { dayOfWeek ->
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                text = dayOfWeek.getDisplayName(TextStyle.NARROW, Locale.KOREAN),
                style = BlackN12,
                textAlign = TextAlign.Center
            )
        }
    }
}*/
