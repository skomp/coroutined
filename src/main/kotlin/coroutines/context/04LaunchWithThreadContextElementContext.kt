package coroutines.context

import coroutines.framework.FrameworkInterface
import coroutines.framework.FrameworkInterface.Request
import coroutines.framework.FrameworkInterface.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ThreadContextElement
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext
import kotlin.system.measureTimeMillis

class RuntimeMontoring(val id: Long) :
    ThreadContextElement<Long>,
    AbstractCoroutineContextElement(RuntimeMontoring) {
    var totalRuntime = 0L

    companion object Key : CoroutineContext.Key<RuntimeMontoring>

    override fun restoreThreadContext(context: CoroutineContext, oldState: Long) {
        totalRuntime = System.currentTimeMillis() - oldState
        println("$id: deactivated, ran for total of $totalRuntime")
    }

    override fun updateThreadContext(context: CoroutineContext): Long {
        println("$id: activate, ran for total of $totalRuntime")
        return System.currentTimeMillis()
    }
}

object LaunchWithMonitoring : FrameworkInterface {

    override suspend fun callFromFramework(r: Request): Response {
        val runtimeMontoring = RuntimeMontoring(1)
        CoroutineScope(runtimeMontoring + Dispatchers.Unconfined).launch {
            delay(10)
            Thread.sleep(10)
            delay(10)
            println("total runtime of 1: ${coroutineContext[RuntimeMontoring]!!.totalRuntime}")
        }

        CoroutineScope(RuntimeMontoring(2) + Dispatchers.Unconfined).launch {
            delay(10)
            Thread.sleep(10)
            delay(10)
            println("total runtime of 2: ${coroutineContext[RuntimeMontoring]!!.totalRuntime}")
        }
        return Response
    }
}

fun main() {
    runBlocking {
        measureTimeMillis {
            val runtimeMontoring = RuntimeMontoring(0)
            val async = CoroutineScope(runtimeMontoring).async {
                println("launcher: ${Thread.currentThread()}")
                LaunchWithMonitoring.callFromFramework(Request)
            }
            async.await()
            println("runtime of async: ${runtimeMontoring.totalRuntime}")
        }.also {
            //println(it)
        }
        delay(2000)
    }
}
