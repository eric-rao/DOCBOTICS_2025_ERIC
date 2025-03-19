package robotlib;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;
    private Socket client;
    private InputStream inputStream;
    private OutputStream outputStream;

    public Server() {
        setupConnection();
    }

    private void setupConnection() {
        try {
            serverSocket = new ServerSocket(16000);
            while (client == null) {
                System.out.println(">> Waiting for connection on '" + getIPAdress() + "'. Please open RobotSim.");
                client = serverSocket.accept();
                setConnectionTimeout(3000);
                inputStream = client.getInputStream();
                outputStream = client.getOutputStream();
                System.out.println(
                        ">> Connection established, starting robot program. RobotSim captures joystick inputs; please go to RobotSim...\n>> ================================================");

            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(">> Network error when setting up connection! Exiting...");
            System.exit(1);
        }
    }

    public void setConnectionTimeout(int millis) {
        try {
            client.setSoTimeout(millis);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(">> Network error when setting connection timeout! Exiting...");
            System.exit(1);
        }
    }

    public String read() {
        byte[] bytes = new byte[2048];
        if (!client.isClosed()) {
            try {
                int data = inputStream.read(bytes);
                if (data != -1) {
                    String string = new String(bytes, 0, data);
                    return string;
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(">> Network error when receiving data! Exiting...");
                System.exit(1);
            }
        }
        return null;
    }

    public void send(String str) {
        try {

            byte[] bytes = str.getBytes();
            byte[] bytesSize = intToByteArray(str.length());
            outputStream.write(bytesSize, 0, 4);
            outputStream.write(bytes, 0, bytes.length);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(">> Network error when sending data! Exiting...");
            System.exit(1);
        }
    }

    private static byte[] intToByteArray(int a) {
        byte[] ret = new byte[4];
        ret[0] = (byte) (a & 0xFF);
        ret[1] = (byte) ((a >> 8) & 0xFF);
        ret[2] = (byte) ((a >> 16) & 0xFF);
        ret[3] = (byte) ((a >> 24) & 0xFF);
        return ret;
    }

    public String floatArrToStr(float[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            if (i != 0)
                sb.append(",");
            sb.append(Float.toString(arr[i]));
        }

        return sb.toString();
    }

    public float[] strToFloatArr(String str) {
        String[] arrOfStr = str.split(",");
        float[] arrOfFloat = new float[arrOfStr.length];
        for (int i = 0; i < arrOfStr.length; i++) {
            arrOfFloat[i] = Float.parseFloat(arrOfStr[i]);
        }
        return arrOfFloat;
    }

    private String getIPAdress() {
        try {
            return Inet4Address.getLocalHost().getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(">> Network error when getting IP address! Exiting...");
            System.exit(1);
            return null;
        }
    }
}
