package com.bumble.appyx.multiplatform.core.navigation.operationstrategies

import com.bumble.appyx.multiplatform.core.navigation.NavModel
import com.bumble.appyx.multiplatform.core.navigation.Operation
import kotlinx.coroutines.CoroutineScope

interface OperationStrategy<NavTarget, State> {

    fun init(
        navModel: NavModel<NavTarget, State>,
        scope: CoroutineScope,
        executeOperation: (operation: Operation<NavTarget, State>) -> Unit
    )

    fun accept(operation: Operation<NavTarget, State>)
}
