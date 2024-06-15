package com.d3cima.tvgame

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.tv.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Surface
import com.d3cima.tvgame.server.Server
import com.d3cima.tvgame.server.WebsocketClient
import com.d3cima.tvgame.ui.theme.Purple80
import com.d3cima.tvgame.ui.theme.TVGAMETheme
import com.d3cima.tvgame.ui.theme.Typography

class MainActivity : ComponentActivity() {
    private val server = Server
    private var token = ""

    init {
        server.onConnect {
            token = server.token()

        }
    }

    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ctx = this
        setContent {
            TVGAMETheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = RectangleShape,
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        ShowToken(token)
                        Button(onClick = {

                        }) {
                            Text("send message")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun ShowToken(token: String, modifier: Modifier = Modifier) {
    Text(
        text = "$token",
        modifier = modifier,
        style = Typography.titleLarge,
        color = Purple80,
    )
}

@Preview(showBackground = true)
@Composable
fun ShowTokenPreview() {
    TVGAMETheme {
        ShowToken("00000000")
    }
}