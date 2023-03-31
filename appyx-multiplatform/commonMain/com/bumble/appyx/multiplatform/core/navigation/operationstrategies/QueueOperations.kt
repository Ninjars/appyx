package com.bumble.appyx.multiplatform.core.navigation.operationstrategies

import com.bumble.appyx.core.navigation.NavElements
import com.bumble.appyx.core.navigation.Operation
import com.bumble.appyx.core.navigation.isTransitioning
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class QueueOperations<NavTarget, State> : BaseOperationStrategy<NavTarget, State>() {

    private val operationQueue = LinkedList<Operation<NavTarget, State>>()
    private lateinit var collectJob: Job

    override fun accept(operation: Operation<NavTarget, State>) {
        collectElementsIfRequired()

        if (hasUnfinishedOperation()) {
            operationQueue.addFirst(operation)
        } else {
            executeOperation(operation)
        }
    }

    private fun collectElementsIfRequired() {
        if (::collectJob.isInitialized.not()) {
            collectJob = scope.launch {
                navModel
                    .elements
                    .collect(::addToQueueIfTransitionInProgress)
            }
        }
    }

    private fun addToQueueIfTransitionInProgress(transitionList: NavElements<NavTarget, out State>) {
        if (transitionList.none { it.isTransitioning } && operationQueue.isNotEmpty()) {
            val operation = operationQueue.removeLast()
            executeOperation(operation)
        }
    }

    private fun hasUnfinishedOperation(): Boolean {
        return navModel.elements.value.any { it.targetState != it.fromState }
    }
}
