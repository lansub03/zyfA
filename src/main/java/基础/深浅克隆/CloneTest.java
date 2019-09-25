package 基础.深浅克隆;

public class CloneTest {
    public static void main(String[] args) throws CloneNotSupportedException {
        CatChild catChild = new CatChild();
        catChild.setName("猫儿子");
        Cat cat = new Cat();
        cat.setName("猫妈");
        cat.setCatChild(catChild);
        //复制Cat
        Cat cat2 = (Cat) cat.clone();
        cat2.setName("修改之后的猫妈");
        cat2.getCatChild().setName("修改之后的猫儿子");
        System.out.println("Cat1  : " + cat);
        System.out.println("Cat2  : " + cat2);
        System.out.println("Cat1 CatChnild name : " + cat.getCatChild().getName());
        System.out.println("Cat2 CatChnild name : " + cat2.getCatChild().getName());
    }
}