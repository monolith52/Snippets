package completionfuture

import javafx.application.Platform
import java.util.concurrent.Executor

object JavaFxExecutor : Executor {
    override fun execute(runnable: Runnable?) {
        runnable ?: return
        Platform.runLater(runnable)
    }

}