/*
    # # # # # # # # # # # # # # # # # # 
    # Masters School Robotics         #
    # Written by Matthew Nappo        #
    #            Zach Battleman       #
    # GitHub: @xoreo, @Zanolon        #
    #                                 #
    # Class Control                   #
    # # # # # # # # # # # # # # # # # # 
*/

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import java.lang.Math;

@TeleOp(name="Teleop: Control (Square Drive)", group="Iterative Opmode")
public class Control extends OpMode {
    // Declare the Robot class
    private Robot robot;

    // Declare some stuff
    private ElapsedTime runtime = new ElapsedTime();
    double max_speed = 0.3;
    
    @Override
    public void init() {
        // Maybe pass in the telemetry as well
        robot = new Robot(hardwareMap, gamepad1);
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
        runtime.reset();
        init();
        
    }
    
    @Override
    public void loop() {
        Drive();
        FunctionalityLoop();
        telemetry.addData("Run Time", runtime.toString());
    }
    
    // Drive - Drive the robot during the control period
    private void Drive() {
        double[] driveSpeeds = robot.GetDriveSpeeds();
        double turnSpeed = robot.GetTurnSpeed();
        
        robot.w0.setPower(driveSpeeds[0] + turnSpeed);
        robot.w1.setPower(driveSpeeds[1] + turnSpeed);
        robot.w2.setPower(driveSpeeds[2] + turnSpeed);
        robot.w3.setPower(driveSpeeds[3] + turnSpeed);
    }
    
    // FunctionalityLoop - Enable control of all other Robot methods
    private void FunctionalityLoop() {
        robot.DeployMarker();
        robot.LockArm();
        robot.MoveClaw();
        robot.RotateLift();
        robot.DriveLift();
    }

    @Override
    public void stop() {
        robot.Stop();
    }
}