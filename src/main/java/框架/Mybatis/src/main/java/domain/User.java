package domain;

import lombok.Data;

import java.util.Date;

//自动生成set get to string
@Data
public class User {
    private int id;//数据库主键
    private String name;//名字
    private String address;//信息
    private Date birthday;//生日时间
    private String sex;//性别

    public User(int id, String name, String address, Date birthday, String sex) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.birthday = birthday;
        this.sex = sex;
    }
    public User() { }

}
