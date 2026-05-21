package com.f1.quiket.feature.mypage.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.f1.quiket.core.designsystem.theme.Brown950
import com.f1.quiket.core.designsystem.theme.Orange100
import com.f1.quiket.core.designsystem.theme.Orange300
import com.f1.quiket.core.designsystem.theme.QuiketTheme
import com.f1.quiket.feature.mypage.R
import com.f1.quiket.feature.mypage.presentation.RoomItem

@Composable
fun CharacterRoomSection(
    level: Int,
    unlockedItems: Set<RoomItem>,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .background(Orange100)
            .height(220.dp),
    ) {
        RoomBackground(unlockedItems = unlockedItems)

        // 캐릭터 + 말풍선을 하나의 Box로 묶어서 BottomCenter 정렬
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp)
                .wrapContentSize(),
            contentAlignment = Alignment.BottomCenter,
        ) {
            // 캐릭터
            Icon(
                painter = painterResource(R.drawable.ic_my_qring),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(160.dp)
            )

            // 말풍선: 캐릭터 상단 기준으로 위로 올림
//            RoomSpeechBubble(
//                level = level,
//                unlockedItems = unlockedItems,
//                modifier = Modifier
//                    .align(Alignment.TopCenter)
//                    .offset(x = 50.dp, y = (-8).dp), // 캐릭터 상단보다 살짝 위
//            )

            Icon(painter = painterResource(R.drawable.ic_speech_balloon),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(x = 50.dp, y = (-30).dp),
            )
        }
    }
}

@Composable
private fun RoomBackground(unlockedItems: Set<RoomItem>) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (RoomItem.RUG in unlockedItems) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp)
                    .size(width = 160.dp, height = 28.dp)
                    .background(Color(0xFFD4956A).copy(alpha = 0.4f)),
            )
        }
        if (RoomItem.SOFA in unlockedItems) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 12.dp, bottom = 30.dp),
            ) {
                Text(text = "🛋", fontSize = 36.sp)
            }
        }
        if (RoomItem.BOOKSHELF in unlockedItems) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 12.dp, end = 12.dp),
            ) {
                Text(text = "📚", fontSize = 32.sp)
            }
        }
        if (RoomItem.FULL_ROOM in unlockedItems) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 10.dp, start = 12.dp),
            ) {
                Text(text = "🪴", fontSize = 28.sp)
            }
        }
    }
}

@Composable
private fun RoomSpeechBubble(
    level: Int,
    unlockedItems: Set<RoomItem>,
    modifier: Modifier = Modifier,
) {
    val message = when {
        RoomItem.RUG !in unlockedItems -> "러그를 모아서\n방꾸미기를 해야지!"
        RoomItem.SOFA !in unlockedItems -> "소파를 모아서\n방꾸미기를 해야지!"
        RoomItem.BOOKSHELF !in unlockedItems -> "책장을 모아서\n방꾸미기를 해야지!"
        RoomItem.FULL_ROOM !in unlockedItems -> "도토리를 모아서\n방꾸미기를 해야지!"
        else -> "방 꾸미기 완성!\n최고의 큐링이야!"
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Orange300)
            .border(width = 1.dp, color = Orange300, shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
    ) {
        Text(
            text = message,
            fontSize = 11.sp,
            color = Brown950,
            lineHeight = 16.sp,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CharacterRoomSectionPreview() {
    QuiketTheme {
        CharacterRoomSection(
            level = 2,
            unlockedItems = setOf(RoomItem.BASIC_ROOM, RoomItem.RUG),
        )
    }
}