package activity_2;

public class Fork implements IFork {

    private boolean allocated = false;

    public void acquire(){
        if (this.allocated){
            try{
            synchronized (Thread.currentThread()){
					Thread.currentThread().wait();
            }
            } catch(InterruptedException ie){
                //TODO do we ignore or return something??
            	System.out.println("Phil punched another Phil in the face!");
            }
        }
        this.allocated = true;
    }

    public void release(){
        this.allocated = false;
    }

}
