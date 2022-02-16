package org.didoszak.audiocutter;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Controller {
    private SimpleStringProperty filePath;
    private SimpleBooleanProperty saveFile;
    private boolean firstFile = true;

    public SimpleStringProperty getFilePath() {
        if(filePath == null)
            filePath = new SimpleStringProperty();
        return filePath;
    }

    public SimpleBooleanProperty getSaveFile() {
        if(saveFile == null)
            saveFile = new SimpleBooleanProperty();
        return saveFile;
    }

    @FXML
    private Label infoLabel;

    @FXML
    private VBox audioContainer;

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
//            case ".mp3":
//            case ".flac":
//            case ".ogg":
//            case ".opus":
            case ".mp4":
                if(firstFile) {
                    audioContainer.getChildren().remove(infoLabel);
                    firstFile = false;
                }
                filePath.set(path.toString());
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

    public void saveFile(MouseEvent mouseEvent) {
        saveFile.set(!saveFile.get());
    }











}
