import java.util.*;
import java.io.*;

/**
 * The 'PythonProcess' class is the "main" class, which solve the task
 * @author Nurullokhon Gulomkodirov
 * @version 11.0
 */
class Stream extends Thread {
    private final InputStream is;
    private final String type;

    Stream(InputStream is, String type) {
        this.is = is;
        this.type = type;
    }

    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                System.out.println(type + ": " + line);
            }
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }
}

public class PythonProcess {
    static String checkPath(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Input path to Python interpreter: ");
        String path = sc.nextLine();
        File file = new File(path);
        if (!file.exists() || file.isDirectory()){
            System.out.println("Interpreter file does not exist in this path.");
            return "";
        }
        return path;
    }

    public static void main(String args[]) {
        String path = checkPath();
        if (!path.equals("")){
            try {
                String cmd[] = {path, "-m", "timeit", "-r", "10"};
                Runtime rt = Runtime.getRuntime();
                Process proc = rt.exec(cmd);

                // любое сообщение об ошибках
                Stream errorGobbler = new Stream(proc.getErrorStream(), "ERROR");
                Stream outputGobbler = new Stream(proc.getInputStream(), "OUTPUT");
                errorGobbler.start();
                outputGobbler.start();

                int seconds = 0;
                long start = System.currentTimeMillis();
                while (proc.isAlive()){
                    long current = System.currentTimeMillis();
                    if ((current - start) / 1000.0 >= 1){
                        start = System.currentTimeMillis();
                        seconds++;
                        System.out.println("Time passed: " + seconds);
                    }
                }
            }
            catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }
}