package org.firstinspires.ftc.teamcode.Team8648;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Team8648.Power8648HardwarePushbot;


@TeleOp(name="8648 RAMMY Teleop", group="Pushbot")
@Disabled
public class Power8648Teleop extends LinearOpMode {
    Power8648HardwarePushbot robot           = new Power8648HardwarePushbot();
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

            robot.leftLinear.setPower(-gamepad2.right_stick_y);
            robot.rightLinear.setPower(-gamepad2.right_stick_y);

            if(gamepad2.dpad_up)
                robot.rightLinear.setTargetPosition(1);
                robot.leftLinear.setTargetPosition(-1);


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
            if(gamepad2.a){
                robot.leftClaw.setPosition(1);
                robot.rightClaw.setPosition(1);
            }
            if(gamepad2.b){
                robot.leftClaw.setPosition(0);
                robot.rightClaw.setPosition(0);

            }

        }
    }
}
