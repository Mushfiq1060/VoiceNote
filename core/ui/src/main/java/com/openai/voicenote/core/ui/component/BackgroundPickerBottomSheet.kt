package com.openai.voicenote.core.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.openai.voicenote.core.designsystem.icon.VnColor
import com.openai.voicenote.core.designsystem.icon.VnIcons
import com.openai.voicenote.core.designsystem.icon.VnImage
import com.openai.voicenote.core.designsystem.theme.VnTheme

@Composable
fun BackgroundPickerBottomSheet(
    onBottomSheetDismiss: () -> Unit,
    onBackgroundColorChange: (Int) -> Unit,
    onBackgroundImageChange: (Int) -> Unit,
    selectedBackgroundColor: Int,
    selectedBackgroundImage: Int
) {
    BottomSheet(onDismiss = { onBottomSheetDismiss() }) {
        ColorContent(
            selectedBackgroundColor = selectedBackgroundColor,
            onBackgroundColorChange = { onBackgroundColorChange(it) }
        )
        ImageContent(
            selectedBackgroundImage = selectedBackgroundImage,
            onBackgroundImageChange = { onBackgroundImageChange(it) }
        )
    }
}

@Composable
fun ColorContent(
    selectedBackgroundColor: Int,
    onBackgroundColorChange: (Int) -> Unit
) {
    Column {
        Text(
            text = "Color",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp)
        )
        ColorRow(
            selectedBackgroundColor = selectedBackgroundColor,
            onBackgroundColorChange = { onBackgroundColorChange(it) }
        )
    }
}

@Composable
fun ColorRow(
    selectedBackgroundColor: Int,
    onBackgroundColorChange: (Int) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(VnColor.bgColorList) {
            Box(
                modifier = Modifier.size(48.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .border(
                            shape = CircleShape, border = BorderStroke(
                                width = if (selectedBackgroundColor == it) 2.dp else 1.dp,
                                color = if (selectedBackgroundColor == it) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                            )
                        )
                        .clickable(onClick = { onBackgroundColorChange(it) })
                ) {
                    if (it == Color.Transparent.toArgb() && it != selectedBackgroundColor) {
                        Icon(
                            painter = painterResource(id = VnIcons.noColor),
                            modifier = Modifier
                                .size(36.dp)
                                .align(Alignment.Center),
                            contentDescription = "no_color"
                        )
                    } else {
                        Spacer(
                            modifier = Modifier
                                .size(48.dp)
                                .background(color = Color(it))
                        )
                    }
                }
                if (it == selectedBackgroundColor) {
                    Icon(
                        painter = painterResource(id = VnIcons.check),
                        modifier = Modifier
                            .size(42.dp)
                            .align(Alignment.Center),
                        contentDescription = "check",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun ImageContent(
    selectedBackgroundImage: Int,
    onBackgroundImageChange: (Int) -> Unit
) {
    Column {
        Text(
            text = "Background",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp)
        )
        BackgroundRow(
            selectedBackgroundImage = selectedBackgroundImage,
            onBackgroundImageChange = { onBackgroundImageChange(it) }
        )
    }
}

@Composable
fun BackgroundRow(
    selectedBackgroundImage: Int,
    onBackgroundImageChange: (Int) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(VnImage.bgImageList) {
            Box(
                modifier = Modifier.size(64.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .border(
                            shape = CircleShape, border = BorderStroke(
                                width = if (selectedBackgroundImage == it.id) 3.dp else 1.dp,
                                color = if (selectedBackgroundImage == it.id) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                            )
                        )
                        .clickable(onClick = { onBackgroundImageChange(it.id) })
                ) {
                    if (it.id == VnImage.bgImageList[0].id) {
                        Icon(
                            painter = painterResource(id = it.drawableId),
                            modifier = Modifier
                                .size(48.dp)
                                .align(Alignment.Center),
                            contentDescription = "no_image"
                        )
                    } else {
                        Image(
                            painter = painterResource(id = it.drawableId),
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds,
                        )
                    }
                }
                if (it.id == selectedBackgroundImage) {
                    Box(
                        modifier = Modifier
                            .offset(x = 42.dp, y = 0.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(
                            painter = painterResource(id = VnIcons.check),
                            modifier = Modifier
                                .size(24.dp)
                                .clip(RoundedCornerShape(14.dp)),
                            contentDescription = "check_circle",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ColorContentPreview() {
    VnTheme {
        Surface {
            ColorContent(
                selectedBackgroundColor = Color.Transparent.toArgb(),
                onBackgroundColorChange = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ImageContentPreview() {
    VnTheme {
        Surface {
            ImageContent(
                selectedBackgroundImage = VnImage.bgImageList[0].id,
                onBackgroundImageChange = {}
            )
        }
    }
}



