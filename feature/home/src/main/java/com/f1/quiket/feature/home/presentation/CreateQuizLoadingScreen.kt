package com.f1.quiket.feature.home.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.f1.quiket.core.designsystem.theme.Blue100
import com.f1.quiket.core.designsystem.theme.Blue200
import com.f1.quiket.core.designsystem.theme.Brown500
import com.f1.quiket.core.designsystem.theme.Brown800
import com.f1.quiket.core.designsystem.theme.Brown950
import com.f1.quiket.core.designsystem.theme.Gray100
import com.f1.quiket.core.designsystem.theme.Gray700
import com.f1.quiket.core.designsystem.theme.Gray900
import com.f1.quiket.core.designsystem.theme.Gray950
import com.f1.quiket.core.designsystem.theme.QuiketTheme
import com.f1.quiket.core.designsystem.theme.White

@Composable
fun CreateQuizLoadingScreen(
    progress: Float,
    rewardCount: Int,
    onBrowseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val progressPercent = (progress.coerceIn(0f, 1f) * 100).toInt()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(White),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 80.dp, end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            LoadingCharacterIllustration(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp),
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = "큐링이가 열심히\n퀴즈랑 도토리 배달을 준비하고 있어요!",
                        color = Gray950,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontSize = 20.sp,
                            lineHeight = 28.sp,
                            fontWeight = FontWeight.Bold,
                        ),
                    )
                    Text(
                        text = "잠시 쉬다 오셔도 돼요!\n배달이 완료되면 불러드릴게요",
                        color = Gray700,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            fontWeight = FontWeight.Medium,
                        ),
                    )
                }
                LoadingProgressRow(
                    progress = progress,
                    progressPercent = progressPercent,
                )
                RewardComment(rewardCount = rewardCount)
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 36.dp)
                .clip(RoundedCornerShape(8.dp))
                .clickable(
                    role = Role.Button,
                    onClick = onBrowseClick,
                )
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "잠시 딴짓하러 가기",
                color = Brown950,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight.Medium,
                ),
            )
            LoadingChevronRight(
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@Composable
private fun LoadingProgressRow(
    progress: Float,
    progressPercent: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(21.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(6.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Gray100),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress.coerceIn(0f, 1f))
                    .height(6.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Brown950),
            )
        }
        Text(
            text = "$progressPercent%",
            color = Gray900,
            textAlign = TextAlign.Right,
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 14.sp,
                lineHeight = 21.sp,
                fontWeight = FontWeight.Normal,
            ),
        )
    }
}

@Composable
private fun RewardComment(
    rewardCount: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.height(21.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "완료 후",
            color = Gray900,
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 14.sp,
                lineHeight = 21.sp,
                fontWeight = FontWeight.Normal,
            ),
        )
        Image(
            painter = painterResource(com.f1.quiket.core.designsystem.R.drawable.ic_acorn),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
        )
        Text(
            text = rewardCount.toString(),
            color = Brown950,
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 14.sp,
                lineHeight = 21.sp,
                fontWeight = FontWeight.SemiBold,
            ),
        )
        Text(
            text = "획득 예정이에요!",
            color = Gray900,
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 14.sp,
                lineHeight = 21.sp,
                fontWeight = FontWeight.Normal,
            ),
        )
    }
}

@Composable
private fun LoadingCharacterIllustration(
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier) {
        val scale = size.width / 328f
        fun x(value: Float) = value * scale
        fun y(value: Float) = value * scale
        val originY = y(34f)
        fun p(x: Float, y: Float) = Offset(x(x), originY + y(y))

        drawCircle(
            brush = Brush.linearGradient(
                colors = listOf(Color(0xFFF9D7B4), Color(0xFFECB781)),
                start = p(78f, 30f),
                end = p(132f, 160f),
            ),
            radius = x(64f),
            center = p(94f, 86f),
        )
        drawArc(
            color = Color(0xFFE8BC8F),
            startAngle = -58f,
            sweepAngle = 118f,
            useCenter = false,
            topLeft = p(54f, 72f),
            size = Size(x(52f), y(112f)),
            style = Stroke(width = x(2.3f), cap = StrokeCap.Round),
        )

        drawOval(
            brush = Brush.linearGradient(
                colors = listOf(Color(0xFFFFD6AB), Color(0xFFF2C18F)),
                start = p(156f, 76f),
                end = p(226f, 242f),
            ),
            topLeft = p(153f, 88f),
            size = Size(x(92f), y(174f)),
        )
        drawOval(
            color = Color(0xFFFFE9C6),
            topLeft = p(174f, 150f),
            size = Size(x(78f), y(92f)),
        )

        drawEar(center = p(155f, 57f), size = x(29f), rotation = -28f)
        drawEar(center = p(226f, 54f), size = x(31f), rotation = 32f)

        rotate(degrees = -8f, pivot = p(190f, 86f)) {
            drawOval(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFFF7D3B3), Color(0xFFF2C18F)),
                    start = p(158f, 36f),
                    end = p(220f, 128f),
                ),
                topLeft = p(150f, 44f),
                size = Size(x(88f), y(90f)),
            )
            drawOval(
                color = Color(0xFFFCEED7),
                topLeft = p(145f, 83f),
                size = Size(x(112f), y(60f)),
            )
            drawOval(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFFFFBDBD), Color(0x00FFBDBD)),
                    center = p(158f, 112f),
                    radius = x(35f),
                ),
                topLeft = p(135f, 92f),
                size = Size(x(58f), y(52f)),
            )
            drawOval(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFFFFBDBD), Color(0x00FFBDBD)),
                    center = p(229f, 104f),
                    radius = x(30f),
                ),
                topLeft = p(205f, 84f),
                size = Size(x(56f), y(48f)),
            )
        }

        drawOval(color = Brown950, topLeft = p(174f, 83f), size = Size(x(15f), y(23f)))
        drawOval(color = Brown950, topLeft = p(213f, 80f), size = Size(x(17f), y(22f)))
        drawRoundRect(
            color = Brown950,
            topLeft = p(195f, 100f),
            size = Size(x(20f), y(10f)),
            cornerRadius = CornerRadius(x(4f), x(4f)),
        )
        drawArc(
            color = Brown950,
            startAngle = 18f,
            sweepAngle = 78f,
            useCenter = false,
            topLeft = p(184f, 99f),
            size = Size(x(27f), y(20f)),
            style = Stroke(width = x(2f), cap = StrokeCap.Round),
        )
        drawPath(
            path = Path().apply {
                moveTo(p(211f, 111f).x, p(211f, 111f).y)
                lineTo(p(229f, 106f).x, p(229f, 106f).y)
                lineTo(p(218f, 124f).x, p(218f, 124f).y)
                close()
            },
            color = Color(0xFFFFC0B9),
        )
        drawStripe(p(178f, 48f), p(185f, 75f), x(8f))
        drawStripe(p(196f, 43f), p(199f, 75f), x(8f))
        drawStripe(p(216f, 49f), p(211f, 77f), x(8f))

        rotate(degrees = 23f, pivot = p(158f, 174f)) {
            drawRoundRect(
                color = Color(0xFFF9D7B4),
                topLeft = p(126f, 137f),
                size = Size(x(37f), y(83f)),
                cornerRadius = CornerRadius(x(20f), x(20f)),
            )
        }
        rotate(degrees = -52f, pivot = p(225f, 145f)) {
            drawRoundRect(
                color = Color(0xFFF9D7B4),
                topLeft = p(211f, 119f),
                size = Size(x(36f), y(80f)),
                cornerRadius = CornerRadius(x(18f), x(18f)),
            )
        }

        rotate(degrees = 10f, pivot = p(244f, 126f)) {
            drawRoundRect(
                brush = Brush.linearGradient(
                    colors = listOf(White, Blue100),
                    start = p(208f, 82f),
                    end = p(276f, 185f),
                ),
                topLeft = p(205f, 82f),
                size = Size(x(89f), y(92f)),
                cornerRadius = CornerRadius(x(11f), x(11f)),
            )
            drawRoundRect(
                color = Blue200,
                topLeft = p(205f, 80f),
                size = Size(x(89f), y(3f)),
                cornerRadius = CornerRadius(x(8f), x(8f)),
            )
        }

        drawRoundRect(
            color = Brown800,
            topLeft = p(95f, 185f),
            size = Size(x(79f), y(49f)),
            cornerRadius = CornerRadius(x(26f), x(26f)),
        )
        drawOval(
            color = Brown950,
            topLeft = p(103f, 184f),
            size = Size(x(62f), y(27f)),
        )
        drawAcornInBag(center = p(126f, 191f), rotation = -20f, scale = scale * 0.8f)
        drawAcornInBag(center = p(146f, 194f), rotation = 22f, scale = scale * 0.86f)

        drawOval(
            brush = Brush.linearGradient(
                colors = listOf(Color(0xFFFFECD8), Color(0xFFECB781)),
                start = p(137f, 237f),
                end = p(162f, 255f),
            ),
            topLeft = p(126f, 225f),
            size = Size(x(39f), y(24f)),
        )
        drawOval(
            brush = Brush.linearGradient(
                colors = listOf(Color(0xFFFFECD8), Color(0xFFECB781)),
                start = p(226f, 238f),
                end = p(253f, 252f),
            ),
            topLeft = p(221f, 225f),
            size = Size(x(36f), y(24f)),
        )
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawEar(
    center: Offset,
    size: Float,
    rotation: Float,
) {
    rotate(degrees = rotation, pivot = center) {
        drawRoundRect(
            brush = Brush.linearGradient(
                colors = listOf(Color(0xFFF9D7B4), Color(0xFFF2C18F)),
                start = Offset(center.x - size, center.y - size),
                end = Offset(center.x + size, center.y + size),
            ),
            topLeft = Offset(center.x - size * 0.5f, center.y - size * 0.45f),
            size = Size(size, size * 0.85f),
            cornerRadius = CornerRadius(size * 0.28f, size * 0.28f),
        )
        drawCircle(
            color = Color(0xFFE8BC8F),
            radius = size * 0.2f,
            center = center,
        )
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawStripe(
    start: Offset,
    end: Offset,
    strokeWidth: Float,
) {
    drawLine(
        color = Color(0xFFE8BC8F),
        start = start,
        end = end,
        strokeWidth = strokeWidth,
        cap = StrokeCap.Round,
    )
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawAcornInBag(
    center: Offset,
    rotation: Float,
    scale: Float,
) {
    rotate(degrees = rotation, pivot = center) {
        drawOval(
            brush = Brush.linearGradient(
                colors = listOf(Color(0xFFFFD7A8), Color(0xFFC87617)),
                start = Offset(center.x - 12f * scale, center.y - 12f * scale),
                end = Offset(center.x + 12f * scale, center.y + 14f * scale),
            ),
            topLeft = Offset(center.x - 13f * scale, center.y - 9f * scale),
            size = Size(26f * scale, 28f * scale),
        )
        drawOval(
            color = Brown500,
            topLeft = Offset(center.x - 14f * scale, center.y - 13f * scale),
            size = Size(28f * scale, 15f * scale),
        )
        drawLine(
            color = Brown800,
            start = Offset(center.x, center.y - 15f * scale),
            end = Offset(center.x + 5f * scale, center.y - 20f * scale),
            strokeWidth = 3f * scale,
            cap = StrokeCap.Round,
        )
    }
}

@Composable
private fun LoadingChevronRight(
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier) {
        val strokeWidth = 3.dp.toPx()
        drawLine(
            color = Brown950,
            start = Offset(size.width * 0.36f, size.height * 0.18f),
            end = Offset(size.width * 0.66f, size.height * 0.5f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round,
        )
        drawLine(
            color = Brown950,
            start = Offset(size.width * 0.66f, size.height * 0.5f),
            end = Offset(size.width * 0.36f, size.height * 0.82f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round,
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun CreateQuizLoadingScreenPreview() {
    QuiketTheme {
        CreateQuizLoadingScreen(
            progress = 0.4f,
            rewardCount = 10,
            onBrowseClick = {},
        )
    }
}
