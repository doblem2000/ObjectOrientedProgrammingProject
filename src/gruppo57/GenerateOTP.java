package gruppo57;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.IntegerProperty;


public class GenerateOTP implements Runnable{
    private final int delay;
    private final String filename;
    private final IntegerProperty otp;
    
    public GenerateOTP(int delay, String filename,IntegerProperty otp) {
        this.delay = delay;
        this.filename = filename;
        this.otp=otp;
    }
    
    @Override
    public void run() {
        Random r = new Random();
        r.setSeed(System.currentTimeMillis());
        
        while(!Thread.interrupted()){
            synchronized(otp){
                otp.setValue(r.nextInt(501));
                try(PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(filename)))){
                    pw.write("Il codice di sblocco OTP è: " + otp.getValue().toString());
                    //System.out.println("Il codice di sblocco OTP è: "+otp.getValue().toString());
                } catch (IOException ex) {
                    Logger.getLogger(GenerateOTP.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            try {
                Thread.sleep(1000*delay);
            } catch (InterruptedException ex) {
                return;
            }
        }
    }
    
}
