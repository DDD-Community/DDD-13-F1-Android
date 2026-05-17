package com.f1.quiket.feature.floating.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.f1.quiket.core.designsystem.theme.Brown50
import com.f1.quiket.core.designsystem.theme.Brown950
import com.f1.quiket.core.designsystem.theme.Gray100
import com.f1.quiket.core.designsystem.theme.Gray500
import com.f1.quiket.core.designsystem.theme.Gray700
import com.f1.quiket.core.designsystem.theme.Gray950
import com.f1.quiket.core.designsystem.theme.QuiketTheme
import com.f1.quiket.core.designsystem.theme.White
import com.f1.quiket.feature.floating.domain.model.LectureItem

@Composable
fun LectureItemCard(
    item: LectureItem,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Brown50)
            .padding(16.dp),
    ) {
        // 번호 + 제목
        Row(verticalAlignment = Alignment.Top) {
            Text(
                text = item.number,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Brown950,
                ),
                modifier = Modifier.width(36.dp),
            )
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Gray950,
                ),
                modifier = Modifier.weight(1f),
            )
        }

        if (item.content.isNotBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = item.content,
                style = MaterialTheme.typography.labelSmall.copy(color = Gray700),
                modifier = Modifier.padding(start = 36.dp),
            )
        }
    }
}

@Composable
fun ClippedLectureItemCard(
    item: LectureItem,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Gray100)
            .padding(16.dp),
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Text(
                text = item.number,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Brown950,
                ),
                modifier = Modifier.width(36.dp),
            )
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Gray950,
                ),
                modifier = Modifier.weight(1f),
            )
        }

        if (item.content.isNotBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = item.content,
                style = MaterialTheme.typography.labelSmall.copy(color = Gray500),
                modifier = Modifier.padding(start = 36.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LectureItemCardPreview() {
    QuiketTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            LectureItemCard(
                item = LectureItem(
                    id = 1,
                    number = "1.",
                    title = "데이터베이스의 개념",
                    content = "데이터베이스란 여러 사람이 공유하여 사용할 목적으로 체계화해 통합·관리하는 데이터의 집합이다.",
                ),
            )
            Spacer(modifier = Modifier.height(8.dp))
            ClippedLectureItemCard(
                item = LectureItem(
                    id = 2,
                    number = "1-1.",
                    title = "DBMS의 특징",
                    content = "독립성, 무결성, 보안성, 일관성, 중복 최소화",
                    isClipped = true,
                ),
            )
        }
    }
}