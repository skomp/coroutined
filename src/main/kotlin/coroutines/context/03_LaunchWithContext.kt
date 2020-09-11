package coroutines.context

import coroutines.framework.FrameworkInterface
import coroutines.framework.FrameworkInterface.Request
import coroutines.framework.FrameworkInterface.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asContextElement
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.coroutineContext
import kotlin.system.measureTimeMillis

object LaunchWithContextCoroutine : FrameworkInterface {
    val threadLocalVariable = ThreadLocalVariable()
    override suspend fun callFromFramework(r: Request): Response {
        CoroutineScope(coroutineContext + threadLocalVariable.actualVariable.asContextElement()).launch {
            println("1: ${Thread.currentThread()}")
            UseThreadLocal(threadLocalVariable)
        }
        CoroutineScope(coroutineContext + threadLocalVariable.actualVariable.asContextElement()).launch {
            println("2: ${Thread.currentThread()}")
            UseThreadLocal(threadLocalVariable)
        }
        return Response
    }
}

fun main() {
    runBlocking {
        measureTimeMillis {
            CoroutineScope(EmptyCoroutineContext).async {
                println("launcher: ${Thread.currentThread()}")
                LaunchWithContextCoroutine.callFromFramework(Request)
            }.await()
        }.also {
            println(it)
        }
    }
}
