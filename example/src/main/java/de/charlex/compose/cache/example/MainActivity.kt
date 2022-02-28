package de.charlex.compose.cache.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import de.charlex.compose.cache.example.theme.SettingsTheme
import de.charlex.compose.cache.rememberLocalCache
import de.charlex.settings.datastore.SettingsDataStore
import de.charlex.settings.datastore.stringPreference
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val settingsDataStore = SettingsDataStore.create(this)
        setContent {
            Body(settingsDataStore = settingsDataStore)
        }
    }
}

@Composable
fun Body(
    settingsDataStore: SettingsDataStore
) {

    SettingsTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Column {
                val myPrefKey = stringPreference("email", "max.mustermann@googlemail.com")
                val coroutineScope = rememberCoroutineScope()
                val settingsValue by settingsDataStore.get(myPrefKey).collectAsState(initial = myPrefKey.defaultValue)

                val (value, onValueChange) = rememberLocalCache(settingsValue, saveDebounceMillis = 500) {
                    coroutineScope.launch {
                        settingsDataStore.put(myPrefKey, it)
                    }
                }

                TextField(value = value, onValueChange = onValueChange)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SettingsTheme {
        Body(settingsDataStore = SettingsDataStore.createInMemory())
    }
}
