package 基础.多线程;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class FirstCallable {
    public static void main(String[] args) {
        //创建Callable对象
        FirstCallable rt = new FirstCallable();
        //先使用Lambda表达式创建Callable<Integer>对象
        //使用FutureTask来包装Callable对象
        FutureTask<Integer> task = new FutureTask<>((Callable<Integer>)()->{
            int i=0;
            for(;i<100;i++) {
                System.out.println(Thread.currentThread().getName()+"循环变量i的值："+i);
            }
            return i;
        });
        for(int i=0;i<100;i++) {
            System.out.println(Thread.currentThread().getName()+" 的循环变量i的值："+i);
            if(i==20) {
                //实质还是以Callable对象来创建并启动的
                new Thread(task,"有返回值的线程").start();

                /*Thread t = new Thread(task);
                t.start();*/
            }
        }
        try {
            System.out.println("子线程的返回值："+task.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
