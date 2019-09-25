package 设计模式.单例模式.枚举单例;

public class EnunmSingleCase {
    //同样的私有化构造方法
    private EnunmSingleCase(){
    }

    private enum EnumSingClass{
       POJO;
       private final EnunmSingleCase enunmSingleCase;
        EnumSingClass(){
            enunmSingleCase = new EnunmSingleCase();
        }
        private EnunmSingleCase getEnunmSingleCase(){
            return enunmSingleCase;
        }
    }

    public static EnunmSingleCase getInstance(){
        return EnumSingClass.POJO.getEnunmSingleCase();
    }
}
