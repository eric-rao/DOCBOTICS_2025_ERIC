package robotlib;

public class Data {

    public static float[] userData;
    public static float[] robotData;

    private static final int version = 6;

    public static int toInt(float a) {
        return a > 0 ? (int) (a + 0.5f) : (int) (a - 0.5f);
    }

    public static int toInt(double a) {
        return a > 0 ? (int) (a + 0.5f) : (int) (a - 0.5f);
    }

    public static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }

    public static double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }

    public static void initData(float[] arr) {

        // for (float i : arr)
        // System.out.print(i + ", ");

        robotData = new float[arr.length];
        robotData = arr;
        Game.robotIdx = toInt(robotData[RobotData.robot.ordinal()]);

        if (Game.robotIdx == 1)
            userData = new float[UserData2018.values().length];
        else if (Game.robotIdx == 2)
            userData = new float[UserData2021.values().length];

        if (robotData[RobotData.version.ordinal()] != version) {
            System.out.print("FATAL ERROR! Java library version = [" + version + "], but Unity simulator version = ["
                    + robotData[RobotData.version.ordinal()] + "]! ");
            if (robotData[RobotData.version.ordinal()] > version)
                System.out.println("Please download the latest version of Java library.");
            else
                System.out.println("Please download the latest version of Unity simulator.");
            System.exit(1);
        }

        // Done with basic setup, below are some additional things

        userData[UserData.cameraIdx.ordinal()] = 0;

        if (Game.robotIdx == 1) {
            userData[UserData2018.leftSpeed.ordinal()] = 0;
            userData[UserData2018.rightSpeed.ordinal()] = 0;
        } else if (Game.robotIdx == 2) {
            userData[UserData2021.frontLeftSpeed.ordinal()] = 0;
            userData[UserData2021.frontRightSpeed.ordinal()] = 0;
            userData[UserData2021.backLeftSpeed.ordinal()] = 0;
            userData[UserData2021.backRightSpeed.ordinal()] = 0;
            userData[UserData2021.frontLeftRotationSpeed.ordinal()] = 0;
            userData[UserData2021.frontRightRotationSpeed.ordinal()] = 0;
            userData[UserData2021.backLeftRotationSpeed.ordinal()] = 0;
            userData[UserData2021.backRightRotationSpeed.ordinal()] = 0;
            userData[UserData2021.shooterSpeed.ordinal()] = 0;
            userData[UserData2021.rollerSpeed.ordinal()] = 0;
            userData[UserData2021.feederSpeed.ordinal()] = 0;
        }
    }

    public static void disableMotors() {
        // motor safety: disable all motor outputs if .set is not called or if diabled
        if (Game.robotIdx == 1) {
            userData[UserData2018.leftSpeed.ordinal()] = 0;
            userData[UserData2018.rightSpeed.ordinal()] = 0;
            userData[UserData2018.elevatorSpeed.ordinal()] = 0;
            userData[UserData2018.leftIntakeSpeed.ordinal()] = 0;
            userData[UserData2018.rightIntakeSpeed.ordinal()] = 0;
        } else if (Game.robotIdx == 2) {
            userData[UserData2021.frontLeftSpeed.ordinal()] = 0;
            userData[UserData2021.frontRightSpeed.ordinal()] = 0;
            userData[UserData2021.backLeftSpeed.ordinal()] = 0;
            userData[UserData2021.backRightSpeed.ordinal()] = 0;
            userData[UserData2021.frontLeftRotationSpeed.ordinal()] = 0;
            userData[UserData2021.frontRightRotationSpeed.ordinal()] = 0;
            userData[UserData2021.backLeftRotationSpeed.ordinal()] = 0;
            userData[UserData2021.backRightRotationSpeed.ordinal()] = 0;
            userData[UserData2021.shooterSpeed.ordinal()] = 0;
            userData[UserData2021.rollerSpeed.ordinal()] = 0;
            userData[UserData2021.feederSpeed.ordinal()] = 0;
        }
    }
}
