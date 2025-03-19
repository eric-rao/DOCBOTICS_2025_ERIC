// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;

/**
 * The methods in this class are called automatically corresponding to each mode, as described in
 * the TimedRobot documentation.
 */
public class Robot extends TimedRobot {

  // Drive motors
  private TalonSRX LeftMasterMotor1 = new TalonSRX(18);
  private TalonSRX LeftMasterMotor2 = new TalonSRX(3);
  private TalonSRX RightMasterMotor1 = new TalonSRX(4);
  private TalonSRX RightMasterMotor2 = new TalonSRX(1);

  // PID-controlled NEO motor
  private SparkMax PIDMotor = new SparkMax(13, MotorType.kBrushless); 

  // Joystick
  private Joystick joy1 = new Joystick(0); 

  // Encoder conversion factor (ticks to feet)
  private final double kDriveTick2Feet = 1.0 / (128 * 6 * Math.PI / 12);

  // PID Constants
  final double kP = 0.3;
  final double kI = 0.5;
  final double kD = 0.1;
  final double iLimit = 1;
  final double maxSpeed = 0.5;

  // PID Variables
  double setpoint = 0;       // Target position
  double errorSum = 0;        // Sum of errors for integral term
  double lastTimestamp = 0;   // Time tracking for delta time
  double lastError = 0;       // Previous error for derivative term

  public Robot() {}

  @Override
  public void robotInit() {
    SmartDashboard.putString("Status", "Robot initialized.");
  }

  @Override
  public void autonomousInit() {
    SmartDashboard.putString("Mode", "Autonomous");
  }

  @Override
  public void autonomousPeriodic() {
    // Autonomous code here
  }

  @Override
  public void teleopInit() {
    SmartDashboard.putString("Mode", "Teleop");
    
    // Reset encoder and PID values
    PIDMotor.getEncoder().setPosition(0.0);  
    errorSum = 0;
    lastError = 0;
    lastTimestamp = Timer.getFPGATimestamp();
  }

  @Override
  public void teleopPeriodic() {
    // Setpoints for PID control based on joystick buttons
    if (joy1.getRawButton(1)) {    
      setpoint = 10;               // Go to position 10
    } else if (joy1.getRawButton(2)) {
      setpoint = 0;                // Return to position 0
    }

    // Manual driving
    double speed = -joy1.getRawAxis(1) * 0.6;  // Forward/backward
    double turn = joy1.getRawAxis(4) * 0.3;    // Turning

    double left = speed + turn;
    double right = speed - turn;

    // PID calculations
    double sensorPosition = PIDMotor.getEncoder().getPosition() * kDriveTick2Feet;  
    double error = setpoint - sensorPosition;
    double dt = Timer.getFPGATimestamp() - lastTimestamp;

    // Integral accumulation
    if (Math.abs(error) < iLimit) {
      errorSum += error * dt;
    }

    // Derivative calculation
    double errorRate = (error - lastError) / dt;

    // PID output calculation
    double outputSpeed = kP * error + kI * errorSum + kD * errorRate;

    // Clamp output speed
    outputSpeed = Math.max(-maxSpeed, Math.min(maxSpeed, outputSpeed));

    // Set PID-controlled motor
    PIDMotor.set(outputSpeed);

    // Drive motors manually if PID is inactive
    if (!joy1.getRawButton(1) && !joy1.getRawButton(2)) {
      LeftMasterMotor1.set(ControlMode.PercentOutput, left);
      LeftMasterMotor2.set(ControlMode.PercentOutput, left);
      RightMasterMotor1.set(ControlMode.PercentOutput, -right);
      RightMasterMotor2.set(ControlMode.PercentOutput, -right);
    }

    // Update timestamps and errors
    lastTimestamp = Timer.getFPGATimestamp();
    lastError = error;

    // Output values to SmartDashboard
    SmartDashboard.putNumber("Encoder Value (Feet)", sensorPosition);
    SmartDashboard.putNumber("Setpoint", setpoint);
    SmartDashboard.putNumber("Output Speed", outputSpeed);
    SmartDashboard.putNumber("kP Term", kP * error);
    SmartDashboard.putNumber("kI Term", kI * errorSum);
    SmartDashboard.putNumber("kD Term", kD * errorRate);
  }

  /*
  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}
  */
}
