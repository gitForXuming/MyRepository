package com.suanfa;

import java.util.Arrays;
import java.util.Random;

public class QuickSort {
    public static void main(String[] args) {
        int[] src = new int[15000];
        Random random = new Random();
        for (int i = 0; i < 15000; i++) {
            src[i] = random.nextInt(15000);
        }
        System.out.println(Arrays.toString(src));
        quickSort(src, 0, src.length - 1);
        System.out.println(Arrays.toString(src));

        int j = 0;
        int k = 0;
        while (k < 10) {
            if (src[j] == src[j + 1]) {

            } else {
                k++;
            }

            if (k == 10) {
                System.out.println(src[j]);
                break;
            }
            j++;
        }
    }

    /**
     * @Title: quickSort
     * @Date 2017年2月15日 下午2:46:48
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @Param @param src
     * @Param @param start
     * @Param @param end
     * @Return void    返回类型
     * @Throws
     */
    private static void quickSort(int[] src, int start, int end) {
        int i, j;
        i = start;
        j = end;
        while (i < j) {
            //默认已i 为基准  从左向右扫描 比j 小的交换一下位置
            while (i < j && src[i] >= src[j]) {
                i++;
            }
            if (i < j) {
                int temp = src[j];
                src[j] = src[i];
                src[i] = temp;
            }
            //此时 基准已经被交换到 j的位置
            while (i < j && src[i] > src[j]) {
                j--;
            }
            if (i < j) {
                int temp = src[j];
                src[j] = src[i];
                src[i] = temp;
            }
        }

        if (i - start > 1) {
            quickSort(src, 0, i - 1);
        }

        if (end - j > 1) {
            quickSort(src, j + 1, end);
        }

		/*int i, j;   
        i = start;   
        j = end;   
        if ((src == null) || (src.length == 0))   
            return;   
           
        while (i < j) {//查找基准点下标   
            while (i < j && src[i] <= src[j]){   
                // 以数组start下标的数据为基准，右侧扫描   找到比start小的一个数进行交换
                j--;   
            }
            if (i < j) { // 右侧扫描，找出第一个比基准小的，交换位置   
                int temp = src[i];   
                src[i] = src[j];   
                src[j] = temp;   
            }   
            // 
            while (i < j && src[i] < src[j])  { 
                // 左侧扫描（此时a[j]中存储着基准值）   
                i++;   
            }
            if (i < j) { // 找出第一个比基准大的，交换位置   
                int temp = src[i];   
                src[i] = src[j];   
                src[j] = temp;   
            }   
        }
        
        if (i - start > 1) { // 递归调用，把key前面的完成排序   
            quickSort(src, 0, i - 1);   
        }   
        if (end - j > 1) {   
            quickSort(src, j + 1, end); // 递归调用，把key后面的完成排序   
        }   */
    }
}
