package 设计模式.工厂模式.简单工厂模式;

//产品工厂类
public class ProductFactory {
    public static Product getProduct(String name){
        if ("phone".equals(name)){
            return new Phone();
        }else if ("computer".equals(name)){
            return new Computer();
        }
        return null;
    }
}
