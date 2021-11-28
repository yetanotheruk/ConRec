package uk.yetanother.conrec.testutils;

import lombok.SneakyThrows;
import uk.yetanother.conrec.ContourGeneratorTest;
import uk.yetanother.conrec.business.ContourTransformer;
import uk.yetanother.conrec.domain.ContourLine;
import uk.yetanother.conrec.domain.ContourPolygon;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

class DrawingContourDataUtility {

    public static void main(String[] args) {
        Runnable r = new Runnable() {
            @SneakyThrows
            public void run() {
                Set<ContourLine> lines = new ContourGeneratorTest().oneContourTestData();
                ContourDisplayComponent contourLinesDisplay = new ContourDisplayComponent(400, 400);
                contourLinesDisplay.addLines(lines);

                Set<ContourPolygon> polygons = ContourTransformer.linesToPolygons(lines);
                ContourDisplayComponent contourPolygonsDisplay = new ContourDisplayComponent(400, 400);
                contourPolygonsDisplay.addPolygons(polygons);

                JPanel jPanel = new JPanel(new GridLayout(1, 0));
                jPanel.add(contourLinesDisplay);
                jPanel.add(contourPolygonsDisplay);
                JOptionPane.showMessageDialog(null, jPanel);
            }
        };
        SwingUtilities.invokeLater(r);
    }
}
