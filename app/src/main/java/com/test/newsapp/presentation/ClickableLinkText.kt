package com.test.newsapp.presentation

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat.startActivity

@Composable
fun ClickableLinkText(
    url: String,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current

    val text = buildAnnotatedString {
        append("Reference Link ")
        pushStringAnnotation("URL", url)
        withStyle(
            style = SpanStyle(
                color = Color.Blue,
                textDecoration = TextDecoration.Underline
            )
        ) {
            append(url)
        }
        pop()
    }

    ClickableText(
        text = text,
        onClick = { offset ->
            text.getStringAnnotations("URL", offset, offset)
                .firstOrNull()?.let { annotation ->
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(annotation.item)
                    )
                    startActivity(context, intent, null)
                }
        },
        modifier = modifier,
        style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
    )
}

@Preview
@Composable
fun ClickableLinkTextPreview() {
    Surface {
        ClickableLinkText(url = "https://www.example.com")
    }
}