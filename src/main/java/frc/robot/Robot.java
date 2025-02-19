// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.*; //libraries, simplified
import com.revrobotics.spark.*;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard; //dashboard to read values

/**
 * The methods in this class are called automatically corresponding to each mode, as described in
 * the TimedRobot documentation. If you change the name of this class or the package after creating
 * this project, you must also update the Main.java file in the project.
 */
public class Robot extends TimedRobot {
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */

  private TalonSRX LeftMasterMotor1 = new TalonSRX(18); // 4 drive motors
  private TalonSRX LeftMasterMotor2 = new TalonSRX(3);
  private TalonSRX RightMasterMotor1 = new TalonSRX(4);
  private TalonSRX RightMasterMotor2 = new TalonSRX(1);

  private SparkMax PIDMotor = new SparkMax(13, MotorType.kBrushless); //test neo motor

  private Joystick joy1 = new Joystick(0); //joystick

  private final double kDriveTick2Feet = 1.0 / 128 * 6 * Math.PI / 12;

  public Robot() {}

  @Override
  public void autonomousInit() {
  }

  @Override
  public void autonomousPeriodic() {
  }
   
  final double kP = 0.3; //kP value for PID
  final double kI = 0.5;
  final double kD = 0.1;
  final double iLimit = 1;
  final double maxSpeed = 0.5;

  double setpoint = 0; //target encoder value
  double errorSum = 0;
  double lastTimestamp = Timer.getFPGATimestamp();
  double lastError = 0;

  @Override
  public void robotPeriodic() {
  }

  @Override
  public void teleopInit() {
    SmartDashboard.putString("Auto on?", "yes");
    PIDMotor.getEncoder().setPosition(0); //reset encoder
    errorSum = 0;
    lastError = 0;
    lastTimestamp = Timer.getFPGATimestamp();
  }

  @Override
  public void teleopPeriodic() {
    /*
    double speed = -joy1.getRawAxis(1) * 0.6;
    double turn = joy1.getRawAxis(4) * 0.3;

    double left = speed + turn;
    double right = speed - turn;

    
    LeftMasterMotor1.set(ControlMode.PercentOutput, left);
    LeftMasterMotor2.set(ControlMode.PercentOutput, left);
    RightMasterMotor1.set(ControlMode.PercentOutput, -right);
    RightMasterMotor2.set(ControlMode.PercentOutput, -right);
    */

    if (joy1.getRawButton(1)){ //go to position 10
      setpoint = 10;
    } else if (joy1.getRawButton(2)){
      setpoint = 0;
    }

    //calculating speed with PID
    double sensorPosition = PIDMotor.getEncoder().getPosition() * kDriveTick2Feet;  
    double error = setpoint - sensorPosition;
    double dt = Timer.getFPGATimestamp() - lastTimestamp; 
    if (Math.abs(error) < iLimit){
      errorSum += error * dt;
    }

    double errorRate = (error  - lastError) / dt;

    double outputSpeed = kP * error; //+ kI * errorSum + kD * errorRate;

    //caps the output speed between -maxSpeed and maxSpeed
    outputSpeed = Math.max(-maxSpeed, Math.min(maxSpeed, outputSpeed));

    PIDMotor.set(outputSpeed);

    lastTimestamp = Timer.getFPGATimestamp(); 
    lastError = error;

    //outputting values
    //SmartDashboard.putNumber("encoder value", PIDMotor.getEncoder().getPosition() * kDriveTick2Feet); //output to dashboard
    SmartDashboard.putNumber("speed", outputSpeed);
    SmartDashboard.putNumber("kP", kP*error);
    /*
    SmartDashboard.putNumber("kI", kI*errorSum);
    SmartDashboard.putNumber("kD", kD*errorRate);
    */
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
