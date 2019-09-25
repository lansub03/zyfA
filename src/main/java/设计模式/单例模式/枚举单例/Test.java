package 设计模式.单例模式.枚举单例;

public class Test {
    public static void main(String[] args) {
        EnunmSingleCase a = EnunmSingleCase.getInstance();
        EnunmSingleCase b = EnunmSingleCase.getInstance();
        System.out.println("a的内存地址："+a);
        System.out.println("b的内存地址："+b);
    }
}
