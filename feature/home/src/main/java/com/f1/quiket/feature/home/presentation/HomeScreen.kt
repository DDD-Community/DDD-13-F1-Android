package com.f1.quiket.feature.home.presentation

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.f1.quiket.core.designsystem.component.HomeActionButton
import com.f1.quiket.core.designsystem.component.HomeProfileCard
import com.f1.quiket.core.designsystem.theme.Black
import com.f1.quiket.core.designsystem.theme.Brown50
import com.f1.quiket.core.designsystem.theme.Gray100
import com.f1.quiket.core.designsystem.theme.Gray600
import com.f1.quiket.core.designsystem.theme.Gray800
import com.f1.quiket.core.designsystem.theme.Gray900
import com.f1.quiket.core.designsystem.theme.Gray950
import com.f1.quiket.core.designsystem.theme.Orange500
import com.f1.quiket.core.designsystem.theme.QuiketTheme
import com.f1.quiket.core.designsystem.theme.White
import com.f1.quiket.core.designsystem.R as DesignSystemR
import com.f1.quiket.feature.home.R
import com.f1.quiket.feature.home.component.ExpandableFab
import com.f1.quiket.feature.home.component.HomeEmptyActivityButton
import com.f1.quiket.feature.home.component.HomeEmptySubjectButton

@Composable
fun HomeScreen() {
    var isExpanded by remember { mutableStateOf(false) }
    // 현재 선택된 탭 상태 (0: 내 과목, 1: 최근 활동)
    var selectedTab by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brown50)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // 상단 흰색 컨테이너 영역
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = White,
                // 하단 라운딩
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 16.dp,
                            start = 24.dp,
                            end = 24.dp,
                            bottom = 32.dp
                        ) // 하단 여유 공간 추가
                ) {
                    // 상단바
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(DesignSystemR.drawable.logo_splash),
                            contentDescription = "Home Quiket Logo",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(width = 90.dp, height = 28.dp)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            painter = painterResource(R.drawable.ic_home_note),
                            contentDescription = "Home Quiket Note",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(24.dp)
                        )
                        Icon(
                            painter = painterResource(R.drawable.ic_home_alert),
                            contentDescription = "Home Quiket Alert",
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .size(24.dp)
                        )
                    }

                    // 텍스트 영역
                    Text(
                        "오늘의 공부, 시작해 볼까요?",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Black,
                        modifier = Modifier.padding(top = 36.dp, bottom = 12.dp)
                    )
                    Text(
                        "내 강의 노트를 업로드 하거나 퀴즈를 만들어 보세요 !",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = Gray600
                    )

                    // 자료업로드
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        HomeActionButton(
                            text = "자료 업로드",
                            iconRes = com.f1.quiket.core.designsystem.R.drawable.ic_home_upload,
                            backgroundColor = Gray100,
                            onClick = {},
                            modifier = Modifier.weight(1f)
                        )
                        HomeActionButton(
                            text = "퀴즈 만들기",
                            iconRes = com.f1.quiket.core.designsystem.R.drawable.ic_home_make,
                            backgroundColor = Orange500,
                            onClick = {},
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            HomeProfileCard(
                "송미짱짱짱",
                1200,
                com.f1.quiket.core.designsystem.R.drawable.ic_profile,
                { },
                modifier = Modifier.padding(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                TabItem(
                    "내 과목",
                    isSelected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    modifier = Modifier.weight(0.7f)
                )
                TabItem(
                    "최근 활동",
                    isSelected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    modifier = Modifier.weight(0.7f)
                )

                Spacer(modifier = Modifier.weight(1.6f))
            }

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = White,
                shape = RoundedCornerShape(topEnd = 24.dp)
            ) {
                Box(modifier = Modifier.padding(5.dp)) {
                    if (selectedTab == 0) {
                        EmptySubjectContent()
                    } else {
                        EmptyActivityContent()
                    }
                }
            }
        }

        if (isExpanded) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Gray900.copy(alpha = 0.6f))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { isExpanded = false }
            )
        }

        ExpandableFab(
            isExpanded = isExpanded,
            onFabClick = { isExpanded = !isExpanded },
            onItemClick = { isExpanded = false },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )
    }
}

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
fun EmptySubjectContent() {
    QuiketTheme {
        Column {
            HomeEmptySubjectButton(
                {},
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 20.dp)
            )
        }
    }
}

@Composable
fun EmptyActivityContent() {
    QuiketTheme {
        Column {
            HomeEmptyActivityButton(
                {},
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 20.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    QuiketTheme {
        HomeScreen()
    }
}
