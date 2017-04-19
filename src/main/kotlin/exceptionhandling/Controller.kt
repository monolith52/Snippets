package exceptionhandling

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.Button
import java.io.File
import java.io.FileInputStream
import java.util.concurrent.CancellationException
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors
import java.util.function.Consumer
import java.util.function.Supplier
import java.util.function.Function

class Controller {

    val threadPool = Executors.newWorkStealingPool()
    var future: CompletableFuture<Void>? = null
    var futureThread: Thread? = null
    @FXML lateinit var button: Button

    @FXML fun onButtonAction(@Suppress("UNUSED_PARAMETER") event: ActionEvent) {
        if (button.text == "cancel") {
//            future?.cancel(true)
            futureThread?.interrupt()
            button.text = "start"
            return
        }
        button.text = "cancel"
        threadPool.execute {
            futureThread = Thread.currentThread()
            future = CompletableFuture.supplyAsync(Supplier { heavyRoutine() }, threadPool)
                    .thenApply { result ->
                        "[applied] " + result
                    } .thenAcceptAsync(Consumer { result ->
//                        throw CancellationException("intentional cancellation")
                        button.text = result
                    }, JavaFxExecutor)
//                    .handle {v: Void, e:Throwable ->
//
//                        return@handle Unit
//                    }
                    .exceptionally { e ->
                        println("exceptionally ${e}")
                        return@exceptionally null
                    }
//            try {
                println("future get: ${future?.get()}")
//            } catch (e: ExecutionException) {
//                println("caught ${e}")
//            }
            println("after future get")
            future?.isCompletedExceptionally ?: println("completedExceptionally")
        }
    }

    fun heavyRoutine(): String {
        println("heavyRoutine start")
        Thread.sleep(3000)
        println("heavyRoutine end")
        return "heavyRoutine on ${Thread.currentThread().name}"
    }

    fun ioException(): String {
        println("ioException start")
        Thread.sleep(3000)
        println("ioException end")
        FileInputStream(File("none.xml")).use {}
        println("FileInputStream open end")
        return "ioException on ${Thread.currentThread().name}"
    }

}