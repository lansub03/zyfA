package 设计模式.单例模式.懒汉模式;

import 设计模式.单例模式.饿汉模式.HungryManPattern;

public class Pattern {
    public static void main(String[] args) {
        SlackerPattern a = SlackerPattern.getSlackerPattern();
        SlackerPattern b = SlackerPattern.getSlackerPattern();

        System.out.println("a的内存地址:"+a);
        System.out.println("b的内存地址:"+b);
    }
}
