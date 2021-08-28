/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ifraytech.tools;

import java.io.IOException;
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.ifraytech.tools.services.Defaults;
import org.ifraytech.tools.ui.EntityCollectionViewController;

/**
 * Main JavaFX application
 * @author ifelere
 */
public class PlayEvolutionsEntitiesGenerator extends Application {
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        URL url = EntityCollectionViewController.class.getResource(
                        String.format("%s.fxml",
                                EntityCollectionViewController.class.getSimpleName().replaceFirst("Controller$", "")));
        FXMLLoader loader = new FXMLLoader(url);
        loader.load();
        Scene scene = new Scene(loader.getRoot(), 1240, 800);        
        primaryStage.setTitle("Play Evolutions Entities Generator");
        primaryStage.setScene(scene);
        primaryStage.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, (e) -> Defaults.getInstance().save());
        primaryStage.addEventFilter(WindowEvent.WINDOW_HIDDEN, (e) -> Defaults.getInstance().save());
        primaryStage.show();
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
