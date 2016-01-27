clear;
clc;
grid on;
close all;
format longg;

%We assume that the wheel speed is constant.
%DISCLAIMER: be careful.

ball_track_circumference = 0.8 * pi; %to be computed accurately.
wheel_rotor_circumference = 0.6 * pi;

%Raw input from the sensors. They share the same origin.
balls_lp = [2871 3582 4420 5460 6633 7939 9369 10888 12547 14339 16311 18425]*0.001;

new_origin = balls_lp(1);

balls_lp = balls_lp - new_origin;
balls_lp_diff = diff(balls_lp);

mod_lp_ball = polyfit(1:length(balls_lp_diff), balls_lp_diff, 1);

a_b = mod_lp_ball(1); %slope of the ball model
b_b = mod_lp_ball(2); %intercept

balls_time_idx = [0 cumsum(balls_lp_diff)];

%First method to compute the speed curve. Philippe's method
x_1 = (balls_time_idx(1:end-1)+balls_time_idx(2:end))/2;
speeds_1 = ball_track_circumference./balls_lp_diff;

%Second method to compute the speed curve. Isabelle's method
speed_function = @(x) 2*ball_track_circumference/sqrt(8*a_b*(x-b_b)+(2*b_b+a_b)^2); %add b here.
speed_function_shifted_b = @(x) 2*ball_track_circumference/sqrt(8*a_b*x+(2*b_b+a_b)^2); %no need to add b.
speed_function_wheel_rotor = @(x) 0.524;

x_2 = linspace(x_1(1), x_1(end), 1000); % generate a grid of 1000 points.
speeds_2 = [];
speeds_rotor = []
for i = x_2
    speeds_2(end+1) = speed_function_shifted_b(i);
    speeds_rotor(end+1) = speed_function_wheel_rotor(i);
end

hold on;
plot(x_1, speeds_1, 'LineWidth', 2);
plot(x_2, speeds_2, 'LineWidth', 2);
plot(x_2, speeds_rotor, 'LineWidth', 2);
hold off;

%First revolution.
integral(speed_function, b_b, balls_lp_diff(1)+b_b, 'ArrayValued', true)

%Not very useful here but can speed up the computations.
primitive_speed_function = @(x) ball_track_circumference*0.5/a_b*(sqrt((a_b+2*b_b)^2-4*a_b*(2*b_b-2*x))-(a_b+2*b_b));

%Quick check if both quantities are equal.
integral(speed_function, 1, pi,'ArrayValued',true)
primitive_speed_function(pi)-primitive_speed_function(1)

inv_speed_function = @(x) (4*ball_track_circumference^2-x^2*(2*b_b+a_b)^2)/(8*a_b*x^2)+b_b;

%Quick check if both quantities are equal.
inv_speed_function(speed_function(3))
inv_speed_function(speed_function(pi))

%Lets make a prediction now.
cutoff_speed = 1.10; %to be measured accurately.
time_cutoff = inv_speed_function(cutoff_speed);
time_left = time_cutoff - balls_lp(end);
ref_time_for_prediction = balls_lp(end);
rem_dist = integral(speed_function_wheel_rotor, ref_time_for_prediction, ref_time_for_prediction+time_left, 'ArrayValued', true);

%Lets compute the angle now.
revolutions_left_float = rem_dist/wheel_rotor_circumference;
angle = (revolutions_left_float-floor(revolutions_left_float))*360; %in degrees.

disp(angle);

angle = mod(angle + 18/37*360, 360);

disp(angle);

%Once we have the angle, we compute the phase(number on the wheel) between
%the wheel and the mark at we add this angle to this phase to find the
%corresponding number. After that we had 18 pockets more because it takes
%time for the ball to stop. We don't do it here but it has been done in Java.