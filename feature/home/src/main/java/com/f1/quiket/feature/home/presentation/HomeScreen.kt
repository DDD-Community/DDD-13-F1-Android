package com.f1.quiket.feature.home.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
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
import com.f1.quiket.core.designsystem.theme.Brown50
import com.f1.quiket.core.designsystem.theme.Brown950
import com.f1.quiket.core.designsystem.theme.Gray900
import com.f1.quiket.core.designsystem.theme.QuiketTheme
import com.f1.quiket.core.designsystem.theme.White
import com.f1.quiket.feature.home.R

@Composable
fun HomeScreen() {
    var isExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brown50)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text("홈", style = MaterialTheme.typography.headlineMedium)
                Text(
                    "오늘 학습 상태와 추천 문제를 보여줄 홈 화면 뼈대입니다.",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }

        if (isExpanded) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    // background Color 다시 설정 필요
                    .background(Gray900.copy(alpha = 0.6f))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        isExpanded = false
                    }
            )
        }

        ExpandableFab(
            isExpanded = isExpanded,
            onFabClick = { isExpanded = !isExpanded },
            onItemClick = { route ->
                isExpanded = false
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )
    }
}
@Composable
fun ExpandableFab(
    isExpanded: Boolean,
    onFabClick: () -> Unit,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End
    ) {
        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn() + slideInVertically { it / 2 },
            exit = fadeOut() + slideOutVertically { it / 2 }
        ) {
            Surface(
                color = Color.Transparent,
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {

                    SmallFab(
                        R.drawable.ic_small_floting_test, "시험 등록하기"
                    ) {
                        onItemClick("시험 등록하기")
                    }

                    SmallFab(
                        R.drawable.ic_small_floting_quiz, "퀴즈 만들기"
                    ) {
                        onItemClick("퀴즈 만들기")
                    }

                    SmallFab(
                        R.drawable.ic_small_floting_upload, "자료 업로드"
                    ) {
                        onItemClick("자료 업로드")
                    }

                    SmallFab(
                        R.drawable.ic_small_floting_add, "과목 추가"
                    ) {
                        onItemClick("과목 추가")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        FloatingActionButton(
            onClick = onFabClick,
            containerColor = Brown950,
            shape = RoundedCornerShape(1000.dp)
        ) {
            Icon(
                painter = painterResource(
                    id = if (isExpanded)R.drawable.ic_floating_close else R.drawable.ic_floating_plus),
                contentDescription = null
            )
        }
    }
}

@Composable
fun SmallFab(
    icon: Int,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Surface(
            color = Color.Transparent,
            contentColor = Color.Transparent
        ){
            Text(
                text = label,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = White
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Surface(
            shape = RoundedCornerShape(1000.dp),
            color = Color.Transparent,
            modifier = Modifier.border(
                width = 2.dp,
                color = Brown950,
                shape = RoundedCornerShape(1000.dp)
            )
        ) {
            SmallFloatingActionButton(
                onClick = onClick,
                shape = RoundedCornerShape(1000.dp),
                containerColor = White
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = Brown950
                )
            }
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
