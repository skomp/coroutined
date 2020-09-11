package coroutines.scheduling

import coroutines.framework.FrameworkInterface
import coroutines.framework.FrameworkInterface.Request
import coroutines.framework.FrameworkInterface.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.system.measureTimeMillis

object CoroutineLaunchInOwnContextInterfaceImplementation : FrameworkInterface {
    override suspend fun callFromFramework(r: Request): Response {
        CoroutineScope(EmptyCoroutineContext).launch {
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
                CoroutineLaunchInOwnContextInterfaceImplementation.callFromFramework(Request)
            }.await()
            println("done")
        }.also {
            println(it)
        }
        delay(5000)
    }
}
