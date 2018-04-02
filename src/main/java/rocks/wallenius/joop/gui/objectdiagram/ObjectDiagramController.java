package rocks.wallenius.joop.gui.objectdiagram;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import rocks.wallenius.joop.gui.WindowController;
import rocks.wallenius.joop.gui.classdiagram.Constructor;
import rocks.wallenius.joop.gui.dialog.MethodDialog;
import rocks.wallenius.joop.gui.dialog.MethodParameters;
import rocks.wallenius.joop.gui.dialog.NewObject;
import rocks.wallenius.joop.gui.dialog.NewObjectDialog;
import rocks.wallenius.joop.gui.util.ClassStringFormatter;
import rocks.wallenius.joop.gui.util.ClassUmlMapperUtil;
import rocks.wallenius.joop.gui.util.ObjectUmlMapperUtil;
import rocks.wallenius.joop.model.entity.JoopObject;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by philipwallenius on 24/03/2018.
 */
public class ObjectDiagramController implements Initializable {

    private WindowController parentController;

    @FXML
    ScrollPane diagram;

    FlowPane pane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pane = new FlowPane();
        pane.setHgap(20);
        pane.setVgap(20);
        pane.setPadding(new Insets(20, 20, 20, 20));
        diagram.setContent(pane);
    }

    public void setParentController(WindowController parentController) {
        this.parentController = parentController;
    }

    public void clear() {
        pane.getChildren().clear();
    }

    public void addObjects(List<JoopObject> objects) {

        for(JoopObject object : objects){

            final UmlObject umlObject = new UmlObject(object.getObject().getClass().getSimpleName(), object.getReference(), ObjectUmlMapperUtil.getProperties(object.getObject()));
            ContextMenu contextMenu = createContextMenu(object.getObject());

            umlObject.setOnContextMenuRequested(event -> {
                contextMenu.hide();
                contextMenu.show(umlObject, event.getScreenX(), event.getScreenY());
            });

            pane.getChildren().add(umlObject);
        }
    }

    private ContextMenu createContextMenu(Object object) {
        ContextMenu contextMenu = new ContextMenu();
        List<MenuItem> menuItems = new ArrayList<>();

        for(Method method : ObjectUmlMapperUtil.getMethods(object)) {

            MenuItem item = new MenuItem(String.format("%s(%s)", method.getName(), ClassStringFormatter.formatParameters(method.getParameters())));
            item.setOnAction(event -> {
                invokeMethod(object, method);
            });
            menuItems.add(item);

        }

        contextMenu.getItems().addAll(menuItems);
        return contextMenu;
    }

    private void invokeMethod(Object object, Method method) {

        List<Class> params = Arrays.stream(method.getParameters()).map(rocks.wallenius.joop.gui.classdiagram.Parameter::getType).collect(Collectors.toList());


        MethodParameters methodParameters = null;
        if(params.size() > 0) {
            MethodDialog dialog = new MethodDialog(params.toArray(new Class[params.size()]));
            Optional<MethodParameters> result = dialog.showAndWait();
            if(result.isPresent()) {
                methodParameters = result.get();
            }
        }

        Class[] parameters;
        Object[] arguments;
        if(methodParameters != null) {
            int numberOfArguments = methodParameters.getArguments().size();
            parameters = methodParameters.getParameters().toArray(new Class[numberOfArguments]);
            arguments = methodParameters.getArguments().toArray(new Object[numberOfArguments]);
        } else {
            parameters = new Class[0];
            arguments = new Object[0];
        }

        Object returnValue = parentController.invokeMethod(object, method.getName(), parameters, arguments);

        if(returnValue != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, String.format("Return value: %s", returnValue.toString()));
            alert.showAndWait();
        }

    }

}
