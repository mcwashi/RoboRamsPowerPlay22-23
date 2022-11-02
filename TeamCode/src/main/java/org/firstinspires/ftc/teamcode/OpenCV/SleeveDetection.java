package org.firstinspires.ftc.teamcode.OpenCV;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class SleeveDetection extends OpenCvPipeline {
    /*
    YELLOW  = Parking Left
    CYAN    = Parking Middle
    MAGENTA = Parking Right
     */

    Telemetry telemetry;
    public enum ParkingPosition {
        LEFT,
        CENTER,
        RIGHT
    }

    // TOPLEFT anchor point for the bounding box
    private static Point SLEEVE_TOPLEFT_ANCHOR_POINT = new Point(145, 168);

    // Width and height for the bounding box
    public static int REGION_WIDTH = 30;
    public static int REGION_HEIGHT = 50;

    // Lower and upper boundaries for colors
    private static final Scalar
            lower_yellow_bounds  = new Scalar(200, 200, 0),
            upper_yellow_bounds  = new Scalar(255, 255, 130),
            lower_cyan_bounds    = new Scalar(0, 200, 200),
            upper_cyan_bounds    = new Scalar(150, 255, 255),
            lower_magenta_bounds = new Scalar(170, 0, 170),
            upper_magenta_bounds = new Scalar(255, 60, 255);

    // Color definitions
    private final Scalar
            YELLOW  = new Scalar(255, 255, 0),
            CYAN    = new Scalar(0, 255, 255),
            MAGENTA = new Scalar(255, 0, 255);

    // Percent and mat definitions
    private double yelPercent, cyaPercent, magPercent;
    private Mat yelMat = new Mat(), cyaMat = new Mat(), magMat = new Mat(), blurredMat = new Mat(), kernel = new Mat();

    // Anchor point definitions
    Point sleeve_pointA = new Point(
            SLEEVE_TOPLEFT_ANCHOR_POINT.x,
            SLEEVE_TOPLEFT_ANCHOR_POINT.y);
    Point sleeve_pointB = new Point(
            SLEEVE_TOPLEFT_ANCHOR_POINT.x + REGION_WIDTH,
            SLEEVE_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT);

    // Running variable storing the parking position
    private volatile ParkingPosition position = ParkingPosition.LEFT;

    @Override
    public Mat processFrame(Mat input) {
        // Noise reduction
        Imgproc.blur(input, blurredMat, new Size(5, 5));
        blurredMat = blurredMat.submat(new Rect(sleeve_pointA, sleeve_pointB));

        Imgproc.cvtColor(input, yelMat, Imgproc.COLOR_RGB2HSV);
        Imgproc.cvtColor(input, cyaMat, Imgproc.COLOR_RGB2HSV);
        Imgproc.cvtColor(input, magMat, Imgproc.COLOR_RGB2HSV);// converting RGB to HSV color scheme
        // Normal HSV has HUE values from 0 to 360. Open CV uses 0 to 180,
        // Normal HSV has Saturation and Value from 0 to 100. Open CV scales that 100 from 0 to 255
        // HUE's for reference
        // Red 0
        // Orange   25 deg or 12.5 in Open VC
        // Yellow   60 or 30 in Open CV
        // Green    120 or 60 in open CV
        // Cyan     180 or 90 in Open CV
        // Blue     240 or 120 in Open CV
        // Purple   280 or 140 in Open CV
        // Pink     300 or 150 in open CV
        // Red      360 or 180 in openCV
        // Apply Morphology
        kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
        Imgproc.morphologyEx(blurredMat, blurredMat, Imgproc.MORPH_CLOSE, kernel);
        
        // Gets channels from given source mat
        Core.inRange(blurredMat, lower_yellow_bounds, upper_yellow_bounds, yelMat);
        Core.inRange(blurredMat, lower_cyan_bounds, upper_cyan_bounds, cyaMat);
        Core.inRange(blurredMat, lower_magenta_bounds, upper_magenta_bounds, magMat);

        // Gets color specific values
        yelPercent = Core.countNonZero(yelMat);
        cyaPercent = Core.countNonZero(cyaMat);
        magPercent = Core.countNonZero(magMat);

        // Calculates the highest amount of pixels being covered on each side
        double maxPercent = Math.max(yelPercent, Math.max(cyaPercent, magPercent));

        // Checks all percentages, will highlight bounding box in camera preview
        // based on what color is being detected
        if (maxPercent == yelPercent) {
            position = ParkingPosition.LEFT;
            Imgproc.rectangle(
                    input,
                    sleeve_pointA,
                    sleeve_pointB,
                    YELLOW,
                    2
            );
        } else if (maxPercent == cyaPercent) {
            position = ParkingPosition.CENTER;
            Imgproc.rectangle(
                    input,
                    sleeve_pointA,
                    sleeve_pointB,
                    CYAN,
                    2
            );
        } else if (maxPercent == magPercent) {
            position = ParkingPosition.RIGHT;
            Imgproc.rectangle(
                    input,
                    sleeve_pointA,
                    sleeve_pointB,
                    MAGENTA,
                    2
            );
        }
        
        // Memory cleanup
        blurredMat.release();
        yelMat.release();
        cyaMat.release();
        magMat.release();
        kernel.release();

        return input;
    }

    // Returns an enum being the current position where the robot will park
    public ParkingPosition getPosition() {
        return position;
    }

}
