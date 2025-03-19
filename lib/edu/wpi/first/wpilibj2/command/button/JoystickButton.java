package edu.wpi.first.wpilibj2.command.button;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.util.ErrorMessages.requireNonNullParam.ErrorMessages;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.Subsystem;

public class JoystickButton {
    private final Joystick m_joystick;
    private final int m_buttonNumber;

    /**
     * Creates a joystick button for triggering commands.
     *
     * @param joystick     The GenericHID object that has the button (e.g. Joystick,
     *                     KinectStick, etc)
     * @param buttonNumber The button number (see
     *                     {@link GenericHID#getRawButton(int) }
     */
    public JoystickButton(Joystick joystick, int buttonNumber) {
        ErrorMessages.requireNonNullParam(joystick, "joystick", "JoystickButton");

        m_joystick = joystick;
        m_buttonNumber = buttonNumber;
    }

    /**
     * Gets the value of the joystick button.
     *
     * @return The value of the joystick button
     */
    public boolean get() {
        return m_joystick.getRawButton(m_buttonNumber);
    }

    /**
     * Starts the given command whenever the trigger just becomes active.
     *
     * @param command       the command to start
     * @param interruptible whether the command is interruptible
     * @return this trigger, so calls can be chained
     */
    public JoystickButton whenActive(final Command command, boolean interruptible) {
        ErrorMessages.requireNonNullParam(command, "command", "whenActive");

        CommandScheduler.getInstance().addButton(new Runnable() {
            private boolean m_pressedLast = get();

            @Override
            public void run() {
                boolean pressed = get();

                if (!m_pressedLast && pressed) {
                    command.schedule(interruptible);
                }

                m_pressedLast = pressed;
            }
        });

        return this;
    }

    /**
     * Starts the given command whenever the trigger just becomes active. The
     * command is set to be interruptible.
     *
     * @param command the command to start
     * @return this trigger, so calls can be chained
     */
    public JoystickButton whenActive(final Command command) {
        return whenActive(command, true);
    }

    /**
     * Runs the given runnable whenever the trigger just becomes active.
     *
     * @param toRun        the runnable to run
     * @param requirements the required subsystems
     * @return this trigger, so calls can be chained
     */
    public JoystickButton whenActive(final Runnable toRun, Subsystem requirements) {
        return whenActive(new InstantCommand(toRun, requirements));
    }

    /**
     * Constantly starts the given command while the button is held.
     *
     * <p>
     * {@link Command#schedule(boolean)} will be called repeatedly while the trigger
     * is active, and will be canceled when the trigger becomes inactive.
     *
     * @param command       the command to start
     * @param interruptible whether the command is interruptible
     * @return this trigger, so calls can be chained
     */
    public JoystickButton whileActiveContinuous(final Command command, boolean interruptible) {
        ErrorMessages.requireNonNullParam(command, "command", "whileActiveContinuous");

        CommandScheduler.getInstance().addButton(new Runnable() {
            private boolean m_pressedLast = get();

            @Override
            public void run() {
                boolean pressed = get();

                if (pressed) {
                    command.schedule(interruptible);
                } else if (m_pressedLast) {
                    command.cancel();
                }

                m_pressedLast = pressed;
            }
        });
        return this;
    }

    /**
     * Constantly starts the given command while the button is held.
     *
     * <p>
     * {@link Command#schedule(boolean)} will be called repeatedly while the trigger
     * is active, and will be canceled when the trigger becomes inactive. The
     * command is set to be interruptible.
     *
     * @param command the command to start
     * @return this trigger, so calls can be chained
     */
    public JoystickButton whileActiveContinuous(final Command command) {
        return whileActiveContinuous(command, true);
    }

    /**
     * Constantly runs the given runnable while the button is held.
     *
     * @param toRun        the runnable to run
     * @param requirements the required subsystems
     * @return this trigger, so calls can be chained
     */
    public JoystickButton whileActiveContinuous(final Runnable toRun, Subsystem... requirements) {
        return whileActiveContinuous(new InstantCommand(toRun, requirements));
    }

    /**
     * Starts the given command when the trigger initially becomes active, and ends
     * it when it becomes inactive, but does not re-start it in-between.
     *
     * @param command       the command to start
     * @param interruptible whether the command is interruptible
     * @return this trigger, so calls can be chained
     */
    public JoystickButton whileActiveOnce(final Command command, boolean interruptible) {
        ErrorMessages.requireNonNullParam(command, "command", "whileActiveOnce");

        CommandScheduler.getInstance().addButton(new Runnable() {
            private boolean m_pressedLast = get();

            @Override
            public void run() {
                boolean pressed = get();

                if (!m_pressedLast && pressed) {
                    command.schedule(interruptible);
                } else if (m_pressedLast && !pressed) {
                    command.cancel();
                }

                m_pressedLast = pressed;
            }
        });
        return this;
    }

    /**
     * Starts the given command when the trigger initially becomes active, and ends
     * it when it becomes inactive, but does not re-start it in-between. The command
     * is set to be interruptible.
     *
     * @param command the command to start
     * @return this trigger, so calls can be chained
     */
    public JoystickButton whileActiveOnce(final Command command) {
        return whileActiveOnce(command, true);
    }

    /**
     * Starts the command when the trigger becomes inactive.
     *
     * @param command       the command to start
     * @param interruptible whether the command is interruptible
     * @return this trigger, so calls can be chained
     */
    public JoystickButton whenInactive(final Command command, boolean interruptible) {
        ErrorMessages.requireNonNullParam(command, "command", "whenInactive");

        CommandScheduler.getInstance().addButton(new Runnable() {
            private boolean m_pressedLast = get();

            @Override
            public void run() {
                boolean pressed = get();

                if (m_pressedLast && !pressed) {
                    command.schedule(interruptible);
                }

                m_pressedLast = pressed;
            }
        });
        return this;
    }

    /**
     * Starts the command when the trigger becomes inactive. The command is set to
     * be interruptible.
     *
     * @param command the command to start
     * @return this trigger, so calls can be chained
     */
    public JoystickButton whenInactive(final Command command) {
        return whenInactive(command, true);
    }

    /**
     * Runs the given runnable when the trigger becomes inactive.
     *
     * @param toRun        the runnable to run
     * @param requirements the required subsystems
     * @return this trigger, so calls can be chained
     */
    public JoystickButton whenInactive(final Runnable toRun, Subsystem... requirements) {
        return whenInactive(new InstantCommand(toRun, requirements));
    }

    /**
     * Toggles a command when the trigger becomes active.
     *
     * @param command       the command to toggle
     * @param interruptible whether the command is interruptible
     * @return this trigger, so calls can be chained
     */
    public JoystickButton toggleWhenActive(final Command command, boolean interruptible) {
        ErrorMessages.requireNonNullParam(command, "command", "toggleWhenActive");

        CommandScheduler.getInstance().addButton(new Runnable() {
            private boolean m_pressedLast = get();

            @Override
            public void run() {
                boolean pressed = get();

                if (!m_pressedLast && pressed) {
                    if (command.isScheduled()) {
                        command.cancel();
                    } else {
                        command.schedule(interruptible);
                    }
                }

                m_pressedLast = pressed;
            }
        });
        return this;
    }

    /**
     * Toggles a command when the trigger becomes active. The command is set to be
     * interruptible.
     *
     * @param command the command to toggle
     * @return this trigger, so calls can be chained
     */
    public JoystickButton toggleWhenActive(final Command command) {
        return toggleWhenActive(command, true);
    }

    /**
     * Cancels a command when the trigger becomes active.
     *
     * @param command the command to cancel
     * @return this trigger, so calls can be chained
     */
    public JoystickButton cancelWhenActive(final Command command) {
        ErrorMessages.requireNonNullParam(command, "command", "cancelWhenActive");

        CommandScheduler.getInstance().addButton(new Runnable() {
            private boolean m_pressedLast = get();

            @Override
            public void run() {
                boolean pressed = get();

                if (!m_pressedLast && pressed) {
                    command.cancel();
                }

                m_pressedLast = pressed;
            }
        });
        return this;
    }

}
