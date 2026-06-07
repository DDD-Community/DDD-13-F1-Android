package com.f1.quiket.feature.home.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.geometry.Rect
import com.f1.quiket.core.designsystem.component.AddSubjectCard
import com.f1.quiket.core.designsystem.component.SubjectShortCard
import com.f1.quiket.core.designsystem.theme.*
import com.f1.quiket.feature.home.R
import com.f1.quiket.feature.home.component.*
import com.f1.quiket.feature.home.model.*

@Composable
fun TabItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        color = if (isSelected) White else Gray100,
        contentColor = if (isSelected) Gray950 else Gray800
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                )
            )
        }
    }
}

@Composable
fun EmptySubjectContent(
    onAddSubjectClick: () -> Unit = {},
) {
    Column {
        HomeEmptySubjectButton(
            onAddSubjectClick,
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 20.dp)
        )
    }
}

@Composable
fun ActiveSubjectContent(
    subjects: List<Subject>,
    onSubjectsChange: (List<Subject>) -> Unit = {},
    onSubjectAreaPositioned: (Rect) -> Unit = {},
    onSubjectClick: (Subject) -> Unit = {},
    onAddSubjectClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned { coords ->
                val pos = coords.positionInRoot()
                onSubjectAreaPositioned(
                    Rect(
                        left = pos.x,
                        top = pos.y,
                        right = pos.x + coords.size.width,
                        bottom = pos.y + coords.size.height
                    )
                )
            }
    ) {
        Row(
            modifier = Modifier
                .height(24.dp)
                .padding(top = 10.dp, end = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text("전체 보기", style = MaterialTheme.typography.labelSmall, color = Gray600)
            Icon(
                painter = painterResource(R.drawable.ic_home_subject_total),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(16.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // index 0 = AddSubjectCard, index 1..N = subjects
            val totalCount = subjects.size + 1
            (0 until totalCount).chunked(2).forEach { rowIndices ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    rowIndices.forEach { idx ->
                        if (idx == 0) {
                            AddSubjectCard(
                                title = "과목 추가",
                                onClick = onAddSubjectClick,
                                modifier = Modifier.weight(1f),
                            )
                        } else {
                            val subject = subjects[idx - 1]
                            SubjectShortCard(
                                title = subject.title,
                                chapter = subject.chapter,
                                isStarred = subject.isStarred,
                                modifier = Modifier.weight(1f),
                                onStarToggle = {
                                    onSubjectsChange(subjects.mapIndexed { i, item ->
                                        if (i == idx - 1) item.copy(isStarred = !item.isStarred)
                                        else item
                                    })
                                },
                                onClick = { onSubjectClick(subject) }
                            )
                        }
                    }
                    if (rowIndices.size == 1) Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun EmptyActivityContent() {
    Column {
        HomeEmptyActivityButton(
            {},
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 20.dp)
        )
    }
}

@Composable
fun ActiveActivityContent(
    activities: List<Activity>,
    onActivityClick: (Activity) -> Unit = {},
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .height(24.dp)
                .padding(top = 10.dp, end = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text("전체 보기", style = MaterialTheme.typography.labelSmall, color = Gray600)
            Icon(
                painter = painterResource(R.drawable.ic_home_subject_total),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(16.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            activities.forEach { activity ->
                HomeActivityCard(
                    title = activity.title,
                    questionCount = activity.questionCount,
                    activityType = activity.activityType,
                    description = activity.description,
                    progressPercent = activity.progressPercent,
                    isQuizCreated = activity.isQuizCreated,
                    onClick = { onActivityClick(activity) }
                )
            }
        }
    }
}
