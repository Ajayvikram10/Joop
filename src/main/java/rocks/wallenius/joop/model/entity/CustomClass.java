package rocks.wallenius.joop.model.entity;

import org.fxmisc.richtext.CodeArea;

import java.nio.file.Path;

/**
 * Created by philipwallenius on 15/02/16.
 */
public class CustomClass {

    private String name;
    private String code;
    private Path path;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

}