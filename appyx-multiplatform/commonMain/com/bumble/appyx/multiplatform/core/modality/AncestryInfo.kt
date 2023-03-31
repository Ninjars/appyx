package com.bumble.appyx.multiplatform.core.modality

import com.bumble.appyx.multiplatform.core.node.ParentNode

sealed class AncestryInfo {

    object Root : AncestryInfo()

    data class Child(
        val anchor: ParentNode<*>,
    ) : AncestryInfo()

}
