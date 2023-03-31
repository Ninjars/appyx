package com.bumble.appyx.multiplatform.core.navigation.operationstrategies

import com.bumble.appyx.multiplatform.core.navigation.Operation

class ExecuteImmediately<NavTarget, State> : BaseOperationStrategy<NavTarget, State>() {

    override fun accept(operation: Operation<NavTarget, State>) {
        executeOperation(operation)
    }
}
