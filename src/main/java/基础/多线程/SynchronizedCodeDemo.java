package 基础.多线程;

public class SynchronizedCodeDemo implements Runnable {
    //100张电影票
    private static int tickets = 100;
    @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                boolean isSuccessTicket = getticket();
                if (isSuccessTicket){
                    System.out.println(Thread.currentThread().getName()+"抢到了电影票____目前电影票的数量为"+tickets);
                }else {
                    System.out.println(Thread.currentThread().getName()+"没有抢到电影票____目前电影票的数量为"+tickets);
                }
            }
        }
        public static void main(String[] args) {
        SynchronizedCodeDemo synchronizedCodeDemo = new SynchronizedCodeDemo();
            new Thread(synchronizedCodeDemo,"淘票").start();
            new Thread(synchronizedCodeDemo,"猫眼").start();
            new Thread(synchronizedCodeDemo,"美团").start();
    }
    //抢票方法
    public  boolean getticket(){
        synchronized("加锁"){
            //判断电影票数量是否足够
            if (tickets >0){
                tickets -=1;
                return true;
            }
            return false;
        }
    }
}
