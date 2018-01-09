package com.suanfa;

import java.util.Arrays;
import java.util.Random;

/**
 *  简介:将两个（或两个以上）有序表合并成一个新的有序表 即把待排序序列分为若干个子序列，每个子序列是有序的。然后再把有序子序列合并为整体有序序列
* 时间复杂度为O(nlogn)
* 稳定排序方式
* @Title: MergeSort.java 
* @Package com.util 
* @Description: TODO(归并排序) 
* @author xuming  
* @date 2017年1月7日 下午1:28:45 
* @version V1.0
 */
public class MergeSort {
	public static void main(String[] args) {
		int sourceCount =15000;
		int[] array = new int[sourceCount];
		Random random = new Random();
		for(int i=0;i<sourceCount;i++){
			array[i] = random.nextInt(sourceCount);
		}
		System.out.println(Arrays.toString(array));
		sort(array,0 ,array.length-1);
		System.out.println(Arrays.toString(array));
	}
	/**
	 * 
	* @Title: sort 
	* @Description: TODO(排序) 
	* @param @param array
	* @param @param low
	* @param @param high    
	* @return void    返回类型 
	* @throws
	 */
	public static void sort(int[] array, int low ,int high){
		int mid = (high+low)>>1;  //这个地方容易出错 是 +  不是 -
		if(low<high){//如果low 小于high 就说明还可以拆分
			sort(array ,low ,mid);//排序左边的
			sort(array ,mid+1 ,high);//排序右边的
			
			merge(array ,low , mid ,high);//两边归并
		}
		return;
	}
	/**
	 * 
	* @Title: merge 
	* @Description: TODO(归并) 
	* @param @param array
	* @param @param low
	* @param @param mid
	* @param @param high    
	* @return void    返回类型 
	* @throws
	 */
	public static void merge(int[] array ,int low ,int mid ,int high){
		int[] temp = new int[high -low + 1];
		int i =low; // 左指针
		int j = mid +1;// 右指针

		int k =0;
		// 把较小的数先移到新数组中
		while(i<=mid && j<=high){//精髓 这个while跑完后 i 或者 j 肯定有一个已经到最大值了  下面的连个while 只会有一个跑
			if(array[i]<array[j]){
				temp[k++] = array[i++];
			}else{
				temp[k++] = array[j++];
			}
		}
		//下面两个while只会有一个运行
		// 把左边剩余的数移入数组
		while(i<=mid){
			temp[k++] = array[i++];
		}
		
		// 把右边边剩余的数移入数组
		while(j<=high){
			temp[k++] = array[j++];
		}
		
		System.arraycopy(temp, 0, array, low, temp.length);
	}
	
}
