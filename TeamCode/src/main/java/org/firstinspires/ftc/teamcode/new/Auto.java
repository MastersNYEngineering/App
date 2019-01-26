/*
Copyright 2019 FIRST Tech Challenge Team 14479

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the "Software"), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute,
sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial
portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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

/**
 * This file contains an example of an iterative (Non-Linear) "OpMode".
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 * The names of OpModes appear on the menu of the FTC Driver Station.
 * When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * Remove a @Disabled the on the next line or two (if present) to add this opmode to the Driver Station OpMode list,
 * or add a @Disabled annotation to prevent this OpMode from being added to the Driver Station
 */
@Autonomous

public class Auto extends OpMode {
    /* Declare OpMode members. */

    static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
    (WHEEL_DIAMETER_INCHES * 3.1415);

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

        lock_arm.setPosition(0.0);
        telemetry.addData("Status", "Initialized");
        
        
        // reset encoders
        w1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        w3.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        w1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        w3.setMode(DcMotor.RunMode.RUN_USING_ENCODER);



    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }
    
    void rack_and_pinion_down() {
        try {
            long t= System.currentTimeMillis();
            long end = t+500;
            while(System.currentTimeMillis() < end) {
              lift_0.setPower(-.5);
               lift_1.setPower(-.5);
              // pause to avoid churning
              Thread.sleep( 2 );
            }
            
        }
        catch (Exception e) {
            
        }

    }
    
        void drive_forward() {
        try {
            long t= System.currentTimeMillis();
            long end = t+1000;
            while(System.currentTimeMillis() < end) {
              w1.setPower(1);
                 w3.setPower(1);
              // pause to avoid churning
              Thread.sleep( 2 );
            }
            
        }
        catch (Exception e) {
            
        }
        w1.setPower(0);
                 w3.setPower(0);

    }
    
    
    void wait5() {
        try {
            long t= System.currentTimeMillis();
            long end = t+2000;
            while(System.currentTimeMillis() < end) {
            //   w1.setPower(1);
            //      w3.setPower(1);
              // pause to avoid churning
              Thread.sleep( 2 );
            }
            t = 0;
            
            
        }
        catch (Exception e) {
               
        }
        
    }
    
    
    
    
    
    
    
    
    
    public void encoderDrive(double speed,
                             double leftInches, double rightInches,
                             double timeoutS) {
        int newLeftTarget;
        int newRightTarget;

        // Ensure that the opmode is still active
        // if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftTarget = w1.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newRightTarget = w3.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
            w1.setTargetPosition(newLeftTarget);
            w3.setTargetPosition(newRightTarget);

            // Turn On RUN_TO_POSITION
            w1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            w3.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            w1.setPower(Math.abs(speed));
            w3.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while ((runtime.seconds() < timeoutS) && (w1.isBusy() && w3.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d",
                                            w1.getCurrentPosition(),
                                            w3.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            w1.setPower(0);
            w3.setPower(0);

            // Turn off RUN_TO_POSITION
            w1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            w3.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //  sleep(250);   // optional pause after each move
        // }
    }
    
    
    
    
    
    
    

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        lock_arm.setPosition(0.7);
        // time.sleep()
        // sleep(3000);

        
        // rack_and_pinion_down();
        // sleep(3000);
        
        // drive_forward();
        // sleep(3000);

        double TURN_SPEED = 0.3;
        encoderDrive(TURN_SPEED,   6, -6, 4.0);  // S2: Turn Right 12 Inches with 4 Sec timeout
        
        deploy_servo.setPosition(0);
        
        // time.sleep()
        
        
        


    }

                    public static void sleep(long sleepTime)
                {
                    long wakeupTime = System.currentTimeMillis() + sleepTime;

                    while (sleepTime > 0)
                    {
                        try
                {
                            Thread.sleep(sleepTime);
                        }
                        catch (InterruptedException e)
                        {
                        }
                        sleepTime = wakeupTime - System.currentTimeMillis();
                    }
                }   //sleep

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {


    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {

    }
}
