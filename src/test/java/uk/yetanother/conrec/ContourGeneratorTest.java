package uk.yetanother.conrec;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;
import uk.yetanother.conrec.business.ContourGenerator;
import uk.yetanother.conrec.business.ContourTransformer;
import uk.yetanother.conrec.domain.ContourLine;
import uk.yetanother.conrec.domain.ContourPolygon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

import static org.junit.Assert.*;

public class ContourGeneratorTest {
    public static final String TEST_DATA_FILENAME = "/testdata.csv";

    @Test
    public void generateClassic() throws IOException {
        assertNotNull(oneContourTestData());
    }

    @Test
    public void generatePolygon() throws IOException {
        double[][] data = readCSV();
        Set<ContourPolygon> polygons = ContourGenerator.generatePolygons(data, generateSequence(data[0].length), generateSequence(data.length), new double[]{1});
        assertEquals(16, polygons.size());
    }

    @Test
    public void transform() throws IOException {
        Set<ContourLine> lines = oneContourTestData();
        Set<ContourPolygon> polygons = ContourTransformer.linesToPolygons(lines);
        assertTrue(polygons.size() < lines.size());
        assertEquals(16, polygons.size());
    }

    public Set<ContourLine> oneContourTestData() throws IOException {
        double[][] data = readCSV();
        return ContourGenerator.generateClassic(data, generateSequence(data[0].length), generateSequence(data.length), new double[]{1});
    }

    private double[] generateSequence(int length) {
        Collection<Double> sequence = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            sequence.add((double) i * 20);
        }
        return ArrayUtils.toPrimitive((sequence.toArray(new Double[0])));
    }

    private double[][] readCSV() throws IOException {
        try (BufferedReader datafile = new BufferedReader(new InputStreamReader(Objects.requireNonNull(this.getClass().getResourceAsStream(TEST_DATA_FILENAME))))) {
            double[][] lines = new double[20][20];
            int yPtr = 19;
            for (String line = datafile.readLine(); line != null; line = datafile.readLine()) {
                int xPtr = 0;
                for (String datapoint : line.split(",")) {
                    lines[xPtr][yPtr] = (Double.parseDouble(datapoint));
                    xPtr++;
                }
                yPtr--;
            }
            return lines;
        }
    }
}