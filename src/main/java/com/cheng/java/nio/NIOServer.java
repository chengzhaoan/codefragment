package com.cheng.java.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**  https://blog.csdn.net/a953713428/article/details/64907250
 * Created by cheng on 2018/6/10.
 */
public class NIOServer {
    public static void  main(String[] args) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(1234));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT); //这个是应该是只注册了accept 事件

        while(true){
            if(selector.selectNow()< 0){ //非阻塞的看看有没有事件发生
                continue;
            }
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> itertaor = keys.iterator();
            while(itertaor.hasNext()){
                 SelectionKey key = itertaor.next();
                 itertaor.remove();
                 if(key.isAcceptable()){
                     ServerSocketChannel acceptServerSocketChannel = (ServerSocketChannel) key.channel();
                     SocketChannel  socketChannel = serverSocketChannel.accept();
                     System.out.println("Accept request from " + socketChannel.getRemoteAddress());
                     SelectionKey  readyKey  = socketChannel.register(selector,SelectionKey.OP_READ);//注册新事件
                     readyKey.attach(new Processor());
                 }else if(key.isReadable()){
                     Processor processor =(Processor) key.attachment();
                     processor.process(key);
                 }
            }
        }

    }
}

//刚开始写的时候把Processor 这个类写到了NIOServer 里，内部类不能有静态变量
class Processor{

    private static final ExecutorService service = Executors.newFixedThreadPool(16);

    public void process(final  SelectionKey selectionKey){
        service.submit(new Runnable() {
            public void run() {
                ByteBuffer buffer = null;
                SocketChannel socketChannel = null;
                try{
                    buffer = ByteBuffer.allocate(1024);
                    socketChannel = (SocketChannel)selectionKey.channel();
                    int count = socketChannel.read(buffer);
                    if(count<0){
                        socketChannel.close();;
                        selectionKey.channel();
                        System.out.println(socketChannel+"read ended");
                    }else if(count ==0){

                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
                System.out.println(socketChannel +"{}\t Read message {}" + new String(buffer.array()));
            }

        });
    }
}