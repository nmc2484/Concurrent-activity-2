import java.util.Random;

public class Philsopher extends Thread{
    private int id;
    private Fork left;
    private Fork right;
    private boolean rHanded;
    private int nTimes;
    private long thinkMillis;
    private long eatMillis;

    public void Philosopher(int id, Fork left, Fork right, boolean rHanded, int nTimes, long thinkMillis, long eatMillis){
        this.id = id;
        this.left = left;
        this.right = right;
        this.rHanded = rHanded;
        this.nTimes = nTimes;
        this.thinkMillis = thinkMillis;
        this.eatMillis = eatMillis;
    }

    public void run(){
        //TODO if nTimes =0 run forever
        for (int i = 0; i <= nTimes; i++){
            long t = 0+(long) (Math.random() * ((this.thinkMillis - 0)+ 1));
            try{
            this.sleep(t);
            } catch( java.lang.InterruptedException ie){
            //TODO do we ignore or return something??
            }
            System.out.println("Philosopher " + this.id + " thinks for " + t + " seconds " );
            if(this.rHanded){
                System.out.println("Philosopher " + this.id + " goes for right fork ");
                this.right.acquire();
                Thread.yield();
                System.out.println("Philosopher " + this.id + " goes for left fork" );
                this.left.acquire();
                Thread.yield();
            }else{
                System.out.println(this.id + " goes for left or right");
                this.left.acquire();
                //TODO does left handed still take right fork? what what?!
                //TODO does philosopher go for other fork?????????
            }
            long eatTime = 0+(long) (Math.random() * ((this.eatMillis - 0)+ 1));
            System.out.println("Philosopher " + this.id + " eats for " + eatTime + " seconds");
            try{
            this.sleep(eatTime);
            } catch(java.lang.InterruptedException ie){
            //TODO do we ignore or return something??
            }
            //Philosopher releases their right fork
            this.right.release();
            System.out.println("Philosopher " + this.id + " releases right fork");
            //Philosopher releases their left fork
            this.left.release();
            System.out.println("Philosopher " + this.id + " releases left fork");
        }
    }

}
