package uk.yetanother.conrec.domain;

import lombok.Data;

@Data
public class TriangleData {

    private double[] h;
    private int[] sh;
    private double[] xh;
    private double[] yh;
    private double lowestDataPoint;
    private double highestDataPoint;

    public TriangleData(double lowestDataPoint, double highestDataPoint) {
        h = new double[5];
        sh = new int[5];
        xh = new double[5];
        yh = new double[5];
        this.lowestDataPoint = lowestDataPoint;
        this.highestDataPoint = highestDataPoint;
    }
}
