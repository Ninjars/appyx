package com.bumble.appyx.core.navigation.operationstrategies

import com.bumble.appyx.core.navigation.NavModel
import com.bumble.appyx.core.navigation.Operation
import kotlinx.coroutines.CoroutineScope

abstract class BaseOperationStrategy<NavTarget, State> : OperationStrategy<NavTarget, State> {

    protected lateinit var scope: CoroutineScope
    protected lateinit var navModel: NavModel<NavTarget, State>
    protected lateinit var executeOperation: (operation: Operation<NavTarget, State>) -> Unit

    override fun init(
        navModel: NavModel<NavTarget, State>,
        scope: CoroutineScope,
        executeOperation: (operation: Operation<NavTarget, State>) -> Unit
    ) {
        this.scope = scope
        this.navModel = navModel
        this.executeOperation = executeOperation
    }
}
