package 基础.深浅克隆;

import lombok.Data;

import java.io.Serializable;

@Data
public class CatChild implements Cloneable {
    private String name;
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
