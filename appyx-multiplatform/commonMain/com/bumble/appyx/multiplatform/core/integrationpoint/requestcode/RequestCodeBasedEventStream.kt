package com.bumble.appyx.multiplatform.core.integrationpoint.requestcode

import kotlinx.coroutines.flow.Flow

interface RequestCodeBasedEventStream<T : RequestCodeBasedEventStream.RequestCodeBasedEvent> {

    fun events(client: RequestCodeClient): Flow<T>

    interface RequestCodeBasedEvent {
        val requestCode: Int
    }
}
