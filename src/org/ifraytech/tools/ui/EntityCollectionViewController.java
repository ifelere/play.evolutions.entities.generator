/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ifraytech.tools.ui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import org.apache.commons.lang3.StringUtils;
import org.ifraytech.tools.Entity;
import org.ifraytech.tools.EntityCollection;
import org.ifraytech.tools.EntityWriter3;
import org.ifraytech.tools.SQLParseException;
import org.ifraytech.tools.ScalaSlickMappingGenerator;
import org.ifraytech.tools.ScalaVelocityEntityWriter;
import org.ifraytech.tools.SlickMappingGenerator;
import org.ifraytech.tools.services.Defaults;
import org.ifraytech.tools.services.GenerateFilesService;
import org.ifraytech.tools.services.SlickMappingGeneratorService;

/**
 * FXML Controller class
 *
 * @author ifelere
 */
public class EntityCollectionViewController implements Initializable {
    public EntityCollection entityCollection = new EntityCollection();
    
    private static final Logger LOGGER = Logger.getLogger(EntityCollectionViewController.class.getName());
    

    private final ObjectProperty<File> outputDirectory = new SimpleObjectProperty<>();
    private final ReadOnlyBooleanWrapper busy = new ReadOnlyBooleanWrapper();

    public boolean isBusy() {
        return busy.get();
    }

    public ReadOnlyBooleanProperty busyProperty() {
        return busy.getReadOnlyProperty();
    }

    public File getOutputDirectory() {
        return outputDirectory.get();
    }

    public void setOutputDirectory(File value) {
        outputDirectory.set(value);
    }

    public ObjectProperty outputDirectoryProperty() {
        return outputDirectory;
    }
    
    
    
    @FXML
    private EntityTableViewController entityDetailsController;
    
    
    @FXML
    private TextField txtOutputDirectory, txtNamespace;
    
    @FXML
    private TextArea txtOutput;
    
    @FXML
    private CheckBox chkOverwrite;
    
    @FXML
    private ProgressBar progressBar;
    
    
    @FXML
    private ListView<Entity> lstEntities;
    
    @FXML
    private Pane rootControl;
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lstEntities.setCellFactory((ListView<Entity> param) -> new TextFieldListCell<>(entityStringConverter));
        
        entityDetailsController.entityProperty().bind(lstEntities.getSelectionModel().selectedItemProperty());
        
        lstEntities.setItems(entityCollection.getEntities());
        
        final Formatter logFormatter = new SimpleFormatter();
        
        Defaults d = Defaults.getInstance();
        chkOverwrite.setSelected(d.isOverwrite());
        if (d.getLastOutputDirectory() != null) {
            txtOutputDirectory.setText(d.getLastOutputDirectory().getAbsolutePath());
        }
        txtNamespace.setText(d.getLastNameSpace());
        
        txtNamespace.focusedProperty().addListener((b, o, focused) -> {
            if (!focused) {
                Defaults.getInstance().setLastNameSpace(txtNamespace.getText());
            }
        });
        chkOverwrite.selectedProperty().addListener((c, o, selected) -> {
            Defaults.getInstance().setOverwrite(selected);
        });
        
       LOGGER.addHandler(new Handler() {
            @Override
            public void publish(LogRecord record) {
                txtOutput.appendText(logFormatter.format(record) + System.lineSeparator());
            }

            @Override
            public void flush() {
                
            }

            @Override
            public void close() throws SecurityException {
                
            }
       });
       
       rootControl.lookupAll("Button").forEach((Node n) -> {
           ((Button)n).disableProperty().bind(busy);
       });
        
    }    
    
    @FXML
    private void doGenerateSlickMapping(ActionEvent e) {
        if (isBusy()) {
            return;
        }
        if (this.entityCollection.getEntities().isEmpty()) {
            return;
        }
        this.entityDetailsController.persistAttributeChanges();
        SlickMappingOptions opts = this.getExportOptions();
        if (opts == null) {
            return;
        }
        try {
            Defaults.getInstance().setLastMappingOptions(opts);
            this.validate();
            progressBar.progressProperty().unbind();
            SlickMappingGenerator gen = new ScalaSlickMappingGenerator();
            gen.setEntityPackageName(txtNamespace.getText());
            SlickMappingGeneratorService service = new SlickMappingGeneratorService(
            this.entityCollection, opts, gen);
            this.bindServiceProgress(service);
            txtOutput.setText(null);
            service.start();
        }catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        
    }
    
    private SlickMappingOptions getExportOptions() {
        ExportSlickMappingDialog dlg = new ExportSlickMappingDialog();
        dlg.setInitialOptions(Defaults.getInstance().getLastMappingOptions());
        return dlg.showAndWait()
                .orElse(null);
    }
    public final StringConverter<Entity> entityStringConverter = new StringConverter<Entity>() {
        @Override
        public String toString(Entity object) {
            return object.getProgramName();
        }

        @Override
        public Entity fromString(String string) {
            
            return entityCollection.getEntities()
                    .stream()
                    .filter((e) -> e.getProgramName().equals(string))
                    .findAny()
                    .orElse(null);
        }
    };
    
    @FXML
    private void addEntity(ActionEvent e) {
        if (isBusy()) {
            return;
        }
        FileChooser chooser = new FileChooser();
        Defaults d = Defaults.getInstance();
        if (d.getLastSourceDirectory() != null) {
            chooser.setInitialDirectory(d.getLastSourceDirectory());
        }
        chooser.setTitle("Select one or more evolution scripts");
        chooser.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("SQL File", "*.sql"));
        List<File> files = chooser.showOpenMultipleDialog(lstEntities.getScene().getWindow());
        if(files == null || files.isEmpty()) {
            return;
        }
        d.setLastSourceDirectory(files.get(0).getParentFile());
        files.forEach((file) -> {
            try {
                List<Entity> list = entityCollection.addSQLFile(file);
                list.forEach((et) -> {
                    LOGGER.log(Level.FINE, "Found {0}", et.getProgramName());
                });
            } catch (SQLParseException | IOException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
    }
    
    @FXML
    private void browseForOutput(ActionEvent e) {
        if (isBusy()) {
            return;
        }
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select output directory");
        Defaults d = Defaults.getInstance();
        if (d.getLastOutputDirectory() != null) {
            chooser.setInitialDirectory(d.getLastOutputDirectory().getParentFile());
        }
        File file = chooser.showDialog(lstEntities.getScene().getWindow());
        if (file != null) {
            d.setLastOutputDirectory(file);
            txtOutputDirectory.setText(file.getAbsolutePath());
        }
    }
    
    
    @FXML
    private void removeSelectedEntity(ActionEvent e) {
        if (isBusy()) {
            return;
        }
        if (!lstEntities.getSelectionModel().isEmpty()) {
            entityCollection.getEntities().removeAll(lstEntities.getSelectionModel().getSelectedItems());
        }
    }
    
    @FXML
    private void doGenerate(ActionEvent e) {
        if (isBusy()) {
            return;
        }
        try {
            this.entityDetailsController.persistAttributeChanges();
            this.validate();
            this.generateImpl();
        }catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
    
    private void bindServiceProgress(Service<?> service) {
        service.messageProperty().addListener((b, o, m) -> {
            txtOutput.appendText(m + System.lineSeparator());
        });
        
        busy.unbind();
        
        busy.bind(service.runningProperty());
        
        progressBar.progressProperty().unbind();
        
        progressBar.progressProperty().bind(service.progressProperty());
        
        service.setOnFailed((e) -> {
            LOGGER.log(Level.SEVERE, null, e.getSource().getException());
            progressBar.progressProperty().unbind();
            progressBar.progressProperty().set(0);
        });
    }
    private void generateImpl() {
        EntityWriter3 writer = new ScalaVelocityEntityWriter();
        writer.setPackageName(txtNamespace.getText());
        GenerateFilesService service = new GenerateFilesService(entityCollection.getEntities(),
                new File(txtOutputDirectory.getText()), chkOverwrite.isSelected(), writer);
        
        txtOutput.setText(null);
        
        Defaults d = Defaults.getInstance();
        
        d.setOverwrite(chkOverwrite.isSelected());
        
        d.setLastNameSpace(txtNamespace.getText());
        
        this.bindServiceProgress(service);
        
        
        service.start();
        
    }
    
    private void validate() {
        if (StringUtils.isEmpty(txtNamespace.getText())) {
            throw new IllegalStateException("You have not provided a namespace");
        }
        if (StringUtils.isEmpty(txtOutputDirectory.getText())) {
            throw new IllegalStateException("You have not provided output directory");
        }
        
        File f = new File(txtOutputDirectory.getText());
        if (!f.exists()) {
            throw new IllegalStateException(String.format("Output directory, '%s', not found!", f));
        }
        if (entityCollection.getEntities().isEmpty()) {
            throw new IllegalStateException("No entity to generate");
        }
    }
    
    
}
