package com.f1.quiket.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.f1.quiket.core.designsystem.theme.Brown950
import com.f1.quiket.core.designsystem.theme.Gray100
import com.f1.quiket.core.designsystem.theme.Gray300
import com.f1.quiket.core.designsystem.theme.QuiketTheme
import com.f1.quiket.core.designsystem.theme.White

// 로그인, 다음, 회원가입, 약관 동의 때 재사용 가능한 Button
@Composable
fun QuiketPrimaryButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        contentPadding = PaddingValues(vertical = 12.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (enabled) {
                Brown950
            } else {
                Gray100
            },
            contentColor = if (enabled) {
                White
            } else {
                Gray300
            },
            disabledContainerColor = Gray100,
            disabledContentColor = Gray300
        ),

        ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun QuiketPrimaryButtonPreview() {
    QuiketTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 비활성 상태
            QuiketPrimaryButton(
                text = "로그인",
                enabled = false,
                onClick = {}
            )

            // 활성 상태
            QuiketPrimaryButton(
                text = "다음",
                enabled = true,
                onClick = {}
            )
        }
    }
}
