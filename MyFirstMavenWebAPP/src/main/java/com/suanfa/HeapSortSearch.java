package com.suanfa;

import java.util.Arrays;
import java.util.Random;

/**
 * 
* @Title: HeapSortSearch.java 
* @Package com.util 
* @Description: TODO(大数据查找) 
* @author xuming  
* @date 2017年1月7日 下午1:05:07 
* @version V1.0
 */
public class HeapSortSearch {
		public static void main(String[] args) {
			int sourceCount =1000;
			int[] array = new int[sourceCount];
			Random random = new Random();
			for(int i=0;i<sourceCount;i++){
				array[i] = random.nextInt(sourceCount);
			}
			System.out.println(Arrays.toString(array));
			
			search(array,3);
		}
		/**
		* @Title: search 
		* @Description: TODO(这里用一句话描述这个方法的作用) 
		* @param @param array
		* @param @param count
		* @param @return    设定文件 
		* @return int[]    返回类型 
		* @throws
		 */
		public static int[] search(int[] array,int count){
			int pos = count-1;
			sort(array ,0 ,count-1);
			System.out.println(Arrays.toString(array));
			while(pos < array.length-1){
				pos++;
				if(array[pos]>array[0]){
					sweep(array ,pos ,0);
					sort(array ,0 ,count-1);
				}
			}
			System.out.println(Arrays.toString(array));
			int[] temp =  new int[count];
			System.arraycopy(array, 0, temp, 0, count);
			return temp;
		}

		/**
		* @Title: sort 
		* @Description: TODO(区间排序 冒泡排序) 
		* @param @param array
		* @param @param begin
		* @param @param end    
		* @return void    返回类型 
		* @throws
		 */
		public static void sort(int[] array,int begin ,int end){
			for(int i=0 ;i<end- begin; i++){
				for(int j=0 ;j<end-begin-i;j++){
					if(array[j]>array[j+1]){
						sweep(array ,j ,j+1);
					}
				}
			}
		}
		/**
		* @Title: sweep 
		* @Description: TODO(位置交换) 
		* @param @param array
		* @param @param i
		* @param @param j     
		* @return void    返回类型 
		* @throws
		 */
		public static void sweep(int[] array,int i ,int j){
			
			int temp = array[i];
			array[i] = array[j];
			array[j] = temp;
					
		}
}
