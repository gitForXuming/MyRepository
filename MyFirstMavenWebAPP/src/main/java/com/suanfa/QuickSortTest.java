package com.suanfa;

import java.util.Arrays;
import java.util.Random;

/**
 * 
* @Title: QuickSortTest.java 
* @Package com.util 
* @Description: TODO(快速排序例子) 
* @author xuming  
* @Date 2017年2月16日 下午2:17:01 
* @Version V1.0
 */
public class QuickSortTest {
	public static void main(String[] args) {
		int[] src = new int[100];
		Random random = new Random();
		for(int i =0;i<100;i++){
			src[i] = random.nextInt(200);
		}
		
		System.out.println(Arrays.toString(src));
		quickSort(src , 0 , src.length-1);
		System.out.println(Arrays.toString(src));
	}
	
	/**
	 * 
	* @Title: quickSort 
	* @Date 2017年2月16日 下午2:17:58 
	* @Description: TODO(快排) 
	* @Param     
	* @Return void    返回类型 
	* @Throws
	 */
	public static void quickSort(int[] src , int start, int end){
		int i ,j ;
		i = start;
		j = end;
		
		while(i<j){
			//以i 为基准 右边扫描 比src[i]小的 全部交换到src[i] 的左边
			while(i<j && src[i]<=src[j]){
				j--;
			}
			//交换
			if(i<j){
				int temp = src[i];
				src[i] = src[j];
				src[j] = temp;
			}
			//此时基准已经变到src[j]
			
			while(i<j && src[i]<src[j]){
				i++;
			}
			if(i<j){
				int temp = src[i];
				src[i] = src[j];
				src[j] = temp;
			}
			if(i-start>1){
				quickSort(src , 0 ,i-1);
			}
			
			if(end -j >1){
				quickSort(src , i+1 ,end);
			}
		}
	}
}
