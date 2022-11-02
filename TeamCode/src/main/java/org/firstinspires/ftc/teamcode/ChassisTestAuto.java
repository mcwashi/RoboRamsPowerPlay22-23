package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "8648 Chassis Test Autonomous", group = "Concept")
@Disabled
public class ChassisTestAuto extends LinearOpMode {
    ChassisTestHardwarePushbot robot = new ChassisTestHardwarePushbot();
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        robot.init(hardwareMap, false);
        telemetry.addData("Status", "Resetting Encoders");    //resets encoders
        telemetry.update();

        robot.leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData("Path0", "Starting at %7d :%7d :%7d :%7d",
                robot.leftFront.getCurrentPosition(),
                robot.leftBack.getCurrentPosition(),
                robot.rightFront.getCurrentPosition(),
                robot.rightBack.getCurrentPosition());
        telemetry.update(); //gets current positions of encoders, should be zero

        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();

        waitForStart();
        runtime.reset();
        //code goes here
        encoderDrive(robot.DRIVE_SPEED, 48.0, 48.0,48.0, 48.0, 10);
        sleep(1000);
        encoderDrive(robot.TURN_SPEED, -48, 48, 48, -48, 10);
        sleep(1000);
     


    }
    public void encoderDrive(double speed,
                             double leftFInches, double rightFInches, double leftBInches, double rightBInches,
                             double timeoutS) {
        int newLeftBackTarget;
        int newRightBackTarget;
        int newLeftFrontTarget;
        int newRightFrontTarget;
        //create variables for new targets
        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftFrontTarget = robot.leftFront.getCurrentPosition() + (int)(leftFInches * robot.COUNTS_PER_INCH);
            newRightBackTarget = robot.rightBack.getCurrentPosition() + (int)(rightBInches * robot.COUNTS_PER_INCH);
            newLeftBackTarget = robot.leftBack.getCurrentPosition() + (int)(leftBInches * robot.COUNTS_PER_INCH);
            newRightFrontTarget = robot.rightFront.getCurrentPosition() + (int)(rightFInches * robot.COUNTS_PER_INCH);
            robot.leftFront.setTargetPosition(newLeftFrontTarget);
            robot.rightFront.setTargetPosition(newRightFrontTarget);
            robot.leftBack.setTargetPosition(newLeftBackTarget);
            robot.rightBack.setTargetPosition(newRightBackTarget);
            // Turn On RUN_TO_POSITION
            robot.leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            robot.leftFront.setPower(Math.abs(speed));
            robot.leftBack.setPower(Math.abs(speed));
            robot.rightFront.setPower(Math.abs(speed));
            robot.rightBack.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (robot.leftFront.isBusy() || robot.rightFront.isBusy() && robot.leftBack.isBusy() || robot.rightBack.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d :%7d :%7d", newLeftFrontTarget,  newRightFrontTarget, newLeftBackTarget, newRightBackTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d :%7d :%7d",
                        robot.leftFront.getCurrentPosition(),
                        robot.rightFront.getCurrentPosition(),
                        robot.leftBack.getCurrentPosition(),
                        robot.rightBack.getCurrentPosition());

                telemetry.update();
            }

            // Stop all motion;
            robot.leftFront.setPower(0);
            robot.leftBack.setPower(0);
            robot.rightFront.setPower(0);
            robot.rightBack.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //  sleep(250);   // optional pause after each move
        }
    }
}
