package com.test;
/**
 * 
* @Title: ShallowStudent.java 
* @Package com.test 
* @Description: 潜复制 
* @author xuming  
* @Date 2016年12月28日 下午2:42:56 
* @Version V1.0
 */
public class ShallowStudent  implements Cloneable{
	  String name;// 常量对象。
	    int age;
	    DeepProfessor p;// 学生1和学生2的引用值都是一样的。
	 
	    ShallowStudent(String name, int age, DeepProfessor p) {
	        this.name = name;
	        this.age = age;
	        this.p = p;
	    }
	 
	    public Object clone() {
	        ShallowStudent o = null;
	        try {
	            o = (ShallowStudent) super.clone();
	            //对引用的对象也进行复制
	            o.p = (DeepProfessor) p.clone();
	        } catch (CloneNotSupportedException e) {
	            System.out.println(e.toString());
	        }
	        return o;
	    }
	 
	    public static void main(String[] args) {
	        DeepProfessor p = new DeepProfessor("wangwu", 50);
	        ShallowStudent s1 = new ShallowStudent("zhangsan", 18, p);
	        ShallowStudent s2 = (ShallowStudent) s1.clone();
	        s2.p.name = "lisi";
	        s2.p.age = 30;
	        System.out.println("name=" + s1.p.name + "," + "age=" + s1.p.age);
	        System.out.println("name=" + s2.p.name + "," + "age=" + s2.p.age);
	        //输出结果学生1和2的教授成为lisi,age为30。
	    }
	}
	 
	class DeepProfessor implements Cloneable {
	    String name;
	    int age;
	 
	    DeepProfessor(String name, int age) {
	        this.name = name;
	        this.age = age;
	    }
	 
	    public Object clone() {
	        DeepProfessor o = null;
	        try {
	            o = (DeepProfessor)super.clone();
	        } catch(CloneNotSupportedException e) {
	            System.out.println(e.toString());
	        }
	        return o;
	    }
}
