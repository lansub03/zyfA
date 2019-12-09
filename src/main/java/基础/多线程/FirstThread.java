package 基础.多线程;

public class FirstThread extends Thread {
    @Override
    public void run() {
        for (int i=0 ; i<=10 ; i++){
            //返回当前线程的名称
            String threadName = getName()+""+i;
            System.out.println(threadName);
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            //获取当前线程
            System.out.println(Thread.currentThread().getName()+""+i);
            //创建并启动第一个线程
            FirstThread firstThread01 = new FirstThread();
            firstThread01.start();
            //创建并启动第二个线程
            FirstThread firstThread02 = new FirstThread();
            firstThread02.start();

        }
    }
}
