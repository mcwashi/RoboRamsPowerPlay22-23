package org.firstinspires.ftc.teamcode.Team9788;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Team8648.Power8648HardwarePushbot;


@TeleOp(name="9788 RAMMY Teleop", group="Pushbot")
@Disabled
public class Power9788Teleop extends LinearOpMode {
    Power8648HardwarePushbot robot           = new Power8648HardwarePushbot(this);
    Power8648HardwarePushbot.SlideTrainerState slideTrainerState = Power8648HardwarePushbot.SlideTrainerState.UNKNOWN;
    double slideError = 0.5;
    double pos;
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        robot.init(hardwareMap, true);
        robot.rightLinear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.leftLinear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        telemetry.addData("Say", "Hello Driver");
        telemetry.update();

        waitForStart();
        if (isStopRequested()) return;
        while (opModeIsActive()) {

            double y = -gamepad1.left_stick_y; // Remember, this is reversed!
            double x = gamepad1.left_stick_x;
            double rx = gamepad1.right_stick_x;
            double lf, lb, rf, rb;

            lf = (y + x + rx); //forward + turn right + strafe right
            lb = (y - x + rx);
            rf = (y - x - rx);
            rb = (y + x - rx);

            if (Math.abs(lf) > 1 || Math.abs(lb) > 1 || Math.abs(rf) > 1 || Math.abs(rb) > 1) {
                double max = 0;
                max = Math.max(Math.abs(lf), Math.abs(lb));
                max = Math.max(Math.abs(rf), max);
                max = Math.max(Math.abs(rb), max);

                // scales output if y + x + rx >1
                lf /= max;
                lb /= max;
                rf /= max;
                rb /= max;

            }

            robot.leftFront.setPower(lf);
            robot.rightFront.setPower(rf);
            robot.leftBack.setPower(lb);
            robot.rightBack.setPower(rb);


            if (gamepad2.dpad_up) {
                encoderLinear(1.0, 38.0, 38.0, 10);

            }
            if (gamepad2.dpad_right) {
                encoderLinear(1.0, 27.5, 27.5, 10);

            }
            if (gamepad2.dpad_left) {
                //encoderLinear(1.0, 16.5, 16.5, 10);
                LowLinear();
            }
            if (gamepad2.left_bumper) {
                encoderLinear(1.0, 2.5, 2.5, 10);

            }
            if (gamepad2.dpad_down) {
                resetLinear();

            }

            /*if(gamepad2.dpad_up)
                robot.rightLinear.setTargetPosition(13);
                robot.leftLinear.setTargetPosition(-13);

             */


            if(gamepad1.dpad_up){
                robot.leftFront.setPower(0.5);
                robot.rightFront.setPower(0.5);
                robot.leftBack.setPower(0.5);
                robot.rightBack.setPower(0.5);
            }
            if(gamepad1.dpad_down){
                robot.leftFront.setPower(-0.5);
                robot.rightFront.setPower(-0.5);
                robot.leftBack.setPower(-0.5);
                robot.rightBack.setPower(-0.5);
            }
            if(gamepad1.dpad_left){
                robot.leftFront.setPower(-0.5);
                robot.rightFront.setPower(0.5);
                robot.leftBack.setPower(-0.5);
                robot.rightBack.setPower(0.5);
            }
            if(gamepad1.dpad_right){
                robot.leftFront.setPower(0.5);
                robot.rightFront.setPower(-0.5);
                robot.leftBack.setPower(0.5);
                robot.rightBack.setPower(-0.5);
            }
            if(gamepad2.a) {
                robot.leftClaw.setPosition(0.40);
                robot.rightClaw.setPosition(0.20);}
            else{
                robot.leftClaw.setPosition(0.3);
                robot.rightClaw.setPosition(0.3);
            }

            telemetry.addData(" Right Target Position", robot.rightLinearTargetHeight);
            telemetry.addData("Left Target Position", robot.leftLinearTargetHeight);
            telemetry.addData("Actual Right Position","%.1f", robot.getRightSlidePos());
            telemetry.addData("Actual Left Position","%.1f", robot.getRightSlidePos());

            telemetry.addData("Right Motor Power","%.1f", robot.rightLinear.getPower());
            telemetry.addData("Left Motor Power","%.1f", robot.leftLinear.getPower());

            telemetry.update();
        }
    }
    public void encoderLinear(double speed,
                              double leftInches, double rightInches,
                              double timeoutS) {
        int newLeftLinearTarget;
        int newRightLinearTarget;
        //create variables for new targets
        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newRightLinearTarget = robot.rightLinear.getCurrentPosition() + (int)(rightInches);
            newLeftLinearTarget = robot.leftLinear.getCurrentPosition() + (int)(leftInches);

            //Sets the target position
            robot.leftLinear.setTargetPosition(newLeftLinearTarget);
            robot.rightLinear.setTargetPosition(newRightLinearTarget);

            robot.leftLinearTargetHeight = newLeftLinearTarget;
            robot.rightLinearTargetHeight= newRightLinearTarget;
            // Turn On RUN_TO_POSITION
            robot.leftLinear.setPower(Math.abs(speed));
            robot.rightLinear.setPower(Math.abs(speed));

            //robot.leftLinear.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            //robot.rightLinear.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            //robot.leftLinear.setPower(Math.abs(speed));
            //robot.rightLinear.setPower(Math.abs(speed));

            robot.leftLinear.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rightLinear.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            telemetry.addData(" Right Target Position", robot.rightLinearTargetHeight);
            telemetry.addData("Left Target Position", robot.leftLinearTargetHeight);
            telemetry.addData("", robot.leftLinearTargetHeight);

            telemetry.addData("Actual Right Position","%.1f", robot.getRightSlidePos());
            telemetry.addData("Actual Left Position","%.1f", robot.getRightSlidePos());

            telemetry.addData("Right Motor Power","%.1f", robot.rightLinear.getPower());
            telemetry.addData("Left Motor Power","%.1f", robot.leftLinear.getPower());

            telemetry.update();
            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (robot.leftLinear.isBusy() && robot.rightLinear.isBusy())) {


                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", newRightLinearTarget, newLeftLinearTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d",
                        robot.rightLinear.getCurrentPosition(),
                        robot.leftLinear.getCurrentPosition());

                telemetry.update();
            }

            /*
           if(gamepad2.dpad_down){
               robot.leftLinear.setTargetPosition(0);
               robot.rightLinear.setTargetPosition(0);

               sleep(3000);

               robot.leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
               robot.leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

               robot.leftLinear.setPower(0);
               robot.rightLinear.setPower(0);

           }

             */
            // Stop all motion;
            //robot.leftLinear.setPower(0);
            //robot.rightLinear.setPower(0);

            //robot.leftLinear.setPower(0);
            //robot.rightLinear.setPower(0);

            //robot.leftLinear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            //robot.rightLinear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            // Turn off RUN_TO_POSITION
            // robot.leftLinear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            // robot.rightLinear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            // robot.leftLinear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            // robot.rightLinear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            /*
            sleep(3000);
            robot.leftLinear.setTargetPosition(0);
            robot.rightLinear.setTargetPosition(0);

            sleep(3000);

            robot.leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            robot.leftLinear.setPower(0);
            robot.rightLinear.setPower(0);

             */


            // optional pause after each move
        }
    }
    public void resetLinear(){
        robot.leftLinear.setTargetPosition(0);
        robot.rightLinear.setTargetPosition(0);


        if((robot.getRightSlidePos() == 0) && (robot.getLeftSlidePos() == 0)){
            robot.leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            robot.leftLinear.setPower(0);
            robot.rightLinear.setPower(0);
        }
        /*
        robot.leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        robot.leftLinear.setPower(0);
        robot.rightLinear.setPower(0);

         */
    }
    public void LowLinear() {
        robot.rightLinear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.leftLinear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.leftLinear.setTargetPosition((int)(1));
        robot.rightLinear.setTargetPosition((int)(1));

        robot.leftLinear.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rightLinear.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.leftLinear.setPower(Math.abs(1));
        robot.rightLinear.setPower(Math.abs(1));


    }
}
