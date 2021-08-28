/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ifraytech.tools.ui;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.Pane;

/**
 *  A dialog class to present slick mapping generation options
 * @author ifelere
 */
public class ExportSlickMappingDialog extends Dialog<SlickMappingOptions>{

    private SlickMappingOptions initialOptions;

    private SlickMappingDialogContentController contentController;
    
    
    public ExportSlickMappingDialog() {
        initComponents();
    }
    
    
    private void initComponents() {
        setTitle("Slick Mapping Options");
        
        setResizable(true);
        
        getDialogPane().getButtonTypes().setAll(
                ButtonType.CANCEL,
                ButtonType.OK
        );
        
        FXMLLoader loader = new FXMLLoader(SlickMappingDialogContentController.class.getResource(
                String.format("%s.fxml", SlickMappingDialogContentController.class.getSimpleName().replace("Controller", ""))));
        try {
            Pane ct = loader.load();
            getDialogPane().setContent(ct);
            contentController = loader.getController();
            
            setResultConverter((ButtonType param) -> {
                if (ButtonType.OK.equals(param)) {
                    return contentController.commit();
                }
                return null;
            });
            if (initialOptions != null) {
                contentController.setOptions(initialOptions);
            }
        } catch (IOException ex) {
            Logger.getLogger(ExportSlickMappingDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }
    /**
     * Gets t initial options
     * @return  the initial options
     */
    public SlickMappingOptions getInitialOptions() {
        return initialOptions;
    }

    /**
     * Sets initial options
     * @param initialOptions Initial options for the dialog 
     */
    public void setInitialOptions(SlickMappingOptions initialOptions) {
        this.initialOptions = initialOptions;
        if (contentController != null) {
            contentController.optionsProperty().set(initialOptions);
        }
    }
    
}
