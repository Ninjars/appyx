package kotlin.com.bumble.appyx

import com.bumble.appyx.core.children.ChildEntry

object Appyx {

    var exceptionHandler: ((Exception) -> Unit)? = null
    var defaultChildKeepMode: ChildEntry.KeepMode = ChildEntry.KeepMode.KEEP

    fun reportException(exception: Exception) {
        val handler = exceptionHandler
        if (handler != null) {
            handler(exception)
        } else {
            throw exception
        }
    }

}
