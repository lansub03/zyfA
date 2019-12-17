package 临时;

/**
 * 编写应用程序，输出满足1+2+3+。。。+n<8888的最大整数n
 */
public class TaskTest {
    public static void main(String[] args) {
        int number = 8888;
        int sum = 0;//记录数字变化
        for(int i = 1; i<= number ; i++){
            sum = sum + i;
            System.out.println("比较第"+i+"次:"+sum+":"+number);
            if (sum >= number){
                System.out.println("最大整数n为："+ (i-1) );
                break;
            }
            System.out.println("我还没有停止运行");
            System.out.println("我还没有停止运行");
        }

    }
}
