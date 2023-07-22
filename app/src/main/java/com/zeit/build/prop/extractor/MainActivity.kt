package com.zeit.build.prop.extractor

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.zeit.build.prop.extractor.controller.PropController
import com.zeit.build.prop.extractor.model.PropKey
import com.zeit.build.prop.extractor.ui.theme.BuildPropExtractorTheme
import com.zeit.build.prop.extractor.ui.view.PropView


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val controller = PropController.extractFromDevice()
        setContent {
            BuildPropExtractorTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background) {
                    Content(props = controller.props.entries.toList())
                }
            }
        }
    }
}

@Composable
fun Content(props: List<Map.Entry<PropKey, String>>) {
    Box(modifier = Modifier.padding(horizontal = Dp(16f))) {
        LazyColumn {
            item {
                Text(text = stringResource(id = R.string.general_total) + ": " + props.size.toString())
            }
            item {
                Spacer(modifier = Modifier.height(Dp(12f)))
            }
            items(props.size) { position ->
                val item = props[position]
                PropView(key = item.key.key, propVal = item.value)
                Spacer(modifier = Modifier.height(Dp(8f)))
            }
            item {
                Spacer(modifier = Modifier.height(Dp(64f)))
            }
        }
        Box(modifier = Modifier
            .padding(bottom = Dp(16f))
            .align(Alignment.BottomCenter)
        ) {
            PropExport(onFileCreated = { context, uri ->
                val streamWriter = context.contentResolver.openOutputStream(uri)
                for (entry in props) {
                    val line = entry.key.key + '=' + entry.value + '\n'
                    streamWriter?.write(line.toByteArray(Charsets.UTF_8))
                }
                streamWriter?.close()
                Toast.makeText(context, R.string.general_export_success, Toast.LENGTH_SHORT).show()
            })
        }
    }
}

@Composable
fun PropExport(onFileCreated: (Context, Uri) -> Unit) {
    val context = LocalContext.current
    val exportAction = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument(mimeType = "text/plain")) {
        if (it != null) {
            onFileCreated(context, it)
        }
    }
    TextButton(modifier = Modifier
        .height(Dp(48f))
        .fillMaxWidth()
        .clip(shape = RoundedCornerShape(Dp(8f)))
        .background(color = Color.Blue)
        .padding(horizontal = Dp(16f)),
        onClick = {
            exportAction.launch("device.prop")
        }) {
        Text(text = stringResource(R.string.general_export), color = Color.White)
    }
}

@Preview(showBackground = true, widthDp = 280, heightDp = 320)
@Composable
fun ContentPreview() {
    BuildPropExtractorTheme {
        val map = linkedMapOf(
            PropKey(group = "group1", name = "name1") to "value1",
            PropKey(group = "group1", name = "name2") to "value2",
            PropKey(group = "group1", subgroup = "subgroup1", name = "name3") to "value3",
            PropKey(group = "group1", subgroup = "subgroup1", name = "name3", type = "type1") to "value4",
            PropKey(prefix = "prefix1", group = "group2", subgroup = "subgroup2", name = "name4", type = "type2") to "value5",
        )
        Content(props = map.entries.toList())
    }
}