package coroutines.context

import coroutines.framework.FrameworkInterface
import coroutines.framework.FrameworkInterface.Request
import coroutines.framework.FrameworkInterface.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.lang.Thread.sleep
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.system.measureTimeMillis

class ThreadLocalVariable {
    val actualVariable = ThreadLocal<ThreadLocalVariable>().apply {
        set(this@ThreadLocalVariable)
        println("setting variable in thread ${Thread.currentThread()}")
    }
    fun get() = actualVariable.get().also { println("getting local variable in thread ${Thread.currentThread()}") }

    override fun toString(): String {
        return "we have variable"
    }
}

object UseThreadLocal {
    // Just some hard work we can offload
    operator fun invoke(threadLocalVariable: ThreadLocalVariable) = println(
        threadLocalVariable.get()
    ).also { sleep(1000) }
}

class SimpleInterfaceImplementation: FrameworkInterface {
    val threadLocalVariable = ThreadLocalVariable()
    override suspend fun callFromFramework(r: Request): Response {
        UseThreadLocal(threadLocalVariable)
        return Response
    }
}

fun main() {
    runBlocking {
        measureTimeMillis {
            CoroutineScope(EmptyCoroutineContext).async {
                SimpleInterfaceImplementation().callFromFramework(Request)
            }.await()
        }.also {
            println(it)
        }
    }
}
