/*
 * Copyright (c) 2011, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
 * This source code is provided to illustrate the usage of a given feature
 * or technique and has been deliberately simplified. Additional steps
 * required for a production-quality application, such as security checks,
 * input validation and proper error handling, might not be present in
 * this sample code.
 */

package com.jdk8Demo.forkjion;
import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RecursiveAction;

/**
 * A class for sorting an array of {@code ints} in parallel.
 * A {@code ForkJoinPool} is used for the parallelism, using the merge sort
 * algorithm the array is split into halves and a new sub task is created
 * for each part. Each sub task is dispatched to the {@code ForkJoinPool}
 * which will schedule the task to a {@code Thread}.
 * This happens until the size of the array is at most 2
 * elements long. At this point the array is sorted using a simple compare
 * and possibly a swap. The tasks then finish by using insert sort to
 * merge the two just sorted arrays.
 *
 * The idea of this class is to demonstrate the usage of RecursiveAction not
 * to implement the best possible parallel merge sort. This version creates
 * a small array for each merge (creating a lot of objects), this could
 * be avoided by keeping a single array.
 */
public class MergeSort {
    private final ForkJoinPool pool;

    private static class MergeSortTask extends RecursiveAction {
        private final int[] array;
        private final int low;
        private final int high;
        private static final int THRESHOLD = 250;

      
        protected MergeSortTask(int[] array, int low, int high) {
            this.array = array;
            this.low = low;
            this.high = high;
           
        }

        @Override
        protected void compute() {
            if (high - low <= THRESHOLD) {
               // System.out.println(String.format("low:%d ,hight:%d" ,low ,high));
                try {
                    Arrays.sort(array, low+1, high+1);
                }catch(Exception e){
                    e.printStackTrace();
                    System.out.println(String.format("low:%d ,hight:%d" ,low ,high));
                    int lo = low;
                    while ((high-lo)>0){
                        System.out.println(array[lo++]);
                    }
                }

            } else {//理解下面逻辑：好比如压栈 此处可能会压栈很多， 把数组拆分成最小单元进行排序 然后合并
                int middle = (low + high ) >> 1;
                // Execute the sub tasks and wait for them to
                //

               /*这种写法是错误的？ */
                ForkJoinTask firstTask = new MergeSortTask(array, low, middle);
                ForkJoinTask scendTask = new MergeSortTask(array, middle+1, high);
                firstTask.fork();
                scendTask.fork();
                firstTask.join();
                scendTask.join();

              /*  ForkJoinTask firstTask = new MergeSortTask(array, low, middle);
                ForkJoinTask scendTask = new MergeSortTask(array, middle, high);
                invokeAll(firstTask ,scendTask);//这个地方是invokeALL
                firstTask.join();
                scendTask.join();*/
                // Then merge the results
                merge(middle);
            }
        }

        /**
         * Merges the two sorted arrays this.low, middle - 1 and middle, this.high - 1
         * @param middle the index in the array where the second sorted list begins
         */
        private void merge(int middle) {
            /*核心算法就是：定义两个指针同时指向两个排好序的数组子单元的最低位，同时从两个子单元中各取第一个
            进行比较，谁大小就取谁拷贝到原始数组，同时指针下一一位，直到两个子单元数组全部拷贝到原始数组*/
            if (array[middle - 1] <= array[middle]) { //最大的都不大于最小的 说明已经符合排序要求
                return; // the arrays are already correctly sorted, so we can skip the merge
            }
            int[] copy = new int[high - low +1 ];
            System.arraycopy(array, low, copy, 0, copy.length);
            //将排好序的两小份（实际上是连续的）拷贝出来作为原始备份
            int copyLow = 0; //原始备份低位index
            int copyHigh = high - low;//原始备份最高位index
            int copyMiddle = middle - low + 1;//原始备份中间index

            for (int i = low, p = copyLow, q = copyMiddle; i <= high; i++) {
                if (q >= copyHigh || (p < copyMiddle && copy[p] < copy[q]) ) {//q >= copyHigh 说明后面的数组取完了 肯定取前面的数组
                    array[i] = copy[p++];
                } else {
                    array[i] = copy[q++];
                }
            }
        }
    }

    /**
     * Creates a {@code MergeSort} containing a ForkJoinPool with the indicated parallelism level
     * @param parallelism the parallelism level used
     */
    public MergeSort(int parallelism) {
        pool = new ForkJoinPool(parallelism);
    }

    /**
     * Sorts all the elements of the given array using the ForkJoin framework
     * @param array the array to sort
     */
    public void sort(int[] array) {
       // System.out.println(Arrays.toString(array));
        ForkJoinTask<Void> job = pool.submit(new MergeSortTask(array, 0, array.length-1));

        //System.out.println(pool.getActiveThreadCount());
        job.invoke();
       // System.out.println(Arrays.toString(array));
    }
}
