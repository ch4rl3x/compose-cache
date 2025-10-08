package de.charlex.compose.cache

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun <T> rememberForUserInput(
    value: T,
    equals: (T, T) -> Boolean = { a, b -> a == b },
    onValueChange: (T) -> Unit
): Pair<T, (T) -> Unit> {

    var currentValue by remember { mutableStateOf(value) }
    var dirty by remember { mutableStateOf(false) }

    SideEffect {
        if (equals(currentValue, value).not() && !dirty) {
            // We are getting updates from outside and we are NOT dirty
            currentValue = value
        } else if (equals(value, currentValue)) {
            dirty = false
        } else {
            // Stay on local changes. We are still dirty...
        }
    }

    return currentValue to remember(onValueChange) {
        {
            dirty = equals(currentValue, it).not()
            currentValue = it
            if (dirty)
                onValueChange(it)
        }
    }
}
