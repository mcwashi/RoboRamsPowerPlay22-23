package org.firstinspires.ftc.teamcode.Team8648;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Power8648HardwarePushbot {

    public DcMotor leftFront = null;
    public DcMotor rightFront = null;
    public DcMotor leftBack = null;
    public DcMotor rightBack = null;

    public DcMotor leftLinear = null;
    public DcMotor rightLinear = null;

    public Servo leftClaw = null;
    public Servo rightClaw = null;



    public static final double     COUNTS_PER_MOTOR_REV    = 312 ;    // eg: TETRIX Motor Encoder
    public static final double     DRIVE_GEAR_REDUCTION    = 1.0;     // This is < 1.0 if geared UP
    public static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    public static final double     LINEAR_DIAMETER_INCHES  = 1.5 ;     // For figuring circumference
    public static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    public static final double     COUNTS_PER_LINEAR_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (LINEAR_DIAMETER_INCHES * 3.1415);
    public static final double     DRIVE_SPEED             = 0.7;
    public static final double     TURN_SPEED              = 0.6;
    public static final double     LINEAR_SPEED              = 0.6;

    public  static double            SLIDELIFTSPEED                  = 1.0; //
    public static  double            SLIDELOWERSPEED                 = -0.4; // use the LOAD instead of down. Zero pushes wheels off the mat
    public static final double      SLIDE_LEVEL_1                   = -1; // inches
    public static final double      SLIDE_LEVEL_2                   = -13; // inches
    public static final double      SLIDE_LEVEL_3                   = -25; // inches
    public static final double      SLIDE_LEVEL_4                   = -38; // inches

    private static final double     TICKS_PER_MOTOR_REV             = 384.5; // goBilda 1150  //312 RPM  537.7
    private static final double     PULLEY_DIA                      = 40; // milimeters
    private static final double     SLIDE_LIFT_DISTANCE_PER_REV     = PULLEY_DIA * Math.PI / 25.4; //  lift = circimference of the pulley converted to inches
    private static final double     TICKS_PER_LIFT_IN               = TICKS_PER_MOTOR_REV / SLIDE_LIFT_DISTANCE_PER_REV; // 109 and change


    public double  targetHeight;





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



    public void  setSlideLevel1(){

        targetHeight = ( SLIDE_LEVEL_1 );
        liftToTargetHeight(targetHeight,3);
        //servo.setPosition(0);
    }

    public void setSlideLevel2(){
        targetHeight = ( SLIDE_LEVEL_2);
        liftToTargetHeight(targetHeight,3);
        //servo.setPosition(0);
    }

    public void setSlideLevel3(){
        targetHeight = ( SLIDE_LEVEL_3);
        liftToTargetHeight(targetHeight,10);
        //servo.setPosition(1.0);
    }

    public void setSlideLevel4(){
        targetHeight = ( SLIDE_LEVEL_4);
        liftToTargetHeight(targetHeight,3);
        //servo.setPosition(1.0);
    }

    public Power8648HardwarePushbot(LinearOpMode opmode){
        this.opmode = opmode;
    }
    /*public Slide_Trainer(LinearOpMode opmode) {
        this.opmode = opmode;

    }

     */
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
