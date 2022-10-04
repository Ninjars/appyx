package com.bumble.appyx.navmodel.tiles.operation

import com.bumble.appyx.core.navigation.NavElements
import com.bumble.appyx.core.navigation.NavKey
import com.bumble.appyx.navmodel.tiles.Tiles
import com.bumble.appyx.navmodel.tiles.Tiles.State.HIDDEN
import com.bumble.appyx.navmodel.tiles.TilesElements
import kotlinx.parcelize.Parcelize

@Parcelize
data class BecomeHidden<T : Any>(
    private val key: NavKey<T>
) : TilesOperation<T> {

    override fun isApplicable(elements: TilesElements<T>): Boolean = true

    override fun invoke(
        elements: TilesElements<T>,
    ): NavElements<T, Tiles.State> =
        elements.transitionTo(HIDDEN) {
            it.key == key && it.fromState != HIDDEN
        }
}

fun <T : Any> Tiles<T>.becomeHidden(key: NavKey<T>) {
    accept(BecomeHidden(key))
}
