package org.firstinspires.ftc.teamcode.Team9788;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Power9788HardwarePushbot {

    public DcMotor leftFront = null;
    public DcMotor rightFront = null;
    public DcMotor leftBack = null;
    public DcMotor rightBack = null;

    public DcMotor leftLinear = null;
    public DcMotor rightLinear = null;

    public Servo leftClaw = null;
    public Servo rightClaw = null;

    public BNO055IMU imu         = null;


    public static final double     COUNTS_PER_MOTOR_REV    = 537.7 ;    // eg: TETRIX Motor Encoder
    public static final double     DRIVE_GEAR_REDUCTION    = 1.0;     // This is < 1.0 if geared UP
    public static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    public static final double     LINEAR_DIAMETER_INCHES  = 1.5 ;     // For figuring circumference
    public static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    public static final double     DRIVE_SPEED             = 0.3;
    public static final double     TURN_SPEED              = 0.6;

    public  static double           SLIDELIFTSPEED                  = -1.0; //
    public static  double           SLIDELOWERSPEED                 = -0.4; // use the LOAD instead of down. Zero pushes wheels off the mat
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


    public Power9788HardwarePushbot(LinearOpMode opmode){
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
        leftFront  = hwMap.get(DcMotor.class, "left_front");
        rightFront = hwMap.get(DcMotor.class, "right_front");
        leftBack  = hwMap.get(DcMotor.class, "left_back");
        rightBack = hwMap.get(DcMotor.class, "right_back");

        leftLinear  = hwMap.get(DcMotor.class, "left_linear");
        rightLinear = hwMap.get(DcMotor.class, "right_linear");



        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);
        rightFront.setDirection(DcMotorSimple.Direction.FORWARD);
        rightBack.setDirection(DcMotorSimple.Direction.FORWARD);

        leftLinear.setDirection(DcMotorSimple.Direction.REVERSE);
        rightLinear.setDirection(DcMotorSimple.Direction.FORWARD);


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

        rightLinear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftLinear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightLinear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftLinear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


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
}
