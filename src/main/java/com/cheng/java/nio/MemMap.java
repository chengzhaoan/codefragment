package com.cheng.java.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;


/**
 * Created by cheng on 2018/6/5.
 */
//https://blog.csdn.net/a953713428/article/details/64907250
//这篇文章的第一段代码应该是有问题的，都是nio ，一个用的内存映射，一个用的另一个，有些差异，应该跟传统io
public class MemMap {

    public static void main(String[] args){
        try{
            byte[] b = new byte[1024];

            System.out.println("==============case 普通IO 普通读");
            //bio
            long startTime2 = System.currentTimeMillis();
            FileInputStream fileInputStream = new FileInputStream("E:\\download\\西部世界.Westworld.S02E06.1080p-天天美剧字幕组.mp4");
            int hasRead = 0;
            while ((hasRead = fileInputStream.read(b)) > 0) {
            }
            fileInputStream.close();
            long endTime2 = System.currentTimeMillis();
            System.out.println("使用普通IO流方式读取文件总耗时： "+(endTime2 - startTime2));

            System.out.println("==============case 普通IO 缓存读");
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream("E:\\download\\西部世界.Westworld.S02E06.1080p-天天美剧字幕组.mp4"));
            long startTime3 = System.currentTimeMillis();
            while((hasRead = bufferedInputStream.read(b)) > 0){

            }
            bufferedInputStream.close();
            long endTime3 = System.currentTimeMillis();
            System.out.println("使用普通IO流  BufferInputStream方式读取文件总耗时： "+(endTime3- startTime3));


            System.out.println("==============case NIO 内存映射读");
            RandomAccessFile file = new RandomAccessFile("E:\\download\\西部世界.Westworld.S02E06.1080p-天天美剧字幕组.mp4","r");
            FileChannel channel = file.getChannel();//这个只有Channel 相当于Stream
            MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_ONLY,0,channel.size());

            //内存映射文件读法
            long len = file.length();
            long stratTime= System.currentTimeMillis();
            for(int i=0;i<file.length();i+=1024){
                if (len - i > 1024) {
                    mappedByteBuffer.get(b);
                } else {
                    mappedByteBuffer.get(new byte[(int)(len - i)]);
                }
            }
            long endTime = System.currentTimeMillis();
            channel.close();
            System.out.println("使用内存映射方式读取文件总耗时： "+(endTime - stratTime));


            System.out.println("==============case NIO 普通读");
            ByteBuffer buffer1 = ByteBuffer.allocate(1024);

            RandomAccessFile file1 = new RandomAccessFile("E:\\download\\西部世界.Westworld.S02E06.1080p-天天美剧字幕组.mp4","r");
            FileChannel channel1 = file1.getChannel();//这个只有Channel 相当于Stream

            long startTime1 = System.currentTimeMillis();
            while(channel1.read(buffer1) > 0){
                buffer1.flip();
                buffer1.clear();
            }

            long endTime1 = System.currentTimeMillis();
            System.out.println("使用NIO 一般读时间： "+(endTime1 - startTime1));





        }catch (IOException e){

        }
    }
}
//        可能跟RandomAccessFile有关
//        结果很意外啊
//       ==============case 普通IO 普通读
//        使用普通IO流方式读取文件总耗时： 2170
//        ==============case 普通IO 缓存读
//        使用普通IO流  BufferInputStream方式读取文件总耗时： 697
//        ==============case NIO 内存映射读
//        使用内存映射方式读取文件总耗时： 3409
//        ==============case NIO 普通读
//        使用NIO 一般读时间： 2034