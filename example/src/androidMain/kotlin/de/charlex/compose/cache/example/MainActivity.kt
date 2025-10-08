package de.charlex.compose.cache.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import de.charlex.compose.cache.example.theme.SettingsTheme
import de.charlex.compose.cache.rememberForUserInput
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Body()
        }
    }
}

@Composable
fun Body(
) {
    val viewModel = remember { MainViewModel() }

    SettingsTheme {
        Scaffold(
            modifier = Modifier.statusBarsPadding()
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                color = MaterialTheme.colors.background
            ) {
                Column {
                    val coroutineScope = rememberCoroutineScope()
                    val state by viewModel.state.collectAsState()

                    val (value, onValueChange) = rememberForUserInput(state) {
                        coroutineScope.launch {
                            delay(250)
                            viewModel.state.compareAndSet(state, it)
                        }
                    }
                    TextField(
                        value = value,
                        onValueChange = onValueChange
                    )
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SettingsTheme {
        Body()
    }
}
