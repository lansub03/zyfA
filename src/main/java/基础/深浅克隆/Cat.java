package 基础.深浅克隆;

import lombok.Data;

import java.io.Serializable;

@Data
public class Cat implements Cloneable {
    private String name;
    private Integer age;
    private CatChild catChild;
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


}
