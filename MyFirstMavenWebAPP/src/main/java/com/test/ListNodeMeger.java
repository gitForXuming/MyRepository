package com.test;

import java.util.Arrays;

/**
 * 
* @Title: ListNodeMeger.java 
* @Package com.test 
* @Description: TODO(面试题中的一个上机题 现有两个升序单向链表 合并成一个并保证合并后的链表也是升序 考点就是归并排序 
* 还不如写归并排序来的快 链表浪费时间) 
* @author xuming  
* @Date 2017年2月23日 下午4:19:45 
* @Version V1.0
 */
public class ListNodeMeger {
	
	public static void main(String[] args) {
		int src1[] = new int[]{0, 2, 2, 3, 4, 7, 7, 8, 8, 9};
		int src2[] = new int[]{1, 1, 3, 4, 5, 5, 7, 9, 9, 9};
		
		Arrays.sort(src1);
		ListNode headOne = new ListNode(src1[0]);
		for(int i=1;i<src1.length;i++){
			ListNode  endNode = headOne;
			while(null!=endNode.next){
				endNode = endNode.next;
			}
			endNode.next = new ListNode(src1[i]);
		}
		
		ListNode headTwo = new ListNode(src2[0]);
		for(int i=1;i<src2.length;i++){
			ListNode  endNode = headTwo;
			while(null!=endNode.next){
				endNode = endNode.next;
			}
			endNode.next = new ListNode(src2[i]);
		}
		
		ListNode sortNode = null;
		ListNode startNode = null;//为了打印
		ListNode oneCurr = headOne;
		ListNode twoCurr = headTwo;
		int i ,j ;
		i=j=0;
		while(i <src1.length && j<src2.length){
			if(oneCurr.val<=twoCurr.val){
				if(null==sortNode){
					sortNode = oneCurr;
				}else{
					sortNode.next = oneCurr;
					sortNode = sortNode.next;
				}
				i++;
				oneCurr = oneCurr.next;
			}else{
				if(null==sortNode){
					sortNode = twoCurr;
				}else{
					sortNode.next = twoCurr;
					sortNode = sortNode.next;
				}
				j++;
				twoCurr = twoCurr.next;
			}
			if(i==0 || j==0){
				startNode = sortNode;
			}
		}
		while(i<src1.length){
			sortNode.next = oneCurr;
			sortNode = sortNode.next;
			oneCurr =oneCurr.next;
			i++;
				
		}
		
		while(j<src2.length){
			sortNode.next = twoCurr;
			sortNode = sortNode.next;
			twoCurr =twoCurr.next;
			j++;
		}
		
		while(null!=startNode){
			System.out.println(startNode.val);
			startNode =startNode.next;
		}
	}
	
	
	public static class ListNode{
		int val;
		ListNode next;
		ListNode(int x){
			this.val =x;
		}
	}
}
