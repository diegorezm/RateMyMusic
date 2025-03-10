package com.diegorezm.ratemymusic.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.diegorezm.ratemymusic.R

@Composable
fun GoogleSignInButton(text: String = "Sign in with Google") {
    OutlinedButton(
        onClick = {
            
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(4.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.google_logo_48x48), // Replace with actual drawable
                contentDescription = "Google Logo",
                modifier = Modifier.size(24.dp) // Adjust size if needed
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text)
        }
    }
}
