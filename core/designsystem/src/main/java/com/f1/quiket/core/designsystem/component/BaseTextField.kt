package com.f1.quiket.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.f1.quiket.core.designsystem.R
import com.f1.quiket.core.designsystem.theme.Gray400
import com.f1.quiket.core.designsystem.theme.Gray50
import com.f1.quiket.core.designsystem.theme.Gray950
import com.f1.quiket.core.designsystem.theme.Negative
import com.f1.quiket.core.designsystem.theme.QuiketTheme

@Composable
fun BaseTextField(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String? = null,
    singleLine: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    trailingIcon: (@Composable (() -> Unit))? = null,
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Normal,
                color = Gray950
            ),
            placeholder = {
                Text(
                    text = hint,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Normal
                    ),
                    color = Gray400
                )
            },
            isError = isError,
            singleLine = singleLine,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            trailingIcon = trailingIcon,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                // 배경
                focusedContainerColor = Gray50,
                unfocusedContainerColor = Gray50,
                errorContainerColor = Gray50,

                // 텍스트 색상
                focusedTextColor = Gray950,
                unfocusedTextColor = Gray950,
                errorTextColor = Gray950,

                // placeholder 색상
                focusedPlaceholderColor = Gray400,
                unfocusedPlaceholderColor = Gray400,

                // unfocused일 때
                unfocusedBorderColor = Color.Transparent,

                // 에러
                errorBorderColor = Negative
            ),
        )
        if (isError && !errorMessage.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = errorMessage,
                color = Negative,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 2.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BaseTextFieldPreview() {
    QuiketTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 기본
            BaseTextField(
                value = "",
                onValueChange = {},
                hint = "이메일을 입력해주세요"
            )

            // 입력, focused x
            BaseTextField(
                value = "test@email.com",
                onValueChange = {},
                hint = "이메일을 입력해주세요"
            )

            // 비밀번호 다시 입력
            BaseTextField(
                value = "wrong-email",
                onValueChange = {},
                hint = "비밀번호를 입력해주세요",
                isError = true,
                errorMessage = "비밀번호를 다시 입력해주세요 (1/5)"
            )

            // trailingIcon 있는 상태
            BaseTextField(
                value = "password123",
                onValueChange = {},
                hint = "비밀번호를 입력해주세요",
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_password),
                        contentDescription = null
                    )
                }
            )
        }
    }
}