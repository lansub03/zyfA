package 基础.多线程;

public class FirstRunnable implements Runnable {
    public void run() {
        for (int i=0 ; i<=10 ; i++){
            //返回当前线程的名称(使用Runnable接口创建线程的时候，不能直接使用getName)
            String threadName = Thread.currentThread().getName()+""+i;
            System.out.println(threadName);
        }
    }
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            //获取当前线程
            System.out.println(Thread.currentThread().getName()+""+i);
            FirstRunnable firstRunnable = new FirstRunnable();
            //创建并启动二个单独的线程
            new Thread(firstRunnable,"子线程名称0").start();
            new Thread(firstRunnable,"子线程名称1").start();
        }
    }
}
