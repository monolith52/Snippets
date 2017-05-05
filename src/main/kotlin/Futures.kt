import javafx.application.Platform
import java.util.concurrent.*
import java.util.function.Consumer
import java.util.function.Supplier

fun main(args: Array<String>) {
    Executors.newSingleThreadExecutor().execute({

        HeavyRoutine().processAsync().get()
        println("After HeavyRoutine on ${Thread.currentThread().name}")

        HeavyRoutine().processAsync(Consumer {
            println("Callbak HeavyRoutine on ${Thread.currentThread().name}")
        })
        println("After HeavyRoutine on ${Thread.currentThread().name}")

        HeavyRoutine().processCompletable().get()
        println("After HeavyRoutine on ${Thread.currentThread().name}")

        HeavyRoutine().processCompletable().
                thenAccept {
             println("Callbak HeavyRoutine on ${Thread.currentThread().name}")
        }
        println("After HeavyRoutine on ${Thread.currentThread().name}")

        Thread.sleep(10000L)
        System.exit(0)

    })
    println("After NewThread on ${Thread.currentThread().name}")
}

class HeavyRoutine {
    fun process(callback: Consumer<String>?): String {
        Thread.sleep(5000L)
        val result = "** RESULT **"
        println("process complete on ${Thread.currentThread().name}")
        callback?.accept(result)
        return result
    }

    fun processAsync(): Future<String> {
        return processAsync(null)
    }

    fun processAsync(callback: Consumer<String>?): Future<String> {
        val future = FutureTask<String>({process(callback)})
        Executors.newSingleThreadExecutor().execute(future)
        return future
    }

    fun processCompletable(): CompletableFuture<String> {
        val exec = Executors.newSingleThreadExecutor()
        val future = CompletableFuture.supplyAsync(Supplier { process(null) }, exec)
        return future
    }
}