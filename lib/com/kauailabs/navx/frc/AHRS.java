package com.kauailabs.navx.frc;

import robotlib.Data;
import robotlib.RobotData;

public class AHRS {

    private double resetVal = 0f;

    public AHRS(Object obj) {

    }

    public double getAngle() {
        return Data.robotData[RobotData.robotAngle.ordinal()] - resetVal;
    }

    public void reset() {
        resetVal = getAngle();
    }
}
