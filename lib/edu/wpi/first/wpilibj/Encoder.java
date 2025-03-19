package edu.wpi.first.wpilibj;

import robotlib.Data;
import robotlib.Game;
import robotlib.RobotData2018;
import robotlib.RobotData2021;

public class Encoder {

    private int whichEncoder;
    private boolean reverseDirection;
    private double resetVal;

    public Encoder(int channelA, int channelB) {
        this(channelA, channelB, false);
    }

    public Encoder(int channelA, int channelB, boolean reverseDirection) {
        resetVal = 0f;
        this.reverseDirection = reverseDirection;
        if (channelA == 0 && channelB == 1)
            whichEncoder = 1;
        else if (channelA == 2 && channelB == 3)
            whichEncoder = 2;
        else if (channelA == 4 && channelB == 5)
            whichEncoder = 3;
        else if (channelA == 6 && channelB == 7)
            whichEncoder = 4;
        else if (channelA == 8 && channelB == 9)
            whichEncoder = 5;
        else if (channelA == 10 && channelB == 11)
            whichEncoder = 6;
        else if (channelA == 12 && channelB == 13)
            whichEncoder = 7;
        else if (channelA == 14 && channelB == 15)
            whichEncoder = 8;
        else if (channelA == 16 && channelB == 17)
            whichEncoder = 9;
        else
            System.out.println("No encoder connected to channels: " + channelA + ", " + channelB);

        // if ((Game.robotIdx == 1 && whichEncoder > 3) || Game.robotIdx == 2 &&
        // whichEncoder > 9)
        // System.out.println(
        // "No encoder connected to channels: " + channelA + ", " + channelB + " for
        // robot " + Game.robotIdx);

    }

    public double get() {
        if (whichEncoder != 0) {
            if (Game.robotIdx == 1) {
                if (whichEncoder == 1)
                    return getEncoderValue(RobotData2018.leftDriveEncoder.ordinal());
                else if (whichEncoder == 2)
                    return getEncoderValue(RobotData2018.rightDriveEncoder.ordinal());
                else if (whichEncoder == 3)
                    return getEncoderValue(RobotData2018.elevatorEncoder.ordinal());
            } else if (Game.robotIdx == 2) {
                if (whichEncoder == 1)
                    return getEncoderValue(RobotData2021.frontLeftRotationEncoder.ordinal());
                else if (whichEncoder == 2)
                    return getEncoderValue(RobotData2021.frontRightRotationEncoder.ordinal());
                else if (whichEncoder == 3)
                    return getEncoderValue(RobotData2021.backLeftRotationEncoder.ordinal());
                else if (whichEncoder == 4)
                    return getEncoderValue(RobotData2021.backRightRotationEncoder.ordinal());
                else if (whichEncoder == 5)
                    return getEncoderValue(RobotData2021.frontLeftEncoder.ordinal());
                else if (whichEncoder == 6)
                    return getEncoderValue(RobotData2021.frontRightEncoder.ordinal());
                else if (whichEncoder == 7)
                    return getEncoderValue(RobotData2021.backLeftEncoder.ordinal());
                else if (whichEncoder == 8)
                    return getEncoderValue(RobotData2021.backRightEncoder.ordinal());
                else if (whichEncoder == 9)
                    return getEncoderValue(RobotData2021.shooterRPM.ordinal());
            }
        }

        System.out.println("Encoder cannot be used because it was not setup correctly.");
        return 0f;
    }

    private double getEncoderValue(int idx) {
        return (Data.toInt(Data.robotData[idx]) - resetVal) * (reverseDirection ? -1 : 1);
    }

    public void reset() {
        if (whichEncoder != 0) {
            resetVal = get();
        } else {
            System.out.println("Encoder cannot be used because it was not setup correctly.");
        }
    }

    public void reset(int resetTo) {
        if (whichEncoder != 0) {
            resetVal = resetTo;
        } else {
            System.out.println("Encoder cannot be used because it was not setup correctly.");
        }
    }

    public void setReverseDirection(boolean reverseDirection) {
        if (whichEncoder != 0) {
            this.reverseDirection = reverseDirection;
        } else {
            System.out.println("Encoder cannot be used because it was not setup correctly.");
        }
    }
}
