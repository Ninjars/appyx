package com.bumble.appyx.navmodel.tiles.operation

import com.bumble.appyx.core.navigation.NavElements
import com.bumble.appyx.core.navigation.NavKey
import com.bumble.appyx.navmodel.tiles.Tiles
import com.bumble.appyx.navmodel.tiles.Tiles.State.HIDDEN
import com.bumble.appyx.navmodel.tiles.Tiles.State.STANDARD
import com.bumble.appyx.navmodel.tiles.TilesElements
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransitionVisibleItems<T : Any>(
    private val elementIds: List<NavKey<T>>
) : TilesOperation<T> {

    override fun isApplicable(elements: TilesElements<T>): Boolean = true

    override fun invoke(
        elements: TilesElements<T>,
    ): NavElements<T, Tiles.State> =
        elements.map { element ->
            when {
                elementIds.contains(element.key) ->
                    element.transitionTo(
                        newTargetState = STANDARD,
                        operation = this
                    )
                !elementIds.contains(element.key) ->
                    element.transitionTo(
                        newTargetState = HIDDEN,
                        operation = this
                    )
                else -> element
            }
        }
}

fun <T : Any> Tiles<T>.transitionVisible(elementIds: List<NavKey<T>>) {
    accept(TransitionVisibleItems(elementIds))
}
