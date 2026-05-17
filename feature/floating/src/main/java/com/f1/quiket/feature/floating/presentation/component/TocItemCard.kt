package com.f1.quiket.feature.floating.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.f1.quiket.core.designsystem.R
import com.f1.quiket.core.designsystem.theme.Brown950
import com.f1.quiket.core.designsystem.theme.Gray100
import com.f1.quiket.core.designsystem.theme.Gray700
import com.f1.quiket.core.designsystem.theme.Gray950
import com.f1.quiket.core.designsystem.theme.QuiketTheme
import com.f1.quiket.core.designsystem.theme.White
import com.f1.quiket.feature.floating.domain.model.TocChapter
import com.f1.quiket.feature.floating.domain.model.TocPart

@Composable
fun TocChapterHeader(
    chapter: TocChapter,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Gray100)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "챕터 ${chapter.id}",
            style = MaterialTheme.typography.labelSmall.copy(color = Brown950),
            modifier = Modifier.width(52.dp),
        )
        Text(
            text = chapter.title,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Bold,
                color = Gray950,
            ),
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
fun TocPartItem(
    part: TocPart,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(White)
            .clickable(onClick = onClick)
            .padding(start = 52.dp, end = 16.dp, top = 12.dp, bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = part.title,
            style = MaterialTheme.typography.bodySmall.copy(
                color = if (part.isSelected) Brown950 else Gray700,
                fontWeight = if (part.isSelected) FontWeight.SemiBold else FontWeight.Normal,
            ),
            modifier = Modifier.weight(1f),
        )
        if (part.isSelected) {
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                painter = painterResource(R.drawable.ic_star_on),
                contentDescription = "선택됨",
                tint = Color.Unspecified,
                modifier = Modifier.size(16.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TocItemCardPreview() {
    val sampleChapter = TocChapter(
        id = 1,
        title = "SQLD 기본",
        parts = listOf(
            TocPart(1, "파트 1 — 데이터베이스 개념", isSelected = true),
            TocPart(2, "파트 2 — 데이터 모델링"),
            TocPart(3, "파트 3 — 정규화"),
        ),
    )
    QuiketTheme {
        Column {
            TocChapterHeader(chapter = sampleChapter)
            sampleChapter.parts.forEach { part ->
                TocPartItem(part = part, onClick = {})
            }
        }
    }
}