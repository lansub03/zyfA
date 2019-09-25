package 算法;

import java.util.ArrayList;
import java.util.List;

/**
 * 给出两个 非空 的链表用来表示两个非负的整数。其中，它们各自的位数是按照 逆序 的方式存储的，并且它们的每个节点只能存储 一位 数字。
 * 如果，我们将这两个数相加起来，则会返回一个新的链表来表示它们的和。
 * 您可以假设除了数字 0 之外，这两个数都不会以 0 开头。
 * 输入：(2 -> 4 -> 3) + (5 -> 6 -> 4)
 * 输出：7 -> 0 -> 8
 * 原因：342 + 465 = 807
 */

public class 两数相加 {
    public static void main(String[] args) {
        ListNode l1 = new ListNode(2);
        l1.next = new ListNode(4);
        l1.next.next = new ListNode(3);

        ListNode l2 = new ListNode(5);
        l2.next = new ListNode(6);
        l2.next.next = new ListNode(4);
        ListNode listNode = addTwoNumbers(l1, l2);
    }
    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        int listNodeNum = getListNodeNum(l1);
        int listNodeNum1 = getListNodeNum(l2);
        System.out.println(listNodeNum+listNodeNum1);
        return null;
    }

    //获取倒序之后的值
    public static int getListNodeNum(ListNode resultNum){
        ListNode isNullList =  resultNum.next;
        int rede = 1;
        int sum = 0;
        while(isNullList.val != 0){
            sum = resultNum.val * rede;
            rede = rede * 10;
            isNullList = resultNum.next;
        }
       return sum;
    }

}
 class ListNode {
    int val;
    ListNode next;
    ListNode(int x) { val = x; }
}