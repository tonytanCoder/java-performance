package com.example.javaperformance.escapeanalysis;

/** -Xmx15m -Xms15m -XX:+DoEscapeAnalysis -XX:+PrintGC -XX:+EliminateAllocations 开启逃逸分析
 *
 * -Xmx15m -Xms15m -XX:-DoEscapeAnalysis -XX:+PrintGC -XX:+EliminateAllocations 关闭逃逸分析
 * */
public class AllocationOnThreadStackTest {
    public static void main(String[] args) throws InterruptedException {
        long begin = System.currentTimeMillis();
        allocate();
        System.out.println("cost:" + (System.currentTimeMillis() - begin));
    }


    private static void allocate() throws InterruptedException {
        for (int i = 0; i < 100000000; i++) {
            Artisan artisan = new Artisan();
            artisan.setId(1);
            artisan.setDesc("artisan");
        }
    }
}
