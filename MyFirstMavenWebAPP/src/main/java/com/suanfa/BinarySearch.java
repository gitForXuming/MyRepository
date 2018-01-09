package com.suanfa;

import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

/**
 * 
* @Title: BinarySearch.java 
* @Package com.util 
* @Description: TODO(二分查找) 
* @author xuming  
* @date 2017年1月7日 下午2:51:56 
* @version V1.0
 */
public class BinarySearch {
	public static void main(String[] args) {
		int sourceCount =10;
		int[] array = new int[sourceCount];
		Random random = new Random();
		System.out.println(-1^(-1<<5));
		System.out.println(System.currentTimeMillis());
		System.out.println(Long.toBinaryString(2524582861L));
		System.out.println((2524582861L-1483784408L)<<9|0);
		System.out.println(Long.toBinaryString(-1l));
		UUID uuid = UUID.randomUUID();
		//System.out.println(String.valueOf(uuid).replaceAll("-", ""));
		for(int i=0;i<sourceCount;i++){
			array[i] = random.nextInt(sourceCount);
			
		}
		System.out.println(Arrays.toString(array));
		int targetValue = random.nextInt(sourceCount);
		Arrays.parallelSort(array);	//先排序
		System.out.println(Arrays.toString(array));
		int targetKey = search(array,0 ,array.length-1,targetValue);
		System.out.println(String.format("目标：%d 对应下标为：%d", targetValue ,targetKey));
	}
	
	public static int search(int[] array ,int low ,int high ,int targetValue){
		int targetKey = -1;
		if(targetValue == array[high]){
			return high;
		}
		if(targetValue == array[low]){
			return low;
		}
		if(low<high){
			int mid = (high +low)>>1;
			if(targetValue == array[mid]){
				return mid;
			}else if(targetValue < array[mid]){
				return search(array,low ,mid-1,targetValue);
			}else{
				return search(array,mid+1 ,high,targetValue);
			}
		}
		return targetKey;
	}
}
