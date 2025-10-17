<a href="https://github.com/Ch4rl3x/compose-cache/actions?query=workflow%3ABuild"><img src="https://github.com/ch4rl3x/compose-cache/actions/workflows/build.yml/badge.svg" alt="Build"></a>
<a href="https://www.codefactor.io/repository/github/ch4rl3x/compose-cache"><img src="https://www.codefactor.io/repository/github/ch4rl3x/compose-cache/badge" alt="CodeFactor" /></a>
<a href="https://repo1.maven.org/maven2/de/charlex/compose/compose-cache/"><img src="https://img.shields.io/maven-central/v/de.charlex.compose/compose-cache" alt="Maven Central" /></a>

# compose-cache

`compose-cache` is a lightweight utility for Jetpack Compose that helps you handle two-way state synchronization between UI and external sources (e.g. databases, flows) — without suffering from cursor jumps or overwritten user input.

> [!NOTE]  
> 🚀 RevealSwipe is now Compose Multiplatform

## 🧠 The Problem

In Compose, text fields are typically bound to a single source of truth — for example, a `StateFlow` or immutable UI state provided by a ViewModel.
When the user types, you usually:

1. Send the new input to the ViewModel (`onValueChange`)
2. The ViewModel emits a new UI state (often via `combine`, `copy`, or other transformations)
3. The UI collects this state and updates the `TextField` value accordingly

The issue arises because state emission and recomposition are asynchronous.
If the user types quickly, there can be a short delay before the ViewModel emits the updated state. During that window:

* The `TextField` still receives the old value from the last emission
* Compose updates the UI with that outdated value
* This causes the cursor to jump or recently typed characters to disappear

This can happen even without databases — simply using `collectAsState()` with a ViewModel is enough to reproduce it, especially in multi-field forms or with combined UI state.

## 🛠 The Solution

`rememberForUserInput` wraps your state with a temporary local cache.
When the user types, it stores the input locally and marks the state as dirty.
While dirty, external updates are ignored to prevent overwriting user input.
Once the external source emits the same value that the user typed, the dirty flag is cleared and control is handed back to the source.

👉 This means the UI stays responsive, and your database stays the single source of truth — without flicker or cursor issues.


## Dependency

Add actual compose-cache library:

```groovy
dependencies {
    implementation 'de.charlex.compose:compose-cache:3.0.0'
}
```

## How does it work?

`rememberForUserInput` can be used for every value/onValueChange behaviors where race conditions occurs

```kotlin
val (text, onTextChange) = rememberForUserInput(
    value = dbValue,
    onValueChange = { newValue ->
        viewModel.updateDatabase(newValue)
    }
)

TextField(
    value = text,
    onValueChange = onTextChange
)
```

### Optional: Custom equality check

If your value isn’t a simple primitive, you can provide a custom equals function:

```kotlin
val (item, onItemChange) = rememberForUserInput(
    value = selectedItem,
    equals = { a, b -> a.id == b.id },
    onValueChange = { newItem -> updateInDb(newItem) }
)
```

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
