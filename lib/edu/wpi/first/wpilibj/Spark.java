package edu.wpi.first.wpilibj;

import robotlib.*;

public class Spark {

    private int port;
    private boolean inverted = false;
    private Dashboard dashboard = Dashboard.getInstance();

    public Spark(int port) {
        this.port = port;
    }

    public void set(double speed) {
        if (dashboard.getMode() == RobotMode.Disabled)
            return;

        speed = Data.clamp(speed, -1, 1) * (inverted ? -1f : 1f);
        if (Game.robotIdx == 1) {
            if (port == 0)
                Data.userData[UserData2018.leftSpeed.ordinal()] = (float) speed;
            else if (port == 1)
                Data.userData[UserData2018.rightSpeed.ordinal()] = (float) -speed;
            else if (port == 2)
                Data.userData[UserData2018.elevatorSpeed.ordinal()] = (float) speed;
            else if (port == 3)
                Data.userData[UserData2018.leftIntakeSpeed.ordinal()] = (float) speed;
            else if (port == 4)
                Data.userData[UserData2018.rightIntakeSpeed.ordinal()] = (float) speed;
            else
                System.out.println("No motors connected to port " + port + " for robot " + Game.robotIdx);
        } else if (Game.robotIdx == 2) {
            if (port == 0)
                Data.userData[UserData2021.frontLeftSpeed.ordinal()] = (float) speed;
            else if (port == 1)
                Data.userData[UserData2021.frontRightSpeed.ordinal()] = (float) speed;
            else if (port == 2)
                Data.userData[UserData2021.backLeftSpeed.ordinal()] = (float) speed;
            else if (port == 3)
                Data.userData[UserData2021.backRightSpeed.ordinal()] = (float) speed;
            else if (port == 4)
                Data.userData[UserData2021.frontLeftRotationSpeed.ordinal()] = (float) speed;
            else if (port == 5)
                Data.userData[UserData2021.frontRightRotationSpeed.ordinal()] = (float) speed;
            else if (port == 6)
                Data.userData[UserData2021.backLeftRotationSpeed.ordinal()] = (float) speed;
            else if (port == 7)
                Data.userData[UserData2021.backRightRotationSpeed.ordinal()] = (float) speed;
            else if (port == 8)
                Data.userData[UserData2021.shooterSpeed.ordinal()] = (float) speed;
            else if (port == 9)
                Data.userData[UserData2021.feederSpeed.ordinal()] = (float) speed;
            else if (port == 10)
                Data.userData[UserData2021.rollerSpeed.ordinal()] = (float) speed;
            else
                System.out.println("No motors connected to port " + port + " for robot " + Game.robotIdx);
        }
    }

    public boolean isInverted() {
        return inverted;
    }

    public void setInverted(boolean inverted) {
        this.inverted = inverted;
    }

}
