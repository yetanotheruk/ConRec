package uk.yetanother.conrec;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;
import uk.yetanother.conrec.business.ContourGenerator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import static org.junit.Assert.assertNotNull;

public class ContourGeneratorTest {
    public static final String TEST_DATA_FILENAME = "/testdata.csv";

    @Test
    public void generate() throws IOException {
        double[][] data = readCSV();
        assertNotNull(ContourGenerator.generate(data, generateSequence(data[0].length), generateSequence(data.length), new double[]{1}));
    }

    private double[] generateSequence(int length) {
        Collection<Double> sequence = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            sequence.add((double) i);
        }
        return ArrayUtils.toPrimitive((sequence.toArray(new Double[0])));
    }

    public double[][] readCSV() throws IOException {
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