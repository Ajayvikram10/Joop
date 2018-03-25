package rocks.wallenius.joop.gui.dialog;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.*;

/**
 * Created by philipwallenius on 25/03/2018.
 */
public class NewObjectDialog extends Dialog<NewObject> {

    private String className;
    private Class[] parameters;
    private List<TextField> inputs;

    public NewObjectDialog(String className, Class[] parameters) {
        super();

        this.className = className;
        this.parameters = parameters;
        inputs = new ArrayList<>();

        setTitle(String.format("Create %s object", className));

        setResizable(true);

        setup();
    }

    private void setup() {
        GridPane pane = new GridPane();

        Label instanceNameLbl = new Label("Instance name: ");
        TextField instanceNameTxt = new TextField();
        pane.add(instanceNameLbl, 0, 0);
        pane.add(instanceNameTxt, 1, 0);

        if(parameters.length > 0) {
            setHeaderText("Please provide the required parameters");

            int rowIndex = 1;

            for(Class param : parameters) {
                Label lbl = new Label(param.getSimpleName() + ": ");
                TextField txt = new TextField();
                pane.add(lbl, 0, rowIndex);
                pane.add(txt, 1, rowIndex);
                inputs.add(txt);
                rowIndex++;
            }

        }

        getDialogPane().setContent(pane);

        ButtonType buttonTypeOk = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().add(buttonTypeOk);

        setResultConverter(button -> {
            if (button == buttonTypeOk) {

                String instanceName = instanceNameTxt.getText().trim();
                List<Object> arguments = new ArrayList<>();

                for(int i = 0; i < parameters.length; i++) {
                    TextField input = inputs.get(i);
                    arguments.add(castArgument(parameters[i], input.getText()));
                }

                return new NewObject(instanceName, Arrays.asList(parameters), arguments);
            }
            return null;
        });
    }

    private Object castArgument(Class type, String value) {
        Object result;
        value = value.trim();
        if(type == String.class) {
            result = value;
        } else if(type == int.class) {
            result = Integer.parseInt(value);
        } else if(type == double.class) {
            result = Double.parseDouble(value);
        } else if(type == float.class) {
            result = Float.parseFloat(value);
        } else if(type == char.class) {
            result = value.charAt(0);
        } else if(type == byte.class) {
            result = Byte.valueOf(value);
        } else if(type == short.class) {
            result = Short.valueOf(value);
        } else if(type == long.class) {
            result = Long.valueOf(value);
        } else if(type == boolean.class) {
            result = Boolean.valueOf(value);
        } else {
            throw new ClassCastException(String.format("Invalid argument value for %s: %s", type.getSimpleName(), value));
        }
        return result;
    }

}