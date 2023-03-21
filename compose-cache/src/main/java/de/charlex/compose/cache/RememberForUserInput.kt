package de.charlex.compose.cache

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
@Deprecated(message = "", replaceWith = ReplaceWith(expression = "rememberForUserInput(value, onValueChange)"), level = DeprecationLevel.ERROR)
fun <T> rememberLocalCache(value: T, saveDebounceMillis: Long? = null, onValueChange: (T) -> Unit): Pair<T, (T) -> Unit> {
    error("Not implemented anymore. Use rememberForUserInput")
}

@Composable
fun <T> rememberForUserInput(value: T, onValueChange: (T) -> Unit): Pair<T, (T) -> Unit> {

    var currentValue by remember { mutableStateOf(value) }
    var dirty by remember { mutableStateOf(false) }

    SideEffect {
        if(value != currentValue && !dirty) {
            // We are getting updates from outside and we are NOT dirty
            currentValue = value
        } else if(value == currentValue) {
            dirty = false
        } else {
            // Stay on local changes. We are still dirty...
        }
    }

    return currentValue to remember(onValueChange) {{
        dirty = true
        currentValue = it
        onValueChange(it)
    }}
}
