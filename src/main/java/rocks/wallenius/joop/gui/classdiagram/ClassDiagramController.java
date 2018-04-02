package rocks.wallenius.joop.gui.classdiagram;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import rocks.wallenius.joop.gui.dialog.NewObject;
import rocks.wallenius.joop.gui.dialog.NewObjectDialog;
import rocks.wallenius.joop.gui.util.ClassUmlMapperUtil;
import rocks.wallenius.joop.gui.WindowController;
import rocks.wallenius.joop.gui.util.ClassStringFormatter;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by philipwallenius on 24/03/2018.
 */
public class ClassDiagramController implements Initializable {

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

    public void clear() {
        pane.getChildren().clear();
    }

    public void addClasses(List<Class> classes) {

        for(Class clazz : classes) {
            final UmlClass umlClass = new UmlClass(clazz.getSimpleName(), ClassUmlMapperUtil.getFields(clazz), ClassUmlMapperUtil.getConstructors(clazz), ClassUmlMapperUtil.getMethods(clazz));
            ContextMenu contextMenu = createContextMenu(clazz);

            umlClass.setOnContextMenuRequested(event -> {
                contextMenu.hide();
                contextMenu.show(umlClass, event.getScreenX(), event.getScreenY());
            });

            pane.getChildren().add(umlClass);
        }

    }

    public void setParentController(WindowController parentController) {
        this.parentController = parentController;
    }

    private ContextMenu createContextMenu(Class clazz) {
        ContextMenu contextMenu = new ContextMenu();
        List<MenuItem> menuItems = new ArrayList<>();

        for(Constructor constructor : ClassUmlMapperUtil.getConstructors(clazz)) {

            MenuItem item = new MenuItem(String.format("%s(%s)", constructor.getName(), ClassStringFormatter.formatParameters(constructor.getParameters())));
            item.setOnAction(event -> {
                invokeConstructor(clazz, constructor);
            });
            menuItems.add(item);

        }

        contextMenu.getItems().addAll(menuItems);
        return contextMenu;
    }

    private void invokeConstructor(Class clazz, Constructor constructor) {

        List<Class> params = Arrays.stream(constructor.getParameters()).map(rocks.wallenius.joop.gui.classdiagram.Parameter::getType).collect(Collectors.toList());

        NewObjectDialog dialog = new NewObjectDialog(clazz.getName(), params.toArray(new Class[params.size()]));

        Optional<NewObject> result = dialog.showAndWait();

        if(result.isPresent()) {
            NewObject newObject = result.get();
            int numberOfArguments = newObject.getArguments().size();
            parentController.invokeConstructor(clazz, newObject.getInstanceName(), newObject.getParameters().toArray(new Class[numberOfArguments]), newObject.getArguments().toArray(new Object[numberOfArguments]));
        }

    }

}
