package uk.yetanother.conrec.testutils;

import uk.yetanother.conrec.domain.ContourLine;
import uk.yetanother.conrec.domain.ContourPolygon;
import uk.yetanother.conrec.domain.Coordinate;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Set;

class ContourDisplayComponent extends JComponent {

    ArrayList<Line2D.Double> lines = new ArrayList<>();
    ArrayList<Path2D.Double> polygons = new ArrayList<>();

    ContourDisplayComponent(int width, int height) {
        super();
        setPreferredSize(new Dimension(width, height));
    }

    public void addLines(Set<ContourLine> contours) {
        for (ContourLine contourLine : contours) {
            lines.add(new Line2D.Double(contourLine.getStart().getX(), contourLine.getStart().getY(),
                    contourLine.getEnd().getX(), contourLine.getEnd().getY()));
        }
        repaint();
    }

    public void addPolygons(Set<ContourPolygon> polygons) {
        for (ContourPolygon polygon : polygons) {
            Path2D.Double path = new Path2D.Double();
            path.moveTo(polygon.getPoints().get(0).getX(), polygon.getPoints().get(0).getY());
            for (Coordinate point : polygon.getPoints()) {
                path.lineTo(point.getX(), point.getY());
            }
            this.polygons.add(path);
        }
        repaint();
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g);
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());
        Dimension d = getPreferredSize();
        g.setColor(Color.black);
        for (Line2D.Double line : lines) {
            g.drawLine(
                    (int) line.getX1(),
                    (int) line.getY1(),
                    (int) line.getX2(),
                    (int) line.getY2()
            );
        }
        for (Path2D.Double polygon : polygons) {
            g2.draw(polygon);
        }

    }
}
