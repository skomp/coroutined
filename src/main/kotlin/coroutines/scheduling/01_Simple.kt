package coroutines.scheduling

import coroutines.framework.FrameworkInterface
import coroutines.framework.FrameworkInterface.Request
import coroutines.framework.FrameworkInterface.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.system.measureTimeMillis

object SomeHardWork {
    // Just some hard work we can offload
    operator fun invoke() {
        //Thread.currentThread().threadGroup.list()
        println("hard work running in ${Thread.currentThread()}")
        Thread.sleep(3000)
        println("done with the hard work")
    }
}

object SimpleInterfaceImplementation : FrameworkInterface {
    override suspend fun callFromFramework(r: Request): Response {
        SomeHardWork()
        return Response
    }
}

fun main() {
    runBlocking {
        measureTimeMillis {
            CoroutineScope(EmptyCoroutineContext).async {
                println("framework call running in ${Thread.currentThread()}")
                SimpleInterfaceImplementation.callFromFramework(Request)
            }.await()
            println("done")
        }.also {
            println(it)
        }
    }
}
