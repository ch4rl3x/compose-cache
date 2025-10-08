package de.charlex.compose.cache.example

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel: ViewModel() {

    val state = MutableStateFlow("")

}
