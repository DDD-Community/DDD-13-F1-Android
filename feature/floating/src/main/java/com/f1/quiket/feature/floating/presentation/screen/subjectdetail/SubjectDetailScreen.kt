package com.f1.quiket.feature.floating.presentation.screen.subjectdetail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.f1.quiket.core.designsystem.component.AddSubjectCard
import com.f1.quiket.core.designsystem.component.BaseTextField
import com.f1.quiket.core.designsystem.component.HomeActionButton
import com.f1.quiket.core.designsystem.component.HomeEmptyExamCard
import com.f1.quiket.core.designsystem.component.HomeExamCard
import com.f1.quiket.core.designsystem.component.SubjectLongCard
import com.f1.quiket.core.designsystem.theme.Brown50
import com.f1.quiket.core.designsystem.theme.Brown950
import com.f1.quiket.core.designsystem.theme.Gray100
import com.f1.quiket.core.designsystem.theme.Gray300
import com.f1.quiket.core.designsystem.theme.Gray400
import com.f1.quiket.core.designsystem.theme.Gray500
import com.f1.quiket.core.designsystem.theme.Gray700
import com.f1.quiket.core.designsystem.theme.Gray950
import com.f1.quiket.core.designsystem.theme.Green800
import com.f1.quiket.core.designsystem.theme.Negative
import com.f1.quiket.core.designsystem.theme.Orange500
import com.f1.quiket.core.designsystem.theme.QuiketTheme
import com.f1.quiket.core.designsystem.theme.White
import com.f1.quiket.feature.floating.R
import com.f1.quiket.feature.floating.domain.model.Chapter
import com.f1.quiket.feature.floating.presentation.component.SubjectDetailTopBar
import com.f1.quiket.feature.floating.presentation.screen.lectureview.LectureViewScreen
import java.util.Calendar
import java.util.concurrent.TimeUnit

private fun calcDDay(dateStr: String): String {
    return runCatching {
        val parts = dateStr.split(".")
        val exam = Calendar.getInstance().apply {
            set(parts[0].toInt(), parts[1].toInt() - 1, parts[2].toInt(), 0, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
        }
        val diff = TimeUnit.MILLISECONDS.toDays(exam.timeInMillis - today.timeInMillis)
        when {
            diff > 0L -> "D-$diff"
            diff == 0L -> "D-Day"
            else -> "D+${-diff}"
        }
    }.getOrDefault("D-?")
}

private val sampleChapters = listOf(
    Chapter(1, "SQLD 기본", 3),
    Chapter(2, "데이터 모델", 4),
    Chapter(3, "SQL 활용", 2),
)

@Composable
fun SubjectDetailScreen(
    subjectName: String,
    studyPurposeLabel: String,
    examTypeLabel: String,
    onBackClick: () -> Unit = {},
) {
    var selectedChapter by remember { mutableStateOf<Chapter?>(null) }

    // LectureView로 이동
    selectedChapter?.let { chapter ->
        LectureViewScreen(
            chapter = chapter,
            onBackClick = { selectedChapter = null },
        )
        return
    }

    // Persistent state across recompositions
    var isStarred by rememberSaveable { mutableStateOf(false) }
    var showDropdownMenu by remember { mutableStateOf(false) }
    var showScheduleDialog by remember { mutableStateOf(false) }
    var confirmedExamName by rememberSaveable { mutableStateOf("") }
    var confirmedSchedule by rememberSaveable { mutableStateOf("") }

    Scaffold(containerColor = Brown50) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
        ) {

            Column(modifier = Modifier.background(Green800)) {
                SubjectDetailTopBar(
                    title = subjectName,
                    isStarred = isStarred,
                    showMenu = showDropdownMenu,
                    onBackClick = onBackClick,
                    onStarClick = { isStarred = !isStarred },
                    onMenuClick = { showDropdownMenu = true },
                    onMenuDismiss = { showDropdownMenu = false },
                )
                SubjectHeaderSection(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    subjectName = subjectName,
                    studyPurposeLabel = studyPurposeLabel,
                    examTypeLabel = examTypeLabel,
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = White,
                        shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                    )
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HomeActionButton(
                    text = "자료 업로드",
                    iconRes = com.f1.quiket.core.designsystem.R.drawable.ic_home_upload,
                    backgroundColor = Gray100,
                    onClick = {},
                    modifier = Modifier
                        .height(103.dp)
                        .weight(1f)
                )

                HomeActionButton(
                    text = "퀴즈 만들기",
                    iconRes = com.f1.quiket.core.designsystem.R.drawable.ic_home_make,
                    backgroundColor = Orange500,
                    onClick = {},
                    modifier = Modifier
                        .height(103.dp)
                        .weight(1f)
                )
            }
            if (confirmedExamName.isNotBlank() && confirmedSchedule.isNotBlank()) {
                HomeExamCard(
                    examName = confirmedExamName,
                    date = confirmedSchedule,
                    dDay = calcDDay(confirmedSchedule),
                    onClick = { showScheduleDialog = true },
                    modifier = Modifier.padding(16.dp),
                )
            } else {
                HomeEmptyExamCard(
                    { showScheduleDialog = true },
                    modifier = Modifier.padding(16.dp),
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // My Subject Section
            MySubjectSection(
                chapters = sampleChapters,
                onChapterClick = { selectedChapter = it },
            )
        }
    }

    if (showScheduleDialog) {
        AddTestCalendarDialog(
            subjectName = subjectName,
            onDismiss = { showScheduleDialog = false },
            onApply = { name, date ->
                confirmedExamName = name
                confirmedSchedule = date
                showScheduleDialog = false
            },
        )
    }
}

@Composable
private fun SubjectHeaderSection(
    modifier: Modifier,
    subjectName: String,
    studyPurposeLabel: String,
    examTypeLabel: String,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Green800)
            .padding(start = 20.dp, end = 20.dp, top = 4.dp),
    ) {
        Row {
            Column {
                Text(
                    text = studyPurposeLabel,
                    style = MaterialTheme.typography.labelSmall.copy(color = Gray400),
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = examTypeLabel,
                    style = MaterialTheme.typography.labelSmall.copy(color = Gray400),
                )
                Text(
                    text = subjectName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = White,
                    ),
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                painter = painterResource(R.drawable.ic_detail_quiket),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.width(190.dp)
            )
        }
    }
}

@Composable
private fun MySubjectSection(
    chapters: List<Chapter>,
    onChapterClick: (Chapter) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White, RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "내 과목",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Gray950
                ),
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "퀴즈 전체 보기",
                style = MaterialTheme.typography.labelMedium.copy(color = Gray500),
                modifier = Modifier.clickable { },
            )
            Icon(
                painter = painterResource(R.drawable.ic_detail_quiz_all),
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = Color.Unspecified
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
            colors = CardDefaults.cardColors(containerColor = White),
        ) {
            chapters.forEachIndexed { index, chapter ->

                SubjectLongCard(
                    title = chapter.name,
                    chapter = "챕터 ${chapter.number}",
                    part = "파트 ${chapter.partCount}개",
                    onClick = { onChapterClick(chapter) },
                )

                if (index < chapters.lastIndex) {
                    HorizontalDivider(
                        color = White,
                        thickness = 16.dp
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
                .background(White)
                .clickable { }
                .padding(vertical = 14.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AddSubjectCard(
                title = "챕터 추가",
                onClick = {},
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun AddTestCalendarDialog(
    subjectName: String,
    onDismiss: () -> Unit,
    onApply: (examName: String, date: String) -> Unit,
) {
    var examName by remember { mutableStateOf("") }
    var showCalendar by remember { mutableStateOf(false) }

    val now = remember { Calendar.getInstance() }
    var calYear by remember { mutableIntStateOf(now.get(Calendar.YEAR)) }
    var calMonth by remember { mutableIntStateOf(now.get(Calendar.MONTH) + 1) } // 1-indexed
    var selectedDay by remember { mutableStateOf<Int?>(null) }
    var confirmedDateStr by remember { mutableStateOf("") }

    val isApplyEnabled = examName.isNotBlank() && confirmedDateStr.isNotBlank()

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "시험 일정 등록",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Gray950,
                    )
                )
                Spacer(modifier = Modifier.height(20.dp))

                if (!showCalendar) {
                    DialogFieldLabel(text = "과목명 *")
                    Spacer(modifier = Modifier.height(6.dp))
                    BaseTextField(
                        value = subjectName,
                        onValueChange = {},
                        hint = "",
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    DialogFieldLabel(text = "시험명")
                    Spacer(modifier = Modifier.height(6.dp))
                    BaseTextField(
                        value = examName,
                        onValueChange = { examName = it },
                        hint = "시험명을 입력해주세요",
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    DialogFieldLabel(text = "시험 날짜")
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(modifier = Modifier.fillMaxWidth()) {
                        BaseTextField(
                            value = confirmedDateStr,
                            onValueChange = {},
                            hint = "YYYY.MM.DD",
                            readOnly = true,
                            modifier = Modifier.fillMaxWidth(),
                            trailingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.ic_detail_date),
                                    contentDescription = "캘린더",
                                    tint = Color.Unspecified,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        )
                        // BasicTextField가 터치를 소비하므로 투명 오버레이로 클릭 가로챔
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() },
                                    onClick = { showCalendar = true },
                                )
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Gray700),
                            border = BorderStroke(1.dp, Gray300),
                        ) {
                            Text("취소", style = MaterialTheme.typography.bodySmall)
                        }
                        Button(
                            onClick = { onApply(examName, confirmedDateStr) },
                            enabled = isApplyEnabled,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Brown950,
                                contentColor = White,
                                disabledContainerColor = Gray300,
                                disabledContentColor = White
                            )
                        ) {
                            Text("적용", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                } else {
                    // ── Calendar mode ──
                    CalendarView(
                        year = calYear,
                        month = calMonth,
                        selectedDay = selectedDay,
                        onPrevMonth = {
                            if (calMonth == 1) {
                                calYear--; calMonth = 12
                            } else {
                                calMonth--
                            }
                            selectedDay = null
                        },
                        onNextMonth = {
                            if (calMonth == 12) {
                                calYear++; calMonth = 1
                            } else {
                                calMonth++
                            }
                            selectedDay = null
                        },
                        onDaySelect = { selectedDay = it },
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        TextButton(
                            onClick = { selectedDay = null },
                        ) {
                            Text(
                                "초기화",
                                style = MaterialTheme.typography.bodySmall.copy(color = Gray500),
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        TextButton(onClick = { showCalendar = false }) {
                            Text(
                                "취소",
                                style = MaterialTheme.typography.bodySmall.copy(color = Gray500),
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Button(
                            onClick = {
                                selectedDay?.let { day ->
                                    confirmedDateStr = "%d.%02d.%02d".format(calYear, calMonth, day)
                                }
                                showCalendar = false
                            },
                            enabled = selectedDay != null,
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Brown950,
                                contentColor = White,
                                disabledContainerColor = Gray300,
                                disabledContentColor = White,
                            ),
                        ) {
                            Text("확인", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DialogFieldLabel(text: String) {

    val annotatedText = buildAnnotatedString {
        append(text.replace("*", ""))

        if (text.contains("*")) {
            withStyle(
                style = SpanStyle(
                    color = Negative
                )
            ) {
                append("*")
            }
        }
    }

    Text(
        text = annotatedText,
        style = MaterialTheme.typography.labelMedium.copy(
            fontWeight = FontWeight.SemiBold,
            color = Gray700,
        )
    )
}

@Composable
private fun CalendarView(
    year: Int,
    month: Int,
    selectedDay: Int?,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onDaySelect: (Int) -> Unit,
) {
    val cal = remember(year, month) {
        Calendar.getInstance().apply { set(year, month - 1, 1) }
    }
    val daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
    // DAY_OF_WEEK: 1=Sun … 7=Sat → convert to 0-indexed col
    val firstDayCol = cal.get(Calendar.DAY_OF_WEEK) - 1

    Column {
        // Month navigation header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            IconButton(onClick = onPrevMonth) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "이전 달",
                    tint = Gray700,
                )
            }
            Text(
                text = "${year}년 ${month}월",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Gray950,
                ),
            )
            IconButton(onClick = onNextMonth) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "다음 달",
                    tint = Gray700,
                )
            }
        }

        // Day-of-week labels
        val dayLabels = listOf("일", "월", "화", "수", "목", "금", "토")
        Row(modifier = Modifier.fillMaxWidth()) {
            dayLabels.forEach { label ->
                Text(
                    text = label,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall.copy(color = Gray500),
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Day grid
        val totalCells = firstDayCol + daysInMonth
        val rows = (totalCells + 6) / 7

        repeat(rows) { row ->
            Row(modifier = Modifier.fillMaxWidth()) {
                repeat(7) { col ->
                    val dayNumber = row * 7 + col - firstDayCol + 1
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (dayNumber in 1..daysInMonth) {
                            val isSelected = dayNumber == selectedDay
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) Brown950 else Color.Transparent)
                                    .clickable { onDaySelect(dayNumber) },
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = "$dayNumber",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = if (isSelected) White else Gray950,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    ),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ────────────────────────────────────────────────────────────────────────────
// Previews
// ────────────────────────────────────────────────────────────────────────────

@Preview(showBackground = true)
@Composable
private fun SubjectDetailPreview() {
    QuiketTheme {
        SubjectDetailScreen(
            subjectName = "SQLD",
            studyPurposeLabel = "시험·자격증 대비",
            examTypeLabel = "자격증",
            onBackClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AddTestCalendarDialogPreview() {
    QuiketTheme {
        AddTestCalendarDialog(
            subjectName = "SQLD",
            onDismiss = {},
            onApply = { _, _ -> },
        )
    }
}