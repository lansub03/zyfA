package 设计模式.单例模式.静态内部类单例模式;

public class StaticInnerClassSingleton {
    private StaticInnerClassSingleton(){}

    //单例持有者
    private static class InnerClaa{
        private final static StaticInnerClassSingleton scsl = new StaticInnerClassSingleton();
    }


    public static StaticInnerClassSingleton getStaticInnerClassSingleton(){
        return InnerClaa.scsl;
    }
}
