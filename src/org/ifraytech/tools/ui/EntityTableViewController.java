/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ifraytech.tools.ui;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.converter.DefaultStringConverter;
import org.ifraytech.tools.Attribute;
import org.ifraytech.tools.Entity;
import org.ifraytech.tools.TypeMapping;

/**
 * FXML Controller class
 *
 * @author ifelere
 */
public class EntityTableViewController implements Initializable {
    static final String CUSTOM_VALUE_TYPE = "Custom";
    
    private final ObjectProperty<Entity> entity = new SimpleObjectProperty<>();

    public Entity getEntity() {
        return entity.get();
    }

    public void setEntity(Entity value) {
        entity.set(value);
    }

    public ObjectProperty entityProperty() {
        return entity;
    }
    

    @FXML
    private TableView<HigherAttribute> tableAttributes;
    
    @FXML
    private TextField txtEntityName;
    
    
    @FXML
    private TableColumn<HigherAttribute, String> colName, colUserType, colType, colDefault;
    
    @FXML
    private TableColumn<HigherAttribute, Boolean> colOptional, colKey;
    
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       colName.setCellFactory((TableColumn<HigherAttribute, String> param) -> new TextFieldTableCell<>(new DefaultStringConverter()));
       
       colName.setCellValueFactory((TableColumn.CellDataFeatures<HigherAttribute, String> param) -> param.getValue().nameProperty());
       
       colDefault.setCellValueFactory((param) -> param.getValue().defaultValueProperty());
       
       colDefault.setCellFactory((param) -> new TextFieldTableCell<>(new DefaultStringConverter()));
       
       colOptional.setCellFactory((TableColumn<HigherAttribute, Boolean> param) -> new CheckBoxTableCell<>());
       
       colOptional.setCellValueFactory((param) -> param.getValue().optionalProperty() );
       
       
//       colKey.setCellFactory((p) -> new TextFieldTableCell<Attribute, Boolean>(new StringConverter<Boolean>() {
//           @Override
//           public String toString(Boolean object) {
//               if (object == null) {
//                   return "";
//               }
//               return object ? "Primary key" : "";
//           }
//
//           @Override
//           public Boolean fromString(String string) {
//               return "Primary key".equals(string);
//           }
//       }));

       colKey.setCellFactory((p) -> new KeyCell());
       
       colKey.setCellValueFactory((p) -> p.getValue().primaryKeyProperty());
       
       
       colType.setCellValueFactory((param) -> param.getValue().programTypeProperty());
       
       colType.setCellFactory((e) -> createTypeSelector());
       
       colUserType.setCellFactory((a) -> new TextFieldTableCell<>(new DefaultStringConverter()));
       
       colUserType.setCellValueFactory((x) -> x.getValue().userValueTypeProperty());
      
       entityProperty().addListener(new ChangeListener<Entity>() {
           @Override
           public void changed(ObservableValue<? extends Entity> observable, Entity oldValue, Entity newValue) {
               
               if (newValue == null) {
                   txtEntityName.textProperty().set("");
                   
                   tableAttributes.getItems().clear();
               } else {
                   
                   txtEntityName.setText(newValue.getProgramName());
                   
                   final ObservableList<HigherAttribute> list = FXCollections.observableArrayList();
                   
                   newValue.getAttributes().forEach((attr) -> {
                      list.add(HigherAttribute.create(attr));
                   });
                   
                   tableAttributes.itemsProperty().setValue(list);
               }
           }           
       });
       
       
       txtEntityName.textProperty().addListener((b, o, newValue) -> {
           if (getEntity() != null) {
               getEntity().setProgramName(newValue);
           }
       });
    }  
    
    public void open(Entity entity) {
        this.entityProperty().set(entity);
    }
    
    private static final Image keyIcon = new Image(Attribute.class.getResourceAsStream("resources/key-icon.png"), 24, 24, true, true);
    
    
    private static class KeyCell extends TableCell<HigherAttribute, Boolean> {
        
        public KeyCell() {
            
        }

        @Override
        protected void updateItem(Boolean item, boolean empty) {
            super.updateItem(item, empty);
            setText("");
            if (item != null && true == item) {
                setGraphic(new ImageView(keyIcon));
            } else {
                setGraphic(null);
            }
        }
        
        
        
    }
    
    
    public void persistAttributeChanges() {
        tableAttributes.getItems().forEach((ha) -> {
            this.getEntity().getAttributes()
                    .stream()
                    .filter((x) -> x.getName().equals(ha.getName()))
                    .findFirst()
                    .ifPresent((attribute) -> {
                        ha.writeTo(attribute);
                    });
        });
    }
    
    
    
    private static TableCell<HigherAttribute, String> createTypeSelector() {
        
        Stream<String> types = Stream.concat(TypeMapping.getInstance()
                .getInstances()
                .stream()
                .map((t) -> t.getScalaType()), Stream.of(CUSTOM_VALUE_TYPE));
        
        
        return new ChoiceBoxTableCell<>(
                FXCollections.observableArrayList(types.collect(Collectors.toList())));
        
        
    }
    
    
    static class HigherAttribute extends Attribute {
        
        private final StringProperty userValueType = new SimpleStringProperty();

        public String getUserValueType() {
            return userValueType.get();
        }

        public void setUserValueType(String value) {
            userValueType.set(value);
        }

        public StringProperty userValueTypeProperty() {
            return userValueType;
        }

        public HigherAttribute() {
        }
        
        public static final HigherAttribute create(Attribute attribute) {
            HigherAttribute ha = new HigherAttribute();
            ha.autoIncrementsProperty().set(attribute.isAutoIncrements());
            ha.setDefaultValue(attribute.getDefaultValue());
            ha.setName(attribute.getName());
            ha.setPrimaryKey(attribute.isPrimaryKey());
            ha.setOptional(attribute.isOptional());
            if (!TypeMapping.getInstance().hasType(attribute.getProgramType())) {
                ha.setProgramType(CUSTOM_VALUE_TYPE);
                ha.setUserValueType(attribute.getProgramType());
            } else {
                ha.setProgramType(attribute.getProgramType());
            }
            return ha;
        }
        
        
        public void writeTo(Attribute attribute) {
            attribute.setAutoIncrements(this.isAutoIncrements());
            attribute.setOptional(this.isOptional());
            attribute.setPrimaryKey(this.isPrimaryKey());
            attribute.setDefaultValue(this.getDefaultValue());
            if (CUSTOM_VALUE_TYPE.equals(this.getProgramType())) {
                attribute.setProgramType(this.getUserValueType());
            } else {
                attribute.setProgramType(this.getProgramType());
            }
        }
        
        
        
    }
    
}
