package com.example.demo.utils;

public class DistanceUtil {

    private static final double EARTH_RADIUS = 6378137;//赤道半径（单位m）

    /**
     * 转化为弧度（radian）
     */
    private static double radian(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 基于余弦定理求两经纬度距离
     *
     * @param longitude1 第一点的经度
     * @param latitude1 第一点的纬度
     * @param longitude2 第二点的经度
     * @param latitude2 第二点的纬度
     * @return 返回的距离，单位m
     */
    public static double getDistance(double longitude1, double latitude1, double longitude2, double latitude2) {
        double radLon1 = radian(longitude1);
        double radLon2 = radian(longitude2);

        double radLat1 = radian(latitude1);
        double radLat2 = radian(latitude2);

        if (radLat1 < 0) {
            radLat1 = Math.PI / 2 + Math.abs(radLat1);// south
        }

        if (radLat1 > 0) {
            radLat1 = Math.PI / 2 - Math.abs(radLat1);// north
        }
        if (radLon1 < 0) {
            radLon1 = Math.PI * 2 - Math.abs(radLon1);// west
        }

        if (radLat2 < 0) {
            radLat2 = Math.PI / 2 + Math.abs(radLat2);// south
        }

        if (radLat2 > 0) {
            radLat2 = Math.PI / 2 - Math.abs(radLat2);// north
        }

        if (radLon2 < 0) {
            radLon2 = Math.PI * 2 - Math.abs(radLon2);// west
        }

        double x1 = EARTH_RADIUS * Math.cos(radLon1) * Math.sin(radLat1);
        double y1 = EARTH_RADIUS * Math.sin(radLon1) * Math.sin(radLat1);
        double z1 = EARTH_RADIUS * Math.cos(radLat1);

        double x2 = EARTH_RADIUS * Math.cos(radLon2) * Math.sin(radLat2);
        double y2 = EARTH_RADIUS * Math.sin(radLon2) * Math.sin(radLat2);
        double z2 = EARTH_RADIUS * Math.cos(radLat2);

        double d = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) + (z1 - z2) * (z1 - z2));
        //余弦定理求夹角
        double theta = Math.acos((EARTH_RADIUS * EARTH_RADIUS + EARTH_RADIUS * EARTH_RADIUS - d * d) / (2 * EARTH_RADIUS * EARTH_RADIUS));
        double distance = theta * EARTH_RADIUS;
        return distance;
    }

    /**
     * 根据经纬度计算两点间的距离
     *
     * @param longitude1 第一个点的经度
     * @param latitude1 第一个点的纬度
     * @param longitude2 第二个点的经度
     * @param latitude2 第二个点的纬度
     * @return 返回距离，单位m
     */
    public static double getDistance2(double longitude1, double latitude1, double longitude2, double latitude2) {
        // 纬度
        double lat1 = Math.toRadians(latitude1);
        double lat2 = Math.toRadians(latitude2);
        // 经度
        double lng1 = Math.toRadians(longitude1);
        double lng2 = Math.toRadians(longitude2);
        // 纬度之差
        double a = lat1 - lat2;
        // 经度之差
        double b = lng1 - lng2;
        // 计算两点距离的公式
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(b / 2), 2)));
        // 弧长乘地球半径，返回单位：m
        s =  s * EARTH_RADIUS;
        return s;
    }

    public static void main(String[] args) {
        double d = getDistance(112.506575, 34.630274, 112.494942, 34.62919);
        System.out.println(d);
        double d2 = getDistance2(112.506575, 34.630274, 112.494942, 34.62919);
        System.out.println(d2);
    }

}
