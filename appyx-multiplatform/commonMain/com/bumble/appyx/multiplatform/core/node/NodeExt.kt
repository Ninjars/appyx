package com.bumble.appyx.multiplatform.core.node

fun <T : Node> T.build(): T = also { it.onBuilt() }
