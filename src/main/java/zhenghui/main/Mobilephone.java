package zhenghui.main;

import jline.NoInterruptUnixTerminal;
import jline.Terminal;
import jline.TerminalFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * user: zhenghui on 2015/8/14.
 * date: 2015/8/14
 * time :21:47
 * email: zhenghui.cjb@taobao.com
 * 这个类是给agent 客户端发送线程，并打印接受回复的类
 */
public class Mobilephone implements Runnable{

    private int port;

    private Selector selector;

    private OutputStream sout;
    private InputStream sin;

    private volatile boolean stop = false;
    private CountDownLatch countDownLatch;

    public Mobilephone(int port,CountDownLatch countDownLatch) throws Exception {
        this.port = port;
        this.countDownLatch = countDownLatch;

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        selector = Selector.open();

        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        Terminal terminal = TerminalFactory.get();
//        terminal.init();
        sout = terminal.wrapOutIfNeeded(System.out);
        sin = terminal.wrapInIfNeeded(System.in);

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                stop = true;
            }
        }));

    }

    public void listen(){
        try{
            while (!stop){
                selector.select(500);
                Set<SelectionKey> selectionKeySet = selector.selectedKeys();
                for (SelectionKey selectionKey : selectionKeySet) {
                    handleSelectionKey(selectionKey);
                }
                //处理完了记得清除
                selectionKeySet.clear();
            }
            if(selector != null){
                selector.close();
            }
        }catch (Exception e){
            stop = true;
            countDownLatch.countDown();
            System.out.println("byte!");
        }

    }

    private void handleSelectionKey(SelectionKey selectionKey) throws Exception{
        if(selectionKey.isValid()){
            if(selectionKey.isAcceptable()){
                ServerSocketChannel serverSocketChannel2 = (ServerSocketChannel) selectionKey.channel();
                SocketChannel socketChannel = null;
                try {
                    socketChannel = serverSocketChannel2.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.socket().setSendBufferSize(1024);
                    socketChannel.socket().setTcpNoDelay(true);
                    socketChannel.socket().setSoLinger(true,1);
                    System.out.println("connection established on " + port);
                    socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                } catch (IOException e) {
                    e.printStackTrace();
                    if(socketChannel != null){
                        try {
                            socketChannel.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                    throw new IOException(e);
                }

                uninterestOps(SelectionKey.OP_ACCEPT, selectionKey);
            } else if(selectionKey.isReadable()){
                //如果是新的read请求,读取对应的消息，然后给客户端发送一个消息
                SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                output(sout,socketChannel);
                interestOps(SelectionKey.OP_READ, selectionKey);
            } else if(selectionKey.isWritable()){
                if(stop){
                    sendExit(selectionKey);
                } else {
                    write(selectionKey);
                }
            }
        }
    }

    private void write(SelectionKey selectionKey) throws IOException {
        input(sin,selectionKey);
        interestOps(SelectionKey.OP_WRITE, selectionKey);
    }

    private void output(OutputStream outputStream,SocketChannel socketChannel) throws Exception {
        byte[] bytes = new byte[4096];
        int read = socketChannel.read(ByteBuffer.wrap(bytes));
        if(read == -1){
            socketChannel.close();
            throw new Exception();
        }
        outputStream.write(bytes,0,read);
        if(read == bytes.length){
            output(outputStream,socketChannel);
        }
    }

    private void input(InputStream inputStream,SelectionKey selectionKey) throws IOException {
        int available = inputStream.available();
        if(available > 0){
            SelectableChannel selectableChannel = selectionKey.channel();
            byte[] bytes = new byte[available];
            inputStream.read(bytes);
            write((WritableByteChannel) selectableChannel,bytes);
        }
    }

    private void write(WritableByteChannel channel,byte[] bytes) throws IOException {
        channel.write(ByteBuffer.wrap(bytes));
    }

    private void sendExit(SelectionKey selectionKey) throws IOException {
        write((WritableByteChannel) selectionKey.channel(),"quit".getBytes());
    }


    private void interestOps(int op,SelectionKey key) {
        key.interestOps(key.interestOps() | op);
    }

    private void uninterestOps(int op,SelectionKey key){
        key.interestOps(key.interestOps() & ~op);
    }

    @Override
    public void run() {
        listen();
    }
}
