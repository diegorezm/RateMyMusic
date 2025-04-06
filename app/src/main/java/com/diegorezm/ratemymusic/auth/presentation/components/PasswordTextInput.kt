package com.diegorezm.ratemymusic.auth.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.diegorezm.ratemymusic.R

@Composable
fun PasswordTextInput(
    modifier: Modifier = Modifier,
    password: String = "",
    onPasswordChange: (String) -> Unit = {},
) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = password,
        onValueChange = {
            onPasswordChange(it)
        },
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        label = { Text(stringResource(R.string.password_hint)) },
        modifier = modifier.fillMaxWidth(),
        trailingIcon = {
            val description = if (isPasswordVisible) "Hide password" else "Show password"

            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                if (isPasswordVisible)
                    Image(
                        painter = painterResource(id = R.drawable.ic_visibility),
                        contentDescription = "Google Logo",
                        modifier = Modifier.size(24.dp)
                    )
                else Image(
                    painter = painterResource(id = R.drawable.ic_visibility_off),
                    contentDescription = description,
                    modifier = Modifier.size(24.dp),

                    )
            }
        }
    )

}

@Preview(name = "PasswordTextInput")
@Composable
private fun PreviewPasswordTextInput() {
    PasswordTextInput()
}