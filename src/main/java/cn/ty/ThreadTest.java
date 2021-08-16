package cn.ty;

import java.util.concurrent.*;

public class ThreadTest {


    public static ExecutorService executorService = new ThreadPoolExecutor(4, 4, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

    public static void main(String[] args) {
        new ThreadTest().doMain();
    }

    public void doMain(){
        int i=0;
        while (i<2){
            EntityA entityA = new EntityA();
            entityA.setName("i："+i);
            ThreadA vfThread = new ThreadA(entityA);
            System.out.println("before entityA:"+entityA.getName()+",");
            Future<String> future = executorService.submit(vfThread);
            i++;
        }

    }

    class ThreadA implements Callable<String>{
        private EntityA entityA;

        public ThreadA(EntityA entityA){
            this.entityA = entityA;
        }

        @Override
        public String call() throws Exception {
            System.out.println(Thread.currentThread()+":do:"+entityA.getName());
            return null;
        }
    }
    class EntityA{
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
