package com.zeit.build.prop.extractor.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.zeit.build.prop.extractor.ui.theme.BuildPropExtractorTheme

@Composable
fun PropView(key: String, propVal: String) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .clip(shape = RoundedCornerShape(Dp(8f)))
        .background(color = Color.LightGray)
        .padding(Dp(12f))
    ) {
        Column {
            Text(text = key, color = Color.Black)
            Spacer(modifier = Modifier.height(Dp(4f)))
            Text(text = propVal, color = Color.Gray)
        }
    }
}

@Preview(showBackground = true, widthDp = 280)
@Composable
fun PropViewPreview() {
    BuildPropExtractorTheme {
        PropView("prefix.group.subgroup.name.type", "someValue")
    }
}