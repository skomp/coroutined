package coroutines.scheduling

import coroutines.framework.FrameworkInterface
import coroutines.framework.FrameworkInterface.Request
import coroutines.framework.FrameworkInterface.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Unconfined
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.system.measureTimeMillis

object CoroutineLaunchNoDelay : FrameworkInterface {
    override suspend fun callFromFramework(r: Request): Response {
        CoroutineScope(Unconfined).launch {
            println("before suspend ${Thread.currentThread()}")
            SomeHardWork()
        }
        return Response
    }
}

object CoroutineLaunchDelay : FrameworkInterface {
    override suspend fun callFromFramework(r: Request): Response {
        CoroutineScope(Unconfined).launch {
            println("before suspend ${Thread.currentThread()}")
            delay(1)
            println("after suspend ${Thread.currentThread()}")
            SomeHardWork()
        }
        return Response
    }
}

fun main() {
    runBlocking {
        measureTimeMillis {
            CoroutineScope(EmptyCoroutineContext).async {
                println("framework call running in ${Thread.currentThread()}")
                CoroutineLaunchDelay.callFromFramework(Request)
                // CoroutineLaunchNoDelay.callFromFramework(Request)
            }.await()
            println("done")
        }.also {
            println(it)
        }
    }
}
