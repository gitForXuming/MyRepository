package com.suanfa;

import java.util.Arrays;
import java.util.Random;

public class MergeSortTest {
	public static void main(String[] args) {
		int[] src = new int[100];
		Random random = new Random();
		for(int i= 0;i<100;i++){
			src[i] = random.nextInt(200);
		}
		System.out.println(Arrays.toString(src));
		
		sort(src ,0 ,src.length-1);
		
		System.out.println(Arrays.toString(src));
	}
	
	/**
	 * 
	* @Title: sort 
	* @Description: TODO(排序) 
	* @param @param src
	* @param @param low
	* @param @param high    
	* @return void    返回类型 
	* @throws
	 */
	private static void sort(int[] src , int low ,int high){
		int mid = (low + high)>>1;
		if (low<high) {
			sort(src , low , mid);
			sort(src , mid+1 ,high);
			meger(src , low , mid, high);
		}
	}
	/**
	 * 
	* @Title: meger 
	* @Description: TODO(归并) 
	* @param     
	* @return void    返回类型 
	* @throws
	 */
	private static void meger(int[] src , int low ,int mid ,int high){
		int i ,j ,k;
		i = low;
		j = mid+1;
		k =0;
		int[] temp = new int[high - low +1];
		while (i<=mid && j<=high) {
			if(src[i]<src[j]){
				temp[k++] = src[i++];
			}else{
				temp[k++] = src[j++];
			}
		}
		
		while(i<=mid){
			temp[k++] = src[i++];
		}
		
		while(j<=high){
			temp[k++] = src[j++];
		}
		//System.out.println(Arrays.toString(temp));
		System.arraycopy(temp, 0, src, low, temp.length);
	}
}
