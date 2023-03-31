package kotlin.com.bumble.appyx.multiplatform.core.state

import androidx.compose.runtime.saveable.SaverScope

interface MutableSavedStateMap : MutableMap<String, Any?> {
    val saverScope: SaverScope
}
