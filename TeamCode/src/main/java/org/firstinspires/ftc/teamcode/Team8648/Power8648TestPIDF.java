package org.firstinspires.ftc.teamcode.Team8648;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.ChassisTestHardwarePushbot;

@TeleOp(name = "8648 RAMMY PIDF Testing", group = "Concept")
@Disabled
public class Power8648TestPIDF extends LinearOpMode {
    Power8648HardwarePushbot robot = new Power8648HardwarePushbot(this);
    private ElapsedTime runtime = new ElapsedTime();
    double currentlfVelocity;
    double currentlbVelocity;
    double currentrfVelocity;
    double currentrbVelocity;

    double maxlfVelocity = 0.0;
    double maxlbVelocity = 0.0;
    double maxrfVelocity = 0.0;
    double maxrbVelocity = 0.0;


    @Override
    public void runOpMode() {
        robot.init(hardwareMap, false);
        robot.leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.leftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {

            robot.leftFront.setPower(1);
            robot.leftBack.setPower(1);
            robot.rightFront.setPower(1);
            robot.rightBack.setPower(1);


            currentlfVelocity = robot.leftFront.getVelocity();
            currentlbVelocity = robot.leftBack.getVelocity();
            currentrfVelocity = robot.rightFront.getVelocity();
            currentrbVelocity = robot.rightBack.getVelocity();


            if (currentlfVelocity > maxlfVelocity) {

                maxlfVelocity = currentlfVelocity;

            }
            if (currentlbVelocity > maxlbVelocity) {

                maxlbVelocity = currentlbVelocity;

            }
            if (currentrfVelocity > maxrfVelocity) {

                maxrfVelocity = currentrfVelocity;

            }
            if (currentrbVelocity > maxrbVelocity) {

                maxrbVelocity = currentrbVelocity;

            }


            telemetry.addData("current left front velocity", currentlfVelocity);
            telemetry.addData("current left back velocity", currentlbVelocity);
            telemetry.addData("current right front velocity", currentrfVelocity);
            telemetry.addData("current right back velocity", currentrbVelocity);

            telemetry.addData("maximum left front velocity", maxlfVelocity);
            telemetry.addData("maximum left back velocity", maxlbVelocity);
            telemetry.addData("maximum  right front velocity", maxrfVelocity);
            telemetry.addData("maximum right back velocity", maxrbVelocity);

            telemetry.update();

        }

    }

}
