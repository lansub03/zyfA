package 设计模式.工厂模式.简单工厂模式;

public class TestFactory {
    public static void main(String[] args) {
        Product phone = ProductFactory.getProduct("phone");
        if (phone != null){
            //手机开始工作
            phone.work();
        }


        Product computer = ProductFactory.getProduct("computer");
        //手机开始工作
        computer.work();
    }
}
