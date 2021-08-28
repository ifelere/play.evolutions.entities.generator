/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ifraytech.tools.ui;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import org.ifraytech.tools.services.Defaults;

/**
 * FXML Controller class
 *
 * @author ifelere
 */
public class SlickMappingDialogContentController implements Initializable {

    @FXML
    private TextField txtWrapperClassName, txtDestination, txtPackageName;
    
    @FXML
    private ChoiceBox<String> cboProfile;
    
    @FXML
    private TextArea txtCustomImports, txtExtends;
    
    private final ObjectProperty<SlickMappingOptions> options = new SimpleObjectProperty<>();

    public SlickMappingOptions getOptions() {
        return options.get();
    }

    public void setOptions(SlickMappingOptions value) {
        options.set(value);
    }

    public ObjectProperty<SlickMappingOptions> optionsProperty() {
        return options;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cboProfile.getItems().addAll("OracleProfile",
                "SQLServerProfile", "MySQLProfile", "PostgresProfile",
                "HsqldbProfile", "SQLiteProfile", "H2Profile", "DerbyProfile", "H2Profile");
        if (options.get() != null) {
            SlickMappingOptions sop = options.get();
            if (sop.getDestination() != null) {
                txtDestination.setText(sop.getDestination().getAbsolutePath());
            }
            txtPackageName.setText(sop.getPackageName());
            txtWrapperClassName.setText(sop.getWrappClassName());
            cboProfile.setValue(sop.getSlickProfile());
            txtCustomImports.setText(sop.getAdditionalImports());
            txtExtends.setText(sop.getExtensions());
           
        }
        options.addListener((b, o, opt) -> {
            if (opt != null) {
                if (opt.getDestination() != null) {
                    txtDestination.setText(opt.getDestination().getAbsolutePath());
                }
                txtPackageName.setText(opt.getPackageName());
                txtCustomImports.setText(opt.getAdditionalImports());
                txtExtends.setText(opt.getExtensions());
                txtWrapperClassName.setText(opt.getWrappClassName());
                cboProfile.setValue(opt.getSlickProfile());
            }
        });
    }    
    
    public SlickMappingOptions commit() {
        SlickMappingOptions sop = new SlickMappingOptions();
        sop.setDestination(new File(txtDestination.getText()));
        sop.setPackageName(txtPackageName.getText());
        sop.setWrappClassName(txtWrapperClassName.getText());
        sop.setSlickProfile(cboProfile.getValue());
        sop.setAdditionalImports(txtCustomImports.getText());
        sop.setExtensions(txtExtends.getText());
        
//        options.set(sop);
        return sop;
    }
   
    
    @FXML
    private void browseDestination(ActionEvent e) {
        Defaults d = Defaults.getInstance();
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select Destination");
        if(d.getLastOutputDirectory() != null) {
            chooser.setInitialDirectory(d.getLastOutputDirectory().getParentFile());
        }
        File dir = chooser.showDialog(null);
        if (dir != null) {
            txtDestination.setText(dir.getAbsolutePath());
        }
        
    }
    
}
