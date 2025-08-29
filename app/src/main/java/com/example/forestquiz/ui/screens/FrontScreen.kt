package com.example.forestquiz.ui.screens

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FrontScreen(
    onNavigateToWeeklyQuiz: () -> Unit,
    onNavigateToLearn: () -> Unit,
    onNavigateToRandomQuiz: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .border(BorderStroke(1.dp, MaterialTheme.colorScheme.onSecondaryContainer))
            .background(color = MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Forest Quiz", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "NPTEL Course Quiz App:\nForests and their Management",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                style = MaterialTheme.typography.titleMedium
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(modifier = Modifier.width(250.dp), onClick = onNavigateToWeeklyQuiz) {
                Text(text = "Take Weekly Quiz")
            }
            Button(modifier = Modifier.width(250.dp), onClick = onNavigateToRandomQuiz) {
                Text(text = "Take Randomized Quiz")
            }
            Button(modifier = Modifier.width(250.dp), onClick = onNavigateToLearn) {
                Text(text = "Learn by Week")
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            HyperlinkText(
                fullText = "Made by Ghouse",
                linkText = listOf("Ghouse"),
                hyperlinks = listOf("https://github.com/WreckedSky")
            )
            HyperlinkText(
                fullText = "Made using Jetpack Compose",
                linkText = listOf("Jetpack Compose"),
                hyperlinks = listOf("https://developer.android.com/develop/ui/compose")
            )
        }
    }
}

@Composable
private fun HyperlinkText(
    modifier: Modifier = Modifier,
    fullText: String,
    linkText: List<String>,
    linkTextColor: Color = Color.Cyan,
    linkTextFontWeight: FontWeight = FontWeight.Normal,
    linkTextDecoration: TextDecoration = TextDecoration.Underline,
    hyperlinks: List<String>,
    fontSize: TextUnit = TextUnit.Unspecified
) {
    val annotatedString = buildAnnotatedString {
        append(fullText)
        addStyle(
            style = SpanStyle(
                fontSize = fontSize,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            ),
            start = 0,
            end = fullText.length
        )

        linkText.forEachIndexed { index, link ->
            val startIndex = fullText.indexOf(link)
            val endIndex = startIndex + link.length
            addStyle(
                style = SpanStyle(
                    color = linkTextColor,
                    fontSize = fontSize,
                    fontWeight = linkTextFontWeight,
                    textDecoration = linkTextDecoration
                ),
                start = startIndex,
                end = endIndex
            )
            addStringAnnotation(
                tag = "URL",
                annotation = hyperlinks[index],
                start = startIndex,
                end = endIndex
            )
        }
    }
    val uriHandler = LocalUriHandler.current
    ClickableText(modifier = modifier, text = annotatedString, onClick = {
        annotatedString.getStringAnnotations("URL", it, it).firstOrNull()
            ?.let { stringAnnotation ->
                uriHandler.openUri(stringAnnotation.item)
            }
    })
}

@Preview(showBackground = true)
@Composable
fun FrontScreenPreview() {
    FrontScreen({}, {}, {})
}