package com.bumble.appyx.sandbox.client.tiles

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.bumble.appyx.core.composable.Child
import com.bumble.appyx.core.composable.visibleChildrenAsState
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.ParentNode
import com.bumble.appyx.navmodel.tiles.Tiles
import com.bumble.appyx.navmodel.tiles.operation.deselect
import com.bumble.appyx.navmodel.tiles.operation.deselectAll
import com.bumble.appyx.navmodel.tiles.operation.removeSelected
import com.bumble.appyx.navmodel.tiles.operation.select
import com.bumble.appyx.navmodel.tiles.transitionhandler.rememberTileColumnTransitionHandler
import com.bumble.appyx.sandbox.client.child.ChildNode
import com.bumble.appyx.sandbox.client.tiles.TileColumnExampleNode.NavTarget
import kotlinx.coroutines.launch

class TileColumnExampleNode(
    buildContext: BuildContext,
    private val tiles: Tiles<NavTarget> = Tiles(
        initialItems = (0..40).map { NavTarget(it.toString()) }
    ),
) : ParentNode<NavTarget>(
    navModel = tiles,
    buildContext = buildContext,
) {

    data class NavTarget(val id: String)

    override fun resolve(navTarget: NavTarget, buildContext: BuildContext): Node =
        ChildNode(navTarget.id, buildContext)

    @Composable
    override fun View(modifier: Modifier) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(
                onClick = { tiles.removeSelected() },
            ) {
                Text(text = "Remove selected")
            }

            BoxWithConstraints(
                modifier = modifier
                    .fillMaxSize()
            ) {
                val elements by tiles.visibleChildrenAsState()
                // Need to do recomposition tests to see if this actually helps
                val isAnyElementSelected by remember {
                    derivedStateOf {
                        elements.any { it.targetState == Tiles.State.SELECTED }
                    }
                }
                val expandedElementSpacingDp = 60.dp
                val expandedElementSpacingPixels =
                    LocalDensity.current.run { expandedElementSpacingDp.roundToPx() }
                val listState: LazyListState = rememberLazyListState()
                val coroutineScope = rememberCoroutineScope()
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    state = listState,
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    userScrollEnabled = !isAnyElementSelected,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    itemsIndexed(elements) { index, element ->
                        Child(
                            navElement = element,
                            transitionHandler = rememberTileColumnTransitionHandler(
                                maxWidth,
                                maxHeight - expandedElementSpacingDp * 2
                            )
                        ) { child, _ ->
                            Box(modifier = Modifier
                                .clickable {
                                    when (element.fromState) {
                                        Tiles.State.STANDARD -> {
                                            tiles.deselectAll()
                                            tiles.select(element.key)
                                            coroutineScope.launch {
                                                listState.animateScrollToItem(
                                                    index,
                                                    -expandedElementSpacingPixels
                                                )
                                            }
                                        }
                                        Tiles.State.SELECTED -> {
                                            tiles.deselect(element.key)
                                        }
                                        Tiles.State.CREATED,
                                        Tiles.State.DESTROYED -> Unit
                                    }
                                }
                            ) {
                                child()
                            }
                        }
                    }
                }
            }
        }
    }
}
