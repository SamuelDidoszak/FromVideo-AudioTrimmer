import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Controller {
    @FXML
    Label infoLabel;

    @FXML
    StackPane stackPane;

    @FXML
    private void handleDragOver(DragEvent event) {
        if(event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.ANY);
        }
    }

    @FXML
    public void handleDragDrop(DragEvent event) {
        File file = event.getDragboard().getFiles().get(0);
        Path path = Paths.get(file.getPath());

        String fileExtension = path.toString().substring(path.toString().lastIndexOf("."));
        switch(fileExtension) {
            case ".wav":
            case ".mp3":
            case ".flac":
            case ".ogg":
            case ".opus":
                stackPane.getChildren().remove(infoLabel);
                break;
            default:
                infoLabel.setText("Unsupported file");

                Runnable runnable = () -> {
                    try {
                        Thread.sleep(2000);
                        Platform.runLater(() -> infoLabel.setText("Drag And Drop"));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                };
                new Thread(runnable).start();
        }

    }
}
