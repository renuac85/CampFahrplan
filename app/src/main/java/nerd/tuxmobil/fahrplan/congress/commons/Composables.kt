package nerd.tuxmobil.fahrplan.congress.commons

import android.view.Gravity.CENTER
import android.view.Gravity.START
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration.Companion.Underline
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import nerd.tuxmobil.fahrplan.congress.R
import nerd.tuxmobil.fahrplan.congress.commons.TextResource.Empty
import nerd.tuxmobil.fahrplan.congress.commons.TextResource.Html
import nerd.tuxmobil.fahrplan.congress.commons.TextResource.PostalAddress
import nerd.tuxmobil.fahrplan.congress.extensions.toSpanned
import nerd.tuxmobil.fahrplan.congress.utils.LinkMovementMethodCompat

@Composable
fun Loading() {
    Box(
        Modifier.fillMaxSize(),
        Alignment.Center
    ) {
        CircularProgressIndicator(Modifier.size(48.dp))
    }
}

@Composable
fun NoData(text: String) {
    Box(
        Modifier.fillMaxSize(),
        Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(100.dp),
            text = text,
            lineHeight = TextUnit(1.5f, TextUnitType.Em),
        )
    }
}

@Composable
fun ClickableText(
    textResource: TextResource,
    fontSize: TextUnit,
    textAlign: TextAlign,
    @ColorRes textColor: Int,
    @ColorRes textLinkColor: Int,
    onClick: (String) -> Unit = {},
) {
    var vertical = 0.dp
    if (textResource is PostalAddress && textResource.text.isNotEmpty()) {
        vertical = 4.dp
    }
    if (textResource is Html && textResource.html.isNotEmpty()) {
        vertical = 4.dp
    }
    Box(Modifier.padding(horizontal = 16.dp, vertical = vertical)) {
        when (textResource) {
            Empty -> Unit
            is PostalAddress -> {
                GenericClickableText(
                    text = textResource.text,
                    plainUrl = textResource.text,
                    fontSize = fontSize,
                    textAlign = textAlign,
                    textLinkColor = colorResource(textLinkColor),
                    onClick = { onClick(textResource.text) },
                )
            }

            is Html -> {
                AndroidView(
                    factory = { context ->
                        TextView(context).apply {
                            movementMethod = LinkMovementMethodCompat.getInstance()
                            setTextColor(ContextCompat.getColor(context, textColor))
                            setLinkTextColor(ContextCompat.getColor(context, textLinkColor))
                            gravity = if (textAlign == TextAlign.Center) CENTER else START
                            textSize = fontSize.value
                            text = textResource.html.toSpanned()
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun GenericClickableText(
    text: String,
    plainUrl: String,
    fontSize: TextUnit,
    textAlign: TextAlign,
    textLinkColor: Color,
    onClick: (String) -> Unit,
) {
    if (text.isNotEmpty()) {
        val tag = "URL"
        val annotatedString = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = textLinkColor,
                    textDecoration = Underline,
                    fontSize = fontSize,
                )
            ) {
                append(text)
            }
            addStringAnnotation(
                tag = tag,
                annotation = plainUrl,
                start = 0,
                end = text.length,
            )
        }

        ClickableText(
            text = annotatedString,
            style = TextStyle(
                textAlign = textAlign,
            ),
            onClick = {
                annotatedString
                    .getStringAnnotations(tag = tag, start = it, end = it)
                    .firstOrNull()
                    ?.let { range -> onClick(range.item) }
            }
        )
    }
}

@Preview
@Composable
private fun ClickableTextPostalAddressPreview() {
    ClickableText(
        textResource = PostalAddress("Congressplatz 1, 20355 Hamburg"),
        fontSize = 18.sp,
        textAlign = TextAlign.Center,
        textColor = -1,
        textLinkColor = android.R.color.holo_blue_light,
        onClick = {},
    )
}

@Preview
@Composable
private fun ClickableTextHtmlPreview() {
    ClickableText(
        textResource = Html("""Design by <a href="https://eventfahrplan.eu">eventfahrplan.eu</a>"""),
        fontSize = 18.sp,
        textAlign = TextAlign.Center,
        textColor = R.color.colorPrimary,
        textLinkColor = android.R.color.holo_purple,
        onClick = {},
    )
}
