# cache
## Current Compose Version: 1.1.0

<a href="https://github.com/Ch4rl3x/compose-cache/actions?query=workflow%3ABuild"><img src="https://github.com/ch4rl3x/compose-cache/actions/workflows/build.yml/badge.svg" alt="Build"></a>
<a href="https://www.codefactor.io/repository/github/ch4rl3x/compose-cache"><img src="https://www.codefactor.io/repository/github/ch4rl3x/compose-cache/badge" alt="CodeFactor" /></a>
<a href="https://repo1.maven.org/maven2/de/charlex/compose/compose-cache/"><img src="https://img.shields.io/maven-central/v/de.charlex.compose/compose-cache" alt="Maven Central" /></a>

# Add to your project

Add actual compose-cache library:

```groovy
dependencies {
    implementation 'de.charlex.compose:compose-cache:1.0.0-rc02'
}
```

# How does it work?

## Provide the SettingsDataStore

```kotlin

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    ...
    setContent {
        //Provide SettingsDataStore around
        CompositionLocalProvider(LocalSettingsDataStore provides settingsDataStore) {
            ...
        }
    }
}

```

## Use one of the following ways

### SettingsContext

```kotlin
Setting(
		key = booleanPreference("key1", false),
    saveDebounceMillis = 250 //Optional
) { value, onValueChange ->
    Switch(
        checked = value,
        onCheckedChange = onValueChanged
    )
}
```

### rememberSettingsValue

```kotlin
val (value, onValueChange) = rememberSettingsValue(key = booleanPreference("key2", false))
Checkbox(checked = value, onCheckedChange = onValueChange)
```

### rememberLocalCache

rememberLocalCache is the base implementation of both, SettingsContext and rememberSettingsValue. 

*It can also be used for other value/onValueChange behaviors where race conditions occurs*

```kotlin
val prefKey = stringPreference("email", "max.mustermann@googlemail.com")
val coroutineScope = rememberCoroutineScope()
val settingsValue by settingsDataStore.get(prefKey).collectAsState(initial = prefKey.defaultValue)

val (value, onValueChange) = rememberLocalCache(settingsValue, saveDebounceMillis = 500) {
		coroutineScope.launch {
				settingsDataStore.put(myPrefKey, it)
    }
}
TextField(value = value, onValueChange = onValueChange)
```



That's it!

License
--------

    Copyright 2022 Alexander Karkossa
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
       http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
