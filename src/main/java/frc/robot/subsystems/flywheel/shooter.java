package frc.robot.subsystems.flywheel;

// import frc.robot.subsystems.lookuptable.setpoint;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class shooter extends SubsystemBase {
  private TalonFX intake = new TalonFX(16);

  private int distance;
  private Timer time3 = new Timer();

  public shooter() {

    intake.setNeutralMode(NeutralModeValue.Brake);
  }

  @Override
  public void periodic() {

    SmartDashboard.putNumber("Intake Current", intake.getStatorCurrent().getValueAsDouble());
    SmartDashboard.putNumber("Intake Velocity", intake.getVelocity().getValueAsDouble());
  }

  public boolean check() {
    return distance > 75;
  }

  public double getDistance() {
    return distance;
  }

  public void speed(double speed) {
    intake.set(speed);
  }

  public boolean hasCurrentSpike() {

    double current = intake.getStatorCurrent().getValueAsDouble();

    double CURRENT_SPIKE_THRESHOLD = 40.0;

    return current > CURRENT_SPIKE_THRESHOLD;
  }

  public Command cmd(double speed) {
    return new Command() {
      @Override
      public void initialize() {
        // Initialization code, such as resetting encoders or PID controllers
        // int kErrThreshold = 10; // how many sensor units until its close-enough
        // int kLoopsToSettle = 2; // how many loops sensor must be close-enough
        // int _withinThresholdLoops = 0;
      }

      @Override
      public void execute() {
        // check(position);
        intake.set(speed);
      }

      @Override
      public void end(boolean interrupted) {}

      @Override
      public boolean isFinished() {
        return false; // Check if the setpoint is reached
      }
    };
  }

  public boolean hasVelocity(double inputVelo) {

    double Velo = intake.getVelocity().getValueAsDouble();

    double velocitythreshold = inputVelo;

    return Math.abs(Velo) > velocitythreshold;
  }

  public Command autoncmdOut(double speed, double Velocity) {
    return new Command() {
      @Override
      public void initialize() {}

      @Override
      public void execute() {
        intake.set(speed);
      }

      @Override
      public void end(boolean interrupted) {
        intake.set(0);
      }

      @Override
      public boolean isFinished() {
        return hasVelocity(Velocity);
      }
    };
  }

  public Command autoncmdIn(double speed) {
    return new Command() {
      @Override
      public void initialize() {
        time3.reset();
        time3.start();
      }

      @Override
      public void execute() {
        intake.set(speed);
      }

      @Override
      public void end(boolean interrupted) {
        intake.set(0);
      }

      @Override
      public boolean isFinished() {
        // return time3.get() > .75 && Math.abs(intake.getVelocity().getValueAsDouble()) < 5;
        return time3.get() > .75;
      }
    };
  }

  public Command autoncmdInLong(double speed) {
    return new Command() {
      @Override
      public void initialize() {
        time3.reset();
        time3.start();
      }

      @Override
      public void execute() {
        intake.set(speed);
      }

      @Override
      public void end(boolean interrupted) {
        intake.set(0);
      }

      @Override
      public boolean isFinished() {
        // return time3.get() > .75 && Math.abs(intake.getVelocity().getValueAsDouble()) < 5;
        return time3.get() > 1.5;
      }
    };
  }
}
