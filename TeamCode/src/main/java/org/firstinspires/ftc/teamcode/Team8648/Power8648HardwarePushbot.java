package org.firstinspires.ftc.teamcode.Team8648;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Power8648HardwarePushbot {

    public DcMotorEx leftFront = null;
    public DcMotorEx rightFront = null;
    public DcMotorEx leftBack = null;
    public DcMotorEx rightBack = null;

    public DcMotorEx leftLinear = null;
    public DcMotorEx rightLinear = null;

    public Servo leftClaw = null;
    public Servo rightClaw = null;

    public BNO055IMU imu         = null;


    public static final double     COUNTS_PER_MOTOR_REV    = 537.7 ;    // eg: TETRIX Motor Encoder
    public static final double     DRIVE_GEAR_REDUCTION    = 1.0;     // This is < 1.0 if geared UP
    public static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    public static final double     LINEAR_DIAMETER_INCHES  = 1.5 ;     // For figuring circumference
    public static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    public static final double     COUNTS_PER_LINEAR_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (LINEAR_DIAMETER_INCHES * 3.1415);
    public static final double     DRIVE_SPEED             = 0.4;
    public static final double     TURN_SPEED              = 0.5;
    public static final double     LINEAR_SPEED              = 0.6;

    public  static double            SLIDELIFTSPEED                  = 1.0; //
    public static  double            SLIDELOWERSPEED                 = -0.4; // use the LOAD instead of down. Zero pushes wheels off the mat
    public static final double      SLIDE_LEVEL_1                   = -1; // inches
    public static final double      SLIDE_LEVEL_2                   = -13; // inches
    public static final double      SLIDE_LEVEL_3                   = -25; // inches
    public static final double      SLIDE_LEVEL_4                   = -38; // inches

    private static final double     TICKS_PER_MOTOR_REV             = 537.7; // goBilda 1150  //312 RPM  537.7
    private static final double     PULLEY_DIA                      = 40; // milimeters
    private static final double     SLIDE_LIFT_DISTANCE_PER_REV     = PULLEY_DIA * Math.PI / 25.4; //  lift = circimference of the pulley converted to inches
    public static final double     TICKS_PER_LIFT_IN               = TICKS_PER_MOTOR_REV / SLIDE_LIFT_DISTANCE_PER_REV; // 109 and change


    public double  leftLinearTargetHeight;
    public double  rightLinearTargetHeight;


    LinearOpMode opmode;
    HardwareMap hwMap           =  null;
    private ElapsedTime runtime  = new ElapsedTime();

    public enum SlideTrainerState {
        UNKNOWN,
        IDLE,
        LOW,
        MID,
        HIGH,
        EXTRA_HIGH
    }


    public double getRightSlidePos(){
        double slidePos;
        slidePos = rightLinear.getCurrentPosition()/ TICKS_PER_LIFT_IN; //returns in inches
        return  slidePos;
    }
    public double getLeftSlidePos(){
        double slidePos;
        slidePos = leftLinear.getCurrentPosition()/ TICKS_PER_LIFT_IN; //returns in inches
        return  slidePos;
    }


    public Power8648HardwarePushbot(LinearOpMode opmode){
        this.opmode = opmode;
    }
    /*public Slide_Trainer(LinearOpMode opmode) {
        this.opmode = opmode;

    }

     */

    public void resetLinear(){
        leftLinear.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightLinear.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftLinear.setTargetPosition(0);
        rightLinear.setTargetPosition(0);

    }
    public void init(HardwareMap ahwMap, boolean inTeleOp){
        hwMap = ahwMap;
        leftFront  = hwMap.get(DcMotorEx.class, "left_front");
        rightFront = hwMap.get(DcMotorEx.class, "right_front");
        leftBack  = hwMap.get(DcMotorEx.class, "left_back");
        rightBack = hwMap.get(DcMotorEx.class, "right_back");

        leftLinear  = hwMap.get(DcMotorEx.class, "left_linear");
        rightLinear = hwMap.get(DcMotorEx.class, "right_linear");



        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);
        rightFront.setDirection(DcMotorSimple.Direction.FORWARD);
        rightBack.setDirection(DcMotorSimple.Direction.FORWARD);

        leftLinear.setDirection(DcMotorSimple.Direction.REVERSE);
        rightLinear.setDirection(DcMotorSimple.Direction.FORWARD);


        leftFront.setVelocityPIDFCoefficients(1.05, 0.105, 0, 10.5);
        leftBack.setVelocityPIDFCoefficients(1.07, 0.107, 0, 10.7);
        rightFront.setVelocityPIDFCoefficients(1.03, 0.103, 0, 10.3);
        rightBack.setVelocityPIDFCoefficients(1.06, 0.106, 0, 10.6);

        leftFront.setPositionPIDFCoefficients(5.0);
        leftBack.setPositionPIDFCoefficients(5.0);
        rightFront.setPositionPIDFCoefficients(5.0);
        rightBack.setPositionPIDFCoefficients(5.0);


        leftBack.setPower(0);
        leftFront.setPower(0);
        rightBack.setPower(0);
        rightFront.setPower(0);

        rightLinear.setPower(0);
        leftLinear.setPower(0);

        leftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        rightLinear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftLinear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //rightLinear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //leftLinear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftClaw = hwMap.get(Servo.class, "left_claw");
        rightClaw = hwMap.get(Servo.class, "right_claw");


        if (!inTeleOp) {

            leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        } else {
            // for InTeleop we don't need encoders because driver controls
            leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            leftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        }


    }
    public void liftToTargetHeight(double height, double timeoutS) {

        int newTargetHeight;


        // Ensure that the opmode is still active
        if (opmode.opModeIsActive()) {

            // Determine new target lift height in ticks based on the current position.
            // When the match starts the current position should be reset to zero.

            newTargetHeight = (int) (height * TICKS_PER_LIFT_IN);
            // Set the target now that is has been calculated
            rightLinear.setTargetPosition(newTargetHeight);
            leftLinear.setTargetPosition(newTargetHeight);

            // Turn On RUN_TO_POSITION
            rightLinear.setPower(Math.abs(SLIDELIFTSPEED));
            leftLinear.setPower(Math.abs(SLIDELIFTSPEED));

            // reset the timeout time and start motion.
            runtime.reset();
            rightLinear.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftLinear.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        }
    }
}
