package coroutines.scheduling

import coroutines.framework.FrameworkInterface
import coroutines.framework.FrameworkInterface.Request
import coroutines.framework.FrameworkInterface.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.coroutineContext
import kotlin.system.measureTimeMillis

object CoroutineLaunchInterfaceImplementation : FrameworkInterface {
    override suspend fun callFromFramework(r: Request): Response {
        CoroutineScope(coroutineContext).launch { SomeHardWork() }
        return Response
    }
}

fun main() {
    runBlocking {
        measureTimeMillis {
            CoroutineScope(EmptyCoroutineContext).async {
                println("framework call running in ${Thread.currentThread()}")
                CoroutineLaunchInterfaceImplementation.callFromFramework(Request)
            }.await()
            println("done")
        }.also {
            println(it)
        }
    }
}
