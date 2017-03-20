package completionfuture

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.Button
import java.awt.Label
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.function.Consumer
import java.util.function.Supplier

class Controller {

    val threadPool = Executors.newCachedThreadPool()
    @FXML lateinit var button: Button

    @FXML fun onButtonAction(event: ActionEvent) {
        button.text = "start"
        threadPool.execute {
            val future2 = CompletableFuture.supplyAsync( heavySupplier, threadPool)
                    .thenAcceptAsync(Consumer { result ->
                        button.text = result
                    }, JavaFxExecutor)
        }
    }

    fun heavyRoutine(): String {
        println("heavyRoutine start")
        Thread.sleep(3000)
        println("heavyRoutine end")
        return "heavyRoutine on ${Thread.currentThread().name}"
    }

    var heavySupplier: Supplier<String> = Supplier sup@ {
        println("heavySupplier start")
        Thread.sleep(3000)
        println("heavySupplier end")
        return@sup "heavySupplier on ${Thread.currentThread().name}"
    }


}