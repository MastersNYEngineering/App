/*
    # # # # # # # # # # # # # # # # # # 
    # Masters School Robotics         #
    # Written by Matthew Nappo,       #
    #            Zach Battleman       #
    # GitHub: @xoreo, @Zanolon        #
    #                                 #
    # Class NewSquareDrive            #
    # # # # # # # # # # # # # # # # # # 
*/

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import java.util.concurrent.TimeUnit;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import java.lang.Math;
import java.util.*;
@TeleOp(name="Main: New Square Drive", group="Iterative Opmode")
public class NewSquareDrive extends OpMode {

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
        // s.setDirection(CRServo.Direction.FORWARD);
        return s;
    }

    @Override
    public void init() {
        // max_speed = 1;
        max_speed = 0.3;
        // max_speed = 0.125;

        w0 = init_motor("w0");
        w1 = init_motor("w1");
        w2 = init_motor("w2");
        w3 = init_motor("w3");

        deploy_servo = init_servo(NAME_deploy_servo);
        lift_rotate = init_motor(NAME_lift_rotate);
        lift_rotate_top= init_motor(NAME_lift_rotate_top);
        claw = init_servo(NAME_claw);
        lift_0 = init_CRservo(NAME_lift_0);
        lift_1 = init_CRservo(NAME_lift_1);

        // s_lift_0 = init_servo(NAME_lift_0);
        // s_lift_1 = init_servo(NAME_lift_1);


        lift_0=hardwareMap.crservo.get(NAME_lift_0);
        lift_1=hardwareMap.crservo.get(NAME_lift_1);
        lock_arm = init_servo(NAME_lock_arm);
    }

    double[] moveHolo() {
        double x_left_joy = gamepad1.left_stick_x;
        double y_left_joy = gamepad1.left_stick_y;
        
        double phi_joy = Math.atan2(y_left_joy, x_left_joy);
        
        double x_left_joy_sq = Math.pow(x_left_joy, 2);
        double y_left_joy_sq = Math.pow(y_left_joy, 2);
        
        double r_joy = Math.sqrt(x_left_joy_sq + y_left_joy_sq);
        
        double speed = max_speed * r_joy;
        
        double alpha_1 = Math.PI / 4;
        double alpha_2 = 3 * Math.PI / 4;
        double alpha_3 = 5 * Math.PI / 4;
        double alpha_4 = 7 * Math.PI / 4;
        
        double theta_1 = alpha_1 - phi_joy;
        double theta_2 = alpha_2 - phi_joy;
        double theta_3 = alpha_3 - phi_joy;
        double theta_4 = alpha_4 - phi_joy;
        
        double w0_power = -speed * Math.sin(theta_1);
        double w1_power = -speed * Math.sin(theta_2);
        double w2_power = -speed * Math.sin(theta_3);
        double w3_power = -speed * Math.sin(theta_4);
        
        telemetry.addData("w0_power", w0_power);
        telemetry.addData("w1_power", w1_power);
        telemetry.addData("w2_power", w2_power);
        telemetry.addData("w3_power", w3_power);
        
        double[] speeds = {
            w0_power,
            w1_power,
            w2_power,
            w3_power
        };

        return speeds;
    }

    double[] moveTractor() {
        double x_left_joy = gamepad1.left_stick_x;
        double y_left_joy = gamepad1.left_stick_y;
        
        // double phi_joy = Math.atan2(y_left_joy, x_left_joy);
        
        // double x_left_joy_sq = Math.pow(x_left_joy, 2);
        // double y_left_joy_sq = Math.pow(y_left_joy, 2);
        
        // double r_joy = Math.sqrt(x_left_joy_sq + y_left_joy_sq);
        
        // double speed = max_speed * r_joy;
        
        // double alpha_1 = Math.PI / 4;
        // double alpha_2 = 3 * Math.PI / 4;
        // double alpha_3 = 5 * Math.PI / 4;
        // double alpha_4 = 7 * Math.PI / 4;
        
        // double theta_1 = alpha_1 - phi_joy;
        // double theta_2 = alpha_2 - phi_joy;
        // double theta_3 = alpha_3 - phi_joy;
        // double theta_4 = alpha_4 - phi_joy;
        
        double w0_power = y_left_joy;
        double w2_power = y_left_joy;

        
        telemetry.addData("w0_power", w0_power);
        telemetry.addData("w1_power", w1_power);
        telemetry.addData("w2_power", w2_power);
        telemetry.addData("w3_power", w3_power);
        
        double[] speeds = {
            w0_power,
            w1_power,
            w2_power,
            w3_power
        };

        return speeds;
    }

    double turn() {
        double x_right_joy = gamepad1.right_stick_x;
        double speed = Range.clip(x_right_joy, -1.0, 1.0) * max_speed;

        return -speed;
    }
    
    boolean marker_bool = false;
    void deploy_marker() {
        double current_position = deploy_servo.getPosition();
        telemetry.addData("servo position",current_position);
        if (gamepad1.x) {
            if (marker_bool == true) {
                if (current_position >= 0) {
                    deploy_servo.setPosition(0);
                }
                marker_bool = false;
            } else if (marker_bool == false) {
                if (current_position <= 0) {
                    deploy_servo.setPosition(.5);
                }
                marker_bool = true;
            }
            
        }
    }

    boolean arm_func_bool = false;
    void lock_arm_func() {
        double current_position = lock_arm.getPosition();
        telemetry.addData("ARM LOCK POSITION", current_position);
        if (gamepad1.y) {
            telemetry.addData("y button", "pressed");
            if (arm_func_bool == true) {
                if (current_position >.1) {
                    lock_arm.setPosition(0.0);
                }
                arm_func_bool = false;
            } else if (arm_func_bool == false) {
                telemetry.addData("bla", "blahhhhh");
                if (current_position < .1) {
                    lock_arm.setPosition(.8);
                }
                arm_func_bool = true;
            }
            
        }
    }

    void move_claw() {
        double current_position = claw.getPosition();
        
        if (gamepad1.b && !open) {
            claw.setPosition(1);
            open=true;
        }
        if (gamepad1.a && open) {
            claw.setPosition(-1);
            open = false;
        }
    }

    void rotate_lift() {
        // telemetry.addData("pressed", gamepad1.pressed(gamepad1.right_trigger));
        telemetry.addData("direct", gamepad1.right_trigger);
        
        double right = 1;
        double left = -1;
        if (gamepad1.left_trigger>0) {
            lift_rotate.setPower(right);
            lift_rotate_top.setPower(right);
        } else {
            lift_rotate.setPower(0);
            lift_rotate_top.setPower(0);
        }
        
        if (gamepad1.left_bumper) {
            lift_rotate.setPower(left);
            lift_rotate_top.setPower(left);
            telemetry.addData("thing",lift_rotate_top.getPower());
        } else {
            lift_rotate.setPower(0);
            lift_rotate_top.setPower(0);
        }
    }

           
            //range of CRservo is from -.93 to .88, the midpoint is -.025... NANI?!?!?!
            void drive_lift() {
                if (gamepad1.right_bumper) {
                    telemetry.addData("right bumper", "pressed");
                    lift_0.setPower(-.93);
                    lift_1.setPower(-.93);
                    // s_lift_0.setPosition(1);
                    // s_lift_1.setPosition(1);
                }
                if (gamepad1.right_trigger>0) {
                    telemetry.addData("left bumper", "pressed");
                    lift_0.setPower(.88);
                    lift_1.setPower(.88);
                    // s_lift_0.setPosition(0);
                    // s_lift_1.setPosition(0);
                }
                else{
                    lift_0.setPower(-0.025);
                    lift_1.setPower(-0.025);
                }
                }
            

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
        // deploy_servo.setPosition(0);
        lock_arm.setPosition(.8);
        
        runtime.reset();
        init();
    }

    @Override
    public void loop() {
        telemetry.addData("MOTORRRR", deploy_servo.getPosition());
        double[] moveTractor = moveTractor();
        // double[] moveHolo = moveHolo();
        double turn = turn();
        w0.setPower(move[0] + turn);
        w1.setPower(move[1] + turn);
        w2.setPower(move[2] + turn);
        w3.setPower(move[3] + turn);
        
        deploy_marker();
        rotate_lift();
        move_claw();
        lock_arm_func();
        drive_lift();
        telemetry.addData("thingerino", lock_arm.getPosition());
        telemetry.addData("SERVO", deploy_servo.getPosition());
        telemetry.addData("rrrrrr", claw.getPosition());
        telemetry.addData("Run Time", runtime.toString());
    }

    @Override
    public void stop() {
        w0 = null;
        w1 = null;
        w2 = null;
        w3 = null;
    }
}
