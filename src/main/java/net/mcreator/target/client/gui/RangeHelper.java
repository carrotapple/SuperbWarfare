package net.mcreator.target.client.gui;

public class RangeHelper {
    // 发射角度（以度为单位），需要根据实际情况修改
    public static double getRange(double thetaDegrees) {
        double initialVelocity = 8.0; // 初始速度 8 m/s
        double thetaRadians = Math.toRadians(thetaDegrees); // 将角度转换为弧度
        double gravity = 0.05; // 重力加速度
        double velocityDecay = 0.99; // 速度衰减系数

        // 计算射程
        return calculateRange(initialVelocity, thetaRadians, gravity, velocityDecay);
    }

    public static double calculateRange(double initialVelocity, double theta, double gravity, double velocityDecay) {
        double vx = initialVelocity * Math.cos(theta); // 水平速度
        double vy = initialVelocity * Math.sin(theta); // 垂直速度

        double x = 0.0; // 水平位置
        double y = 1.0; // 垂直位置

        // 当炮弹还未触地时，继续计算飞行轨迹
        while (y >= 0) {
            // 更新位置
            x += vx;
            y += vy;

            // 更新速度
            vx *= velocityDecay;
            vy = vy * velocityDecay - gravity;

            // 如果炮弹触地，则跳出循环
            if (y < 0) {
                break;
            }
        }

        // 返回最终水平距离
        return x;
    }
}
