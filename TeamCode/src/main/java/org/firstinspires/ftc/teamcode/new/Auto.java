/*
    # # # # # # # # # # # # # # # # # # 
    # Masters School Robotics         #
    # Written by Matthew Nappo        #
    #            Zach Battleman       #
    # GitHub: @xoreo, @Zanolon        #
    #                                 #
    # Class Auto                      #
    # # # # # # # # # # # # # # # # # # 
*/
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.text.SimpleDateFormat;
import java.util.Date;

@Autonomous

public class Auto extends OpMode {

    static final double COUNTS_PER_MOTOR_REV = 1440;
    static final double DRIVE_GEAR_REDUCTION = 2.0;
    static final double WHEEL_DIAMETER_INCHES = 4.0;
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double TURN_SPEED = 0.3;

    String NAME_deploy_servo = "marker";
    String NAME_claw = "claw";
    String NAME_lift_rotate = "claw_rotate";
    String NAME_lift_rotate_top = "claw_rotate_top";
    String NAME_lift_0 = "drive_claw_left";
    String NAME_lift_1 = "drive_claw_right";
    String NAME_lock_arm = "arm_lock";

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor w0 = null;
    private DcMotor w1 = null;
    private DcMotor w2 = null;
    private DcMotor w3 = null;

    private DcMotor lift_rotate = null;
    private DcMotor lift_rotate_top = null;
    private CRServo lift_0 = null;
    private CRServo lift_1 = null;

    private Servo s_lift_0 = null;
    private Servo s_lift_1 = null;

    private Servo deploy_servo = null;
    private Servo claw = null;
    private Servo lock_arm = null;

    private boolean open = false;
    private double max_speed;

    private DcMotor init_motor(String id) {
        DcMotor m = null;
        m = hardwareMap.get(DcMotor.class, id);
        m.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);
        m.setDirection(DcMotor.Direction.REVERSE);
        return m;
    }

    private Servo init_servo(String id) {
        Servo s = null;
        s = hardwareMap.get(Servo.class, id);
        s.setDirection(Servo.Direction.FORWARD);
        return s;
    }

    private CRServo init_CRservo(String id) {
        CRServo s = null;
        s = hardwareMap.get(CRServo.class, id);
        return s;
    }

    @Override
    public void init() {

        max_speed = 0.3;

        w0 = init_motor("w0");
        w1 = init_motor("w1");
        w2 = init_motor("w2");
        w3 = init_motor("w3");

        deploy_servo = init_servo(NAME_deploy_servo);
        lift_rotate = init_motor(NAME_lift_rotate);
        lift_rotate_top = init_motor(NAME_lift_rotate_top);
        claw = init_servo(NAME_claw);
        lift_0 = init_CRservo(NAME_lift_0);
        lift_1 = init_CRservo(NAME_lift_1);
        lift_0 = hardwareMap.crservo.get(NAME_lift_0);
        lift_1 = hardwareMap.crservo.get(NAME_lift_1);
        lock_arm = init_servo(NAME_lock_arm);

        lock_arm.setPosition(0.0);
        // reset encoders
        w1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        w3.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        
        w1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        w3.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        
        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void init_loop() {}

    public boolean sleep(long secs) {
        this.resetStartTime();
        if (this.getRuntime() > secs) {
            return true;
        }
        return false;
    }

    void rack_and_pinion_down() {
        try {
            long t = System.currentTimeMillis();
            long end = t + 500;
            while (System.currentTimeMillis() < end) {
                lift_0.setPower(-.5);
                lift_1.setPower(-.5);
                // pause to avoid churning
                Thread.sleep(2);
            }

        } catch (Exception e) { }
    }

    public void encoderDrive(double speed,
        double leftInches, double rightInches,
        double timeoutS) {
        int newLeftTarget;
        int newRightTarget;

        // if (opModeIsActive()) {
        newLeftTarget = w1.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
        newRightTarget = w3.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
        w1.setTargetPosition(newLeftTarget);
        w3.setTargetPosition(newRightTarget);

        w1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        w3.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        runtime.reset();
        w1.setPower(Math.abs(speed));
        w3.setPower(Math.abs(speed));

        while ((runtime.seconds() < timeoutS) && (w1.isBusy() && w3.isBusy())) {
            telemetry.addData("Path1", "Running to %7d :%7d", newLeftTarget, newRightTarget);
            telemetry.addData("Path2", "Running at %7d :%7d",
                w1.getCurrentPosition(),
                w3.getCurrentPosition());
            telemetry.update();
        }

        w1.setPower(0);
        w3.setPower(0);

        w1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        w3.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        // }
    }

    @Override
    public void start() {
        lock_arm.setPosition(0.7);
        if (sleep(5)) {
            encoderDrive(TURN_SPEED, 6, -6, 4.0);
            if (sleep(5)) {
                deploy_servo.setPosition(0);
            }
        }

        // lock_arm.setPosition(0.7);
        // encoderDrive(TURN_SPEED, 6, -6, 4.0);
        // deploy_servo.setPosition(0);
    }

    @Override
    public void loop() {

    }

    @Override
    public void stop() {

    }
}