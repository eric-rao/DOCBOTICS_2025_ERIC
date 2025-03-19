package robotlib;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.function.Consumer;

public class PrintManager {
    public PrintManager(Consumer<String> handleNewLog) {
        PrintStream origOut = System.out;
        PrintStream interceptor = new Interceptor(origOut, handleNewLog);
        System.setOut(interceptor);
    }
}

class Interceptor extends PrintStream {
    private final Consumer<String> handleNewLog;

    public Interceptor(OutputStream out, Consumer<String> handleNewLog) {
        super(out, true);
        this.handleNewLog = handleNewLog;
    }

    @Override
    public void print(String s) {// do what ever you like
        super.print(s);
        handleNewLog.accept(s);
    }

}
