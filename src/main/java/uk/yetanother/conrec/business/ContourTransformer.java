package uk.yetanother.conrec.business;

import uk.yetanother.conrec.domain.ContourLine;
import uk.yetanother.conrec.domain.ContourPolygon;
import uk.yetanother.conrec.domain.Coordinate;

import java.util.*;

/**
 * ContourTransformer is responsible for attempting to convert ContourLines from the algorithm into ContourPolygons
 * that are easier to work with and drawn in some cases.
 */
public class ContourTransformer {

    private ContourTransformer() {
        // Removing public constructor.
    }

    /**
     * Lines to polygons attempts to convert ContourLines for each level to as many complete ContourPolygons as possible.
     *
     * @param lines the ContourLines to convert
     * @return ContourPolygon's each of which describe the points that make it and the value of the contour it was made for.
     */
    @SuppressWarnings({"squid:S2864", "squid:S3776"})
    // squid:S2864 - KeySet iteration required for polygon building at a contour level at a time.
    // squid:S3776 - Breaking up the method more will reduce the overall readability.
    public static Set<ContourPolygon> linesToPolygons(Set<ContourLine> lines) {
        Set<ContourPolygon> polygons = new HashSet<>();
        Map<Double, Set<ContourLine>> sortedLines = separateContourLineValues(lines);

        for (Double contourValue : sortedLines.keySet()) {
            Set<ContourPolygon> polygonsInProgress = new HashSet<>();

            for (ContourLine line : sortedLines.get(contourValue)) {
                ContourPolygon startMatch = null;
                ContourPolygon endMatch = null;
                for (ContourPolygon polygonInProgress : polygonsInProgress) {
                    if (line.getStart().equals(polygonInProgress.getPoints().get(0)) || line.getStart().equals(polygonInProgress.getPoints().get(polygonInProgress.getPoints().size() - 1))) {
                        startMatch = polygonInProgress;
                    } else if (line.getEnd().equals(polygonInProgress.getPoints().get(0)) || line.getEnd().equals(polygonInProgress.getPoints().get(polygonInProgress.getPoints().size() - 1))) {
                        endMatch = polygonInProgress;
                    }
                }

                if (startMatch == null && endMatch == null) {
                    // Start a new Polygon
                    polygonsInProgress.add(new ContourPolygon(line.getValue(), new ArrayList<>(Arrays.asList(line.getStart(), line.getEnd()))));
                } else if (startMatch != null && endMatch != null) {
                    // We found two polygons to join
                    polygonsInProgress.removeAll(Arrays.asList(startMatch, endMatch));
                    polygonsInProgress.add(joinPolygons(line, startMatch, endMatch));
                } else {
                    // See if we need to append the line to a Polygon in Progress
                    if (startMatch != null) {
                        appendLineToPolygon(line, startMatch);
                    }
                    if (endMatch != null) {
                        appendLineToPolygon(line, endMatch);
                    }
                }
            }

            polygons.addAll(polygonsInProgress);
        }

        return polygons;
    }

    private static void appendLineToPolygon(ContourLine line, ContourPolygon polygon) {
        if (line.getStart().equals(polygon.getPoints().get(0))) {
            // Add line end to polygon start
            polygon.getPoints().add(0, line.getEnd());
        } else if (line.getStart().equals(polygon.getPoints().get(polygon.getPoints().size() - 1))) {
            // Add line end to polygon end
            polygon.getPoints().add(line.getEnd());
        } else if (line.getEnd().equals(polygon.getPoints().get(0))) {
            // Add line start to polygon start
            polygon.getPoints().add(0, line.getStart());
        } else if (line.getEnd().equals(polygon.getPoints().get(polygon.getPoints().size() - 1))) {
            // Add line start to polygon end
            polygon.getPoints().add(line.getStart());
        }
    }

    @SuppressWarnings("squid:S2589")
    // For readability and completeness the boolean condition that is not required will be kept.
    private static ContourPolygon joinPolygons(ContourLine linkLine, ContourPolygon startMatchPolygon, ContourPolygon endMatchPolygon) {
        ContourPolygon newPolygon = new ContourPolygon();
        newPolygon.setValue(startMatchPolygon.getValue());
        boolean startPointAtHead = linkLine.getStart().equals(startMatchPolygon.getPoints().get(0));
        boolean endPointAtHead = linkLine.getEnd().equals(endMatchPolygon.getPoints().get(0));

        if (!startPointAtHead && endPointAtHead) {
            newPolygon.getPoints().addAll(startMatchPolygon.getPoints());
            newPolygon.getPoints().addAll(endMatchPolygon.getPoints());
        } else if (startPointAtHead && endPointAtHead) {
            List<Coordinate> reversedPoints = new ArrayList<>(endMatchPolygon.getPoints());
            Collections.reverse(reversedPoints);
            newPolygon.getPoints().addAll(reversedPoints);
            newPolygon.getPoints().addAll(startMatchPolygon.getPoints());
        } else if (startPointAtHead && !endPointAtHead) {
            newPolygon.getPoints().addAll(endMatchPolygon.getPoints());
            newPolygon.getPoints().addAll(startMatchPolygon.getPoints());
        } else {
            List<Coordinate> reversedPoints = new ArrayList<>(endMatchPolygon.getPoints());
            Collections.reverse(reversedPoints);
            newPolygon.getPoints().addAll(startMatchPolygon.getPoints());
            newPolygon.getPoints().addAll(reversedPoints);
        }
        return newPolygon;
    }

    private static Map<Double, Set<ContourLine>> separateContourLineValues(Set<ContourLine> lines) {
        Map<Double, Set<ContourLine>> sortedLines = new HashMap<>();
        for (ContourLine line : lines) {
            if (sortedLines.containsKey(line.getValue())) {
                sortedLines.get(line.getValue()).add(line);
            } else {
                sortedLines.put(line.getValue(), new HashSet<>(Collections.singletonList(line)));
            }
        }
        return sortedLines;
    }
}
