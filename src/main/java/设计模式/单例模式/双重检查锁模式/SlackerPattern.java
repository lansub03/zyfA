package 设计模式.单例模式.双重检查锁模式;

public class SlackerPattern {
    //定义一个静态变量，未初始化
    //添加volatile关键字，在读操作完成之前，写操作必须全部完成。
    private static volatile  SlackerPattern slackerPattern ;
    //私有化构造函数，防止外部实例化
    private SlackerPattern(){}

    public  static SlackerPattern getSlackerPattern(){
        synchronized (SlackerPattern.class){
            //抢到锁后，判断是否为null
            if (slackerPattern ==null) slackerPattern = new SlackerPattern();
            return slackerPattern;
        }
    }
}
