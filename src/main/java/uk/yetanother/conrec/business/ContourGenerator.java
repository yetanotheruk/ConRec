package uk.yetanother.conrec.business;

import uk.yetanother.conrec.domain.ContourLine;
import uk.yetanother.conrec.domain.ContourLineType;
import uk.yetanother.conrec.domain.Coordinate;
import uk.yetanother.conrec.domain.TriangleData;

import java.util.HashSet;
import java.util.Set;

import static uk.yetanother.conrec.domain.ContourLineType.*;

public class ContourGenerator {

    private static final ContourLineType[][][] CASTAB =
            {
                    {{NO_LINE, NO_LINE, S23_AND_S31}, {NO_LINE, V2_AND_V3, V2_AND_S31}, {S12_AND_S23, V3_AND_S12, S31_AND_S12}},
                    {{NO_LINE, V3_AND_V1, V1_AND_S23}, {V1_AND_V2, V3_AND_V1, V1_AND_V2}, {V1_AND_S23, V3_AND_V1, NO_LINE}},
                    {{S31_AND_S12, V3_AND_S12, S12_AND_S23}, {V2_AND_S31, V2_AND_V3, NO_LINE}, {S23_AND_S31, NO_LINE, NO_LINE}}
            };
    private static final int[] IM = {0, 1, 1, 0};
    private static final int[] JM = {0, 0, 1, 1};
    private static final int NUMBER_OF_TRIANGLES = 4;

    private ContourGenerator() {
        // Hiding public constructor
    }

    /**
     * Given data points for both the X and Y axis as well as the surface this function will attempt to draw lines for
     * each contour level requested.
     *
     * @param data - A two-dimensional array containing the data to be contoured.
     * @param xValues - A array containing all the horizontal coordinates of each sample point.
     * @param yValues - A array containing all the vertical coordinates of each sample point.
     * @param contourLevels - A array of each contour value to evaluate and generate lines for.
     * @return ContourLine's each of which describe the start and end points of the line and the value of the contour it was made for.
     */
    public static Set<ContourLine> generate(double[][] data, double[] xValues, double[] yValues, double[] contourLevels) {
        Set<ContourLine> foundLines = new HashSet<>();

        for (int y = (yValues.length - 2); y >= 0; y--) {
            for (int x = 0; x < xValues.length - 1; x++) {
                double lowestDataPoint = Math.min(Math.min(data[x][y], data[x][y + 1]), Math.min(data[x + 1][y], data[x + 1][y + 1]));
                double highestDataPoint = Math.max(Math.max(data[x][y], data[x][y + 1]), Math.max(data[x + 1][y], data[x + 1][y + 1]));
                TriangleData triangleData = new TriangleData(lowestDataPoint, highestDataPoint);

                if (highestDataPoint >= contourLevels[0] && lowestDataPoint <= contourLevels[contourLevels.length - 1]) {
                    for (double contourLevel : contourLevels) {
                        foundLines.addAll(processContourLevel(data, xValues, yValues, y, x, triangleData, contourLevel));
                    }
                }
            }
        }
        return foundLines;
    }

    private static Set<ContourLine> processContourLevel(double[][] data, double[] xValues, double[] yValues, int y, int x, TriangleData triangleData, double contourLevel) {
        Set<ContourLine> contourLines = new HashSet<>();
        if (contourLevel >= triangleData.getLowestDataPoint() && contourLevel <= triangleData.getHighestDataPoint()) {
            buildTriangleData(data, xValues, yValues, y, x, triangleData, contourLevel);
            processTrianglesAndRecordLine(contourLines, triangleData, contourLevel);
        }
        return  contourLines;
    }

    private static void buildTriangleData(double[][] data, double[] xValues, double[] yValues, int y, int x, TriangleData triangleData, double contourLevel) {
        for (int m = NUMBER_OF_TRIANGLES; m >= 0; m--) {
            if (m > 0) {
                triangleData.getH()[m] = data[x + IM[m - 1]][y + JM[m - 1]] - contourLevel;
                triangleData.getXh()[m] = xValues[x + IM[m - 1]];
                triangleData.getYh()[m] = yValues[y + JM[m - 1]];
            } else {
                triangleData.getH()[0] = 0.25 * (triangleData.getH()[1] + triangleData.getH()[2] + triangleData.getH()[3] + triangleData.getH()[4]);
                triangleData.getXh()[0] = 0.5 * (xValues[x] + xValues[x + 1]);
                triangleData.getYh()[0] = 0.5 * (yValues[y] + yValues[y + 1]);
            }
            if (triangleData.getH()[m] > 0.0) {
                triangleData.getSh()[m] = 1;
            } else if (triangleData.getH()[m] < 0.0) {
                triangleData.getSh()[m] = -1;
            } else
                triangleData.getSh()[m] = 0;
        }
    }

    private static void processTrianglesAndRecordLine(Set<ContourLine> generatedLines, TriangleData triangleData, double contourLevel) {
        //
        // Note: at this stage the relative heights of the corners and the
        // centre are in the h array, and the corresponding coordinates are
        // in the xh and yh arrays. The centre of the box is indexed by 0
        // and the 4 corners by 1 to 4 as shown below.
        // Each triangle is then indexed by the parameter m, and the 3
        // vertices of each triangle are indexed by parameters m1,m2,and
        // m3.
        // It is assumed that the centre of the box is always vertex 2
        // though this is important only when all 3 vertices lie exactly on
        // the same contour level, in which case only the side of the box
        // is drawn.
        //
        //
        //      vertex 4 +-------------------+ vertex 3
        //               | \               / |
        //               |   \    m=3    /   |
        //               |     \       /     |
        //               |       \   /       |
        //               |  m=4    X   m=2   |       the centre is vertex 0
        //               |       /   \       |
        //               |     /       \     |
        //               |   /    m=1    \   |
        //               | /               \ |
        //      vertex 1 +-------------------+ vertex 2
        //
        //
        //
        //               Scan each triangle in the box
        //
        int trianglePoint1;
        final int trianglePoint2 = 0; // Triangle Point 2 is always the center of the square and always 0.
        int trianglePoint3;
        for (int triangleNumber = 1; triangleNumber <= NUMBER_OF_TRIANGLES; triangleNumber++) {
            trianglePoint1 = triangleNumber;
            if (triangleNumber != NUMBER_OF_TRIANGLES) {
                trianglePoint3 = triangleNumber + 1;
            } else {
                trianglePoint3 = 1;
            }

            double startPointX;
            double startPointY;
            double endPointX;
            double endPointY;
            switch (CASTAB[triangleData.getSh()[trianglePoint1] + 1][triangleData.getSh()[trianglePoint2] + 1][triangleData.getSh()[trianglePoint3] + 1]) {
                case V1_AND_V2:
                    startPointX = triangleData.getXh()[trianglePoint1];
                    startPointY = triangleData.getYh()[trianglePoint1];
                    endPointX = triangleData.getXh()[trianglePoint2];
                    endPointY = triangleData.getYh()[trianglePoint2];
                    break;
                case V2_AND_V3:
                    startPointX = triangleData.getXh()[trianglePoint2];
                    startPointY = triangleData.getYh()[trianglePoint2];
                    endPointX = triangleData.getXh()[trianglePoint3];
                    endPointY = triangleData.getYh()[trianglePoint3];
                    break;
                case V3_AND_V1:
                    startPointX = triangleData.getXh()[trianglePoint3];
                    startPointY = triangleData.getYh()[trianglePoint3];
                    endPointX = triangleData.getXh()[trianglePoint1];
                    endPointY = triangleData.getYh()[trianglePoint1];
                    break;
                case V1_AND_S23:
                    startPointX = triangleData.getXh()[trianglePoint1];
                    startPointY = triangleData.getYh()[trianglePoint1];
                    endPointX = findIntersection(trianglePoint2, trianglePoint3, triangleData.getH(), triangleData.getXh());
                    endPointY = findIntersection(trianglePoint2, trianglePoint3, triangleData.getH(), triangleData.getYh());
                    break;
                case V2_AND_S31:
                    startPointX = triangleData.getXh()[trianglePoint2];
                    startPointY = triangleData.getYh()[trianglePoint2];
                    endPointX = findIntersection(trianglePoint3, trianglePoint1, triangleData.getH(), triangleData.getXh());
                    endPointY = findIntersection(trianglePoint3, trianglePoint1, triangleData.getH(), triangleData.getYh());
                    break;
                case V3_AND_S12:
                    startPointX = triangleData.getXh()[trianglePoint3];
                    startPointY = triangleData.getYh()[trianglePoint3];
                    endPointX = findIntersection(trianglePoint1, trianglePoint2, triangleData.getH(), triangleData.getXh());
                    endPointY = findIntersection(trianglePoint1, trianglePoint2, triangleData.getH(), triangleData.getYh());
                    break;
                case S12_AND_S23:
                    startPointX = findIntersection(trianglePoint1, trianglePoint2, triangleData.getH(), triangleData.getXh());
                    startPointY = findIntersection(trianglePoint1, trianglePoint2, triangleData.getH(), triangleData.getYh());
                    endPointX = findIntersection(trianglePoint2, trianglePoint3, triangleData.getH(), triangleData.getXh());
                    endPointY = findIntersection(trianglePoint2, trianglePoint3, triangleData.getH(), triangleData.getYh());
                    break;
                case S23_AND_S31:
                    startPointX = findIntersection(trianglePoint2, trianglePoint3, triangleData.getH(), triangleData.getXh());
                    startPointY = findIntersection(trianglePoint2, trianglePoint3, triangleData.getH(), triangleData.getYh());
                    endPointX = findIntersection(trianglePoint3, trianglePoint1, triangleData.getH(), triangleData.getXh());
                    endPointY = findIntersection(trianglePoint3, trianglePoint1, triangleData.getH(), triangleData.getYh());
                    break;
                case S31_AND_S12:
                    startPointX = findIntersection(trianglePoint3, trianglePoint1, triangleData.getH(), triangleData.getXh());
                    startPointY = findIntersection(trianglePoint3, trianglePoint1, triangleData.getH(), triangleData.getYh());
                    endPointX = findIntersection(trianglePoint1, trianglePoint2, triangleData.getH(), triangleData.getXh());
                    endPointY = findIntersection(trianglePoint1, trianglePoint2, triangleData.getH(), triangleData.getYh());
                    break;
                case NO_LINE:
                default:
                    continue;
            }
            // Record the found line
            generatedLines.add(new ContourLine(contourLevel, new Coordinate(startPointX, startPointY), new Coordinate(endPointX, endPointY)));
        }
    }

    private static double findIntersection(int p1, int p2, double[] h, double[] xh) {
        return (h[p2] * xh[p1] - h[p1] * xh[p2]) / (h[p2] - h[p1]);
    }
}
