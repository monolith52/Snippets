package completionfuture

import javafx.application.Application
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

class Main: Application() {
    override fun start(primaryStage: Stage?) {
        val root: Parent = FXMLLoader.load(javaClass.getResource("Main.fxml"))
        val scene = Scene(root, 300.0, 300.0)
        primaryStage?.scene = scene
        primaryStage?.show()
        primaryStage?.onCloseRequest = EventHandler{Platform.exit(); System.exit(0);}
    }
}

fun main(args: Array<String>) {
    Application.launch(Main::class.java, *args)
}