package org.firstinspires.ftc.teamcode.Team9788;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Team8648.Power8648HardwarePushbot;


@TeleOp(name="9788 RAMMY Teleop One-Controller Test", group="Pushbot")
//@Disabled
public class Power9788TeleopOneControllerTest extends LinearOpMode {
    Power9788HardwarePushbot robot           = new Power9788HardwarePushbot(this);
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        robot.init(hardwareMap, true);
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


            if(gamepad2.y){

            }
            if (gamepad1.dpad_up) {
                liftToTargetHeight(0.2 ,10,10);
            }
            if (gamepad1.dpad_right) {
            }
            if (gamepad1.dpad_left) {
            }
            if (gamepad1.left_bumper) {

            }
            if (gamepad1.dpad_down) {

            }

            if(gamepad1.a) {
                robot.leftClaw.setPosition(0);
                robot.rightClaw.setPosition(0);}
            else{
                robot.leftClaw.setPosition(1);
                robot.rightClaw.setPosition(1);
                }

            telemetry.addData(" Right Target Position", robot.rightLinearTargetHeight);
            telemetry.addData("Left Target Position", robot.leftLinearTargetHeight);
            telemetry.addData("Actual Right Position in Inches","%.1f", robot.getRightSlidePos());
            telemetry.addData("Actual Left Position in Inches","%.1f", robot.getLeftSlidePos());
            telemetry.addData("Actual Left Position", robot.leftLinear.getCurrentPosition());
            telemetry.addData("Actual Right Position", robot.rightLinear.getCurrentPosition());

            telemetry.addData("Right Motor Power","%.1f", robot.rightLinear.getPower());
            telemetry.addData("Left Motor Power","%.1f", robot.leftLinear.getPower());
            telemetry.addData("Lift inch", robot.TICKS_PER_LIFT_IN);


            telemetry.update();
        }
    }
    public void liftToTargetHeight(double speed, double height, double timeoutS) {

        int newTargetHeight;


        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target lift height in ticks based on the current position.
            // When the match starts the current position should be reset to zero.

            newTargetHeight = (int) (height);
            // Set the target now that is has been calculated
            robot.rightLinear.setTargetPosition(newTargetHeight);
            robot.leftLinear.setTargetPosition(newTargetHeight);

            robot.leftLinearTargetHeight = newTargetHeight;
            robot.rightLinearTargetHeight = newTargetHeight;


            // Turn On RUN_TO_POSITION
            robot.rightLinear.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.leftLinear.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            robot.rightLinear.setPower(speed);
            robot.leftLinear.setPower(speed);


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

}
