package com.bumble.appyx.navmodel.tiles.operation

import com.bumble.appyx.core.navigation.NavElements
import com.bumble.appyx.core.navigation.NavKey
import com.bumble.appyx.navmodel.tiles.Tiles
import com.bumble.appyx.navmodel.tiles.Tiles.State.STANDARD
import com.bumble.appyx.navmodel.tiles.TilesElements
import kotlinx.parcelize.Parcelize

@Parcelize
data class BecomeVisible<T : Any>(
    private val key: NavKey<T>
) : TilesOperation<T> {

    override fun isApplicable(elements: TilesElements<T>): Boolean = true

    override fun invoke(
        elements: TilesElements<T>,
    ): NavElements<T, Tiles.State> =
        elements.transitionTo(STANDARD) {
            it.key == key && it.fromState == Tiles.State.HIDDEN
        }
}

fun <T : Any> Tiles<T>.becomeVisible(key: NavKey<T>) {
    accept(BecomeVisible(key))
}
