package de.charlex.compose.cache

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.structuralEqualityPolicy
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.receiveAsFlow

@FlowPreview
@Composable
fun <T> rememberLocalCache(value: T, saveDebounceMillis: Long? = null, onValueChange: (T) -> Unit): Pair<T, (T) -> Unit> {
    var internalValue by remember(value) { mutableStateOf(value, structuralEqualityPolicy()) }

    // remember a channel we can send to.
    // Conflated because we want old values to be replaced by new.
    // queryChannel.offer will always succeed.
    val settingsDataStoreChannel = remember { Channel<T>(Channel.CONFLATED) }

    saveDebounceMillis?.let {

        // LaunchedEffect will run our valueChange and cancel everything when
        // this composable leaves the composition. If we get a different
        // key object or settingsDataStore object this will cancel the old effect and launch a new one.
        LaunchedEffect(Unit) {
            // Use withContext here to change dispatchers if desired
            settingsDataStoreChannel.receiveAsFlow()
                .debounce(it)
                .collect {
                    onValueChange(it)
                }
        }
    }

    return internalValue to {
        internalValue = it
        if (saveDebounceMillis != null) {
            settingsDataStoreChannel.trySend(it)
        } else {
            onValueChange(it)
        }
    }
}
