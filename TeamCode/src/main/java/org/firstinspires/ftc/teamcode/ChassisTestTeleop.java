package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.OLDCODE.Frenzy8648HardwarePushbot;

@TeleOp(name="Chassis Test Teleop", group="Pushbot")
@Disabled
public class ChassisTestTeleop extends LinearOpMode {
    ChassisTestHardwarePushbot robot           = new ChassisTestHardwarePushbot();
    @Override
    public void runOpMode() {
        robot.init(hardwareMap, true);
        telemetry.addData("Say", "Hello Driver");
        telemetry.update();
        waitForStart();
        if (isStopRequested()) return;
        while (opModeIsActive()) {
           /* double leftPower;
            double rightPower;
            double drive = -gamepad1.left_stick_y;
            double turn  =  gamepad1.right_stick_x;
            double slide = gamepad2.right_stick_y;
            leftPower    = Range.clip(drive + turn, -1.0, 1.0) ;
            rightPower   = Range.clip(drive - turn, -1.0, 1.0) ;

            robot.leftFront.setPower(leftPower);
            robot.rightFront.setPower(rightPower);
            robot.leftBack.setPower(leftPower);
            robot.rightBack.setPower(rightPower);

            robot.slideMotor.setPower(slide);
            */
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

            if (gamepad1.dpad_up) {
                robot.leftFront.setPower(0.5);
                robot.rightFront.setPower(0.5);
                robot.leftBack.setPower(0.5);
                robot.rightBack.setPower(0.5);
            }
            if (gamepad1.dpad_down) {
                robot.leftFront.setPower(-0.5);
                robot.rightFront.setPower(-0.5);
                robot.leftBack.setPower(-0.5);
                robot.rightBack.setPower(-0.5);
            }
            if (gamepad1.dpad_left) {
                robot.leftFront.setPower(-0.5);
                robot.rightFront.setPower(0.5);
                robot.leftBack.setPower(-0.5);
                robot.rightBack.setPower(0.5);
            }
            if (gamepad1.dpad_right) {
                robot.leftFront.setPower(0.5);
                robot.rightFront.setPower(-0.5);
                robot.leftBack.setPower(0.5);
                robot.rightBack.setPower(-0.5);
            }
        }
    }
}
