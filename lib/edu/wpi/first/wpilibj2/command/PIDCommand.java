// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package edu.wpi.first.wpilibj2.command;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.util.ErrorMessages.requireNonNullParam.ErrorMessages;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A command that controls an output with a {@link PIDController}. Runs forever
 * by default - to add exit conditions and/or other behavior, subclass this
 * class. The controller calculation and output are performed synchronously in
 * the command's execute() method.
 */
public class PIDCommand extends CommandBase {
    protected final PIDController m_controller;
    protected Supplier<Double> m_measurement;
    protected Supplier<Double> m_setpoint;
    protected Consumer<Double> m_useOutput;

    /**
     * Creates a new PIDCommand, which controls the given output with a
     * PIDController.
     *
     * @param controller        the controller that controls the output.
     * @param measurementSource the measurement of the process variable
     * @param setpointSource    the controller's setpoint
     * @param useOutput         the controller's output
     * @param requirements      the subsystems required by this command
     */
    public PIDCommand(PIDController controller, Supplier<Double> measurementSource, Supplier<Double> setpointSource,
            Consumer<Double> useOutput, Subsystem... requirements) {
        ErrorMessages.requireNonNullParam(controller, "controller", "SynchronousPIDCommand");
        ErrorMessages.requireNonNullParam(measurementSource, "measurementSource", "SynchronousPIDCommand");
        ErrorMessages.requireNonNullParam(setpointSource, "setpointSource", "SynchronousPIDCommand");
        ErrorMessages.requireNonNullParam(useOutput, "useOutput", "SynchronousPIDCommand");

        m_controller = controller;
        m_useOutput = useOutput;
        m_measurement = measurementSource;
        m_setpoint = setpointSource;
        m_requirements.addAll(Set.of(requirements));
    }

    /**
     * Creates a new PIDCommand, which controls the given output with a
     * PIDController.
     *
     * @param controller        the controller that controls the output.
     * @param measurementSource the measurement of the process variable
     * @param setpoint          the controller's setpoint
     * @param useOutput         the controller's output
     * @param requirements      the subsystems required by this command
     */
    public PIDCommand(PIDController controller, Supplier<Double> measurementSource, double setpoint,
            Consumer<Double> useOutput, Subsystem... requirements) {
        this(controller, measurementSource, () -> setpoint, useOutput, requirements);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        m_controller.setSetpoint(m_setpoint.get());
        m_useOutput.accept(m_controller.calculate(m_measurement.get()));
    }

    @Override
    public void end(boolean interrupted) {
        m_useOutput.accept(0.0);
    }

    /**
     * Returns the PIDController used by the command.
     *
     * @return The PIDController
     */
    public PIDController getController() {
        return m_controller;
    }
}
