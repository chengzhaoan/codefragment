package com.cheng.java.lang;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by cheng on 2018/6/22.
 * readblog
 * https://www.cnblogs.com/xiaoxi/p/6036701.html
 */
public class TestString2 {

    @Test
    public void  TestConstPool(){
        String a = "chenssy";
        String b = "chenssy";
        String c = new String("chenssy");

        Assert.assertTrue(a ==b);
        Assert.assertFalse(a ==c );


    }

    @Test
    public void testTestNewequles(){
        String b = "chenssy";

        String a = new String("chenssy");
        String c = new String("chenssy");


        Assert.assertFalse(a == c );
//      上边的代码虽然是最基本的测试，结果都懂，但是结合下边的代码，还有新的收获
//      新的String 里边的内容是指向原来的String的char[] 和 hash 这个不难理解，char[]是final类型，
//      需要复制也没什么问题，String 在构造的时候回做哪些工作呢，在类的构造环节在详细解释下。
//      new 的时候都干了什么？
//      public String(String original) {
//            this.value = original.value;
//            this.hash = original.hash;
//      }
    }

    //编译期确定
    @Test
    public void testStaticString(){
        String s0="helloworld";
        String s1="helloworld";
        String s2="hello"+"world";
        System.out.println("===========test3============");
        Assert.assertTrue(s0==s1); //true 可以看出s0跟s1是指向同一个对象
        Assert.assertTrue(s0==s2); //true 可以看出s0跟s2是指向同一个对象
    }
//编译期无法确定
    @Test
    public void compilingUnsure(){
        String s0="helloworld";
        String s1=new String("helloworld");
        String s2="hello" + new String("world");
        System.out.println("===========test4============");
        Assert.assertFalse( s0==s1 ); //false
        Assert.assertFalse( s0==s2 ); //false
        Assert.assertFalse( s1==s2 ); //false
    }
    //继续-编译期无法确定 体会一下
//    步骤：
//            1)栈中开辟一块中间存放引用str1，str1指向池中String常量"abc"。
//            2)栈中开辟一块中间存放引用str2，str2指向池中String常量"def"。
//            3)栈中开辟一块中间存放引用str3。
//            4)str1 + str2通过StringBuilder的最后一步toString()方法还原一个新的String对象"abcdef"，因此堆中开辟一块空间存放此对象。
//            5)引用str3指向堆中(str1 + str2)所还原的新String对象。
//            6)str3指向的对象在堆中，而常量"abcdef"在池中，输出为false。
    @Test
    public void compilingUnsure2(){
        String str1="abc";
        String str2="def";
        String str3=str1+str2;

        Assert.assertFalse(str3=="abcdef"); //false

    }
// 有符号就当成不能确定不优化
    @Test
    public void compilingUnsure3(){
        String s0 = "ab";
        String s1 = "b";
        String s2 = "a" + s1;

        Assert.assertFalse((s0 == s2)); //result = false
    }


    @Test
    public void Stringplus(){
        String test="javalanguagespecification";
        String str="java";
        String str1="language";
        String str2="specification";
       Assert.assertTrue(test == "java" + "language" + "specification");//编译器能够确定
       Assert.assertFalse(test == str + str1 + str2);//运行期才能够确定
    }

    @Test
    public void testStringFinal(){
        String s0 = "ab";
        final String s1 = "b";
        String s2 = "a" + s1;

        Assert.assertTrue((s0 == s2)); //不变的在编译器能够确定
    }

    @Test
    public void testStringFinal2(){
        String s0 = "ab";
        final String s1 = getS1();
        String s2 = "a" + s1;

        Assert.assertFalse((s0 == s2)); //不变的在编译器能够确定
    }
    private static String getS1() {
        return "b";
    }


    @Test
    public void testStringintern(){
        String s0 = "kvill";
        String s1 = new String("kvill");
        String s2 = new String("kvill");

        Assert.assertFalse( s0 == s1 ); //false

        s1.intern(); //虽然执行了s1.intern(),但它的返回值没有赋给s1
        s2 = s2.intern(); //把常量池中"kvill"的引用赋给s2
        Assert.assertFalse( s0 == s1); //flase
        Assert.assertTrue( s0 == s1.intern() ); //true//说明s1.intern()返回的是常量池中"kvill"的引用
        Assert.assertTrue( s0 == s2 ); //true
    }

    @Test
    public void StringPlus(){
        long start= System.currentTimeMillis();
        String s = null;
        for(int i = 0; i < 100000; i++) {
            s += "a";
        }
        long end= System.currentTimeMillis();
        System.out.println(end- start);

       long start1= System.currentTimeMillis();
        StringBuilder s1 = new StringBuilder();
        for(int i = 0; i < 100000; i++) {
            s1.append( "a");
        }
        s1.toString();
        long  end1= System.currentTimeMillis();
        System.out.println(end1- start1);
        //7215
        //1
    }
}
