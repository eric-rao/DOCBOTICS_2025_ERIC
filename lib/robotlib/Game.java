package robotlib;

import edu.wpi.first.wpilibj.RobotBase;

public class Game {

    private Server server;
    public static int robotIdx = 0;

    public Game(RobotBase robot) {
        Dashboard.getInstance();
        server = new Server();
        getRobotData();
        robot.start();
        sendUserData();
        server.setConnectionTimeout(1000);
        while (true) {
            getRobotData();
            robot.update();
            sendUserData();
        }
    }

    private void getRobotData() {
        String str = server.read();
        if (str == null) {
            System.out.println("Network Error: Did not receive any data from RobotSim! You can ignore this.");
            return;
        }
        float[] arr = server.strToFloatArr(str);

        if (Data.robotData == null || Data.userData == null)
            Data.initData(arr);
        else
            Data.robotData = arr;
    }

    private void sendUserData() {
        String str = server.floatArrToStr(Data.userData);
        server.send(str);
    }
}
