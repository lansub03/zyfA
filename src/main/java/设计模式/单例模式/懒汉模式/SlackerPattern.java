package 设计模式.单例模式.懒汉模式;

public class SlackerPattern {
    //定义一个静态变量，未初始化
    private static  SlackerPattern slackerPattern ;
    //私有化构造函数，防止外部实例化
    private SlackerPattern(){}
    /**
     * 添加class类锁，影响了性能，加锁之后将代码进行了串行化，
     * 我们的代码块绝大部分是读操作，在读操作的情况下，代码线程是安全的
     */
    public synchronized static SlackerPattern getSlackerPattern(){
        //使用时，先判断实例是否为null，如果实例为null，则创建对象
        if (slackerPattern ==null) slackerPattern = new SlackerPattern();
        return slackerPattern;
    }
}
