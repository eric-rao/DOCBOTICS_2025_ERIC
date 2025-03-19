package edu.wpi.first.wpilibj;

import robotlib.Dashboard;
import robotlib.Data;
import robotlib.RobotMode;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public abstract class TimedRobot extends RobotBase {
    private final Dashboard dashboard = Dashboard.getInstance();
    private RobotMode robotMode, prevRobotMode;
    private double startTime = 0;
    private int practiceModeStages;

    public void robotInit() {
    }

    public void robotPeriodic() {
    }

    public void disabledInit() {
    }

    public void disabledPeriodic() {
    }

    public void autonomousInit() {
    }

    public void autonomousPeriodic() {
    }

    public void teleopInit() {
    }

    public void teleopPeriodic() {
    }

    public void testInit() {
    }

    public void testPeriodic() {
    }

    @Override
    public void start() {
        dashboard.setConnectionStatus(true);
        dashboard.enableButtonPressed(false);
        robotMode = dashboard.getMode();
        robotInit();
    }

    @Override
    public void update() {
        robotMode = dashboard.getMode();

        Data.disableMotors();
        robotPeriodic();

        if (robotMode == RobotMode.Disabled) {
            if (prevRobotMode != robotMode)
                disabledInit();
            disabledPeriodic();
            Data.disableMotors();
        } else if (robotMode == RobotMode.Teleop) {
            if (prevRobotMode != robotMode)
                teleopInit();
            teleopPeriodic();
        } else if (robotMode == RobotMode.Auto) {
            if (prevRobotMode != robotMode)
                autonomousInit();
            autonomousPeriodic();
        } else if (robotMode == RobotMode.Test) {
            if (prevRobotMode != robotMode)
                testInit();
            testPeriodic();
        } else if (robotMode == RobotMode.Practice) {
            if (prevRobotMode != robotMode) {
                practiceModeStages = 0;
                startTime = Timer.getFPGATimestamp();
                dashboard.setStatusLabel(
                        "<html><center style='font-size:14px'>Autonomous</center><center style='font-size:14px'>Disabled</center></html>");
            }
            runPracticeMode();
        } else {
        }

        if (robotMode != RobotMode.Disabled && robotMode != RobotMode.Practice) {
            if (prevRobotMode != robotMode)
                startTime = Timer.getFPGATimestamp();
            dashboard.setTimestamp(Timer.getFPGATimestamp() - startTime);
        }

        if (SmartDashboard.isChanged())
            dashboard.updateSmartDashboard(SmartDashboard.getTreeMap(), SmartDashboard.getLongestStrLen());

        prevRobotMode = robotMode;
    }

    private void runPracticeMode() {
        double elapsedTime = Timer.getFPGATimestamp() - startTime;
        double totalTime = 5;
        if (practiceModeStages == 1) {
            totalTime = 15;
            autonomousPeriodic();
        } else if (practiceModeStages == 2) {
            totalTime = 135;
            teleopPeriodic();
        }

        double countdown = totalTime - elapsedTime;
        dashboard.setTimestamp(countdown);
        if (countdown < 0) {
            startTime = Timer.getFPGATimestamp();
            practiceModeStages += 1;
            if (practiceModeStages == 1) {
                dashboard.setStatusLabel(
                        "<html><center style='font-size:14px'>Autonomous</center><center style='font-size:14px'>Enabled</center></html>");
                autonomousInit();
            } else if (practiceModeStages == 2) {
                dashboard.setStatusLabel(
                        "<html><center style='font-size:14px'>Teleoperated</center><center style='font-size:14px'>Enabled</center></html>");
                teleopInit();
            }

        }

        if (practiceModeStages == 3) {
            practiceModeStages = 0;
            dashboard.enableButtonPressed(false);
            dashboard.setStatusLabel(
                    "<html><center style='font-size:14px'>Practice</center><center style='font-size:14px'>Disabled</center></html>");

        }
    }
}
