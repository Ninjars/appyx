package com.bumble.appyx.multiplatform.core.navigation.operationstrategies

import com.bumble.appyx.multiplatform.core.navigation.Operation
import com.bumble.appyx.multiplatform.core.navigation.isTransitioning


class FinishTransitionsOnNewOperation<NavTarget, State> :
    BaseOperationStrategy<NavTarget, State>() {

    override fun accept(operation: Operation<NavTarget, State>) {
        finishUnfinishedTransitions()
        executeOperation(operation)
    }

    private fun finishUnfinishedTransitions() {
        navModel
            .elements
            .value
            .mapNotNull { navElement ->
                if (navElement.isTransitioning) navElement.key else null
            }
            .also { if (it.isNotEmpty()) navModel.onTransitionFinished(it) }
    }
}
