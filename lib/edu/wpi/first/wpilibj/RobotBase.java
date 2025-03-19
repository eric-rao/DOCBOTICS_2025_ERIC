package edu.wpi.first.wpilibj;

import java.util.function.Supplier;

import robotlib.Game;

public abstract class RobotBase {

    public static <T extends RobotBase> void startRobot(Supplier<T> robotSupplier) {
        new Game(robotSupplier.get());
    }

    /**
     * This function is called once when the robot program starts.
     */
    public abstract void start();

    /**
     * This function is called repeatedly (frequency = RobotSim FPS) when the robot
     * runs.
     */
    public abstract void update();

    /**
     * A shortcut way to print to the console.
     */
    protected void print(Object message) {
        System.out.println(message);
    }
}
