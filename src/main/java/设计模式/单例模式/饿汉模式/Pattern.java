package 设计模式.单例模式.饿汉模式;

public class Pattern {
    public static void main(String[] args) {
        HungryManPattern a = HungryManPattern.getHungryManPattern();
        HungryManPattern b = HungryManPattern.getHungryManPattern();

        System.out.println("a的内存地址:"+a);
        System.out.println("b的内存地址:"+b);
    }
}
