package 设计模式.单例模式.饿汉模式;

public class HungryManPattern {
    //李忠静态变量来存储唯一实例
    private static final HungryManPattern hmp = new HungryManPattern();
    //私有化构造方法，防止外部进行实例化
    private HungryManPattern(){ }
    public static HungryManPattern getHungryManPattern(){
        return hmp;
    }
}
