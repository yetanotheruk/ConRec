package uk.yetanother.conrec.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContourPolygon {

    private double value;
    private List<Coordinate> points = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ContourPolygon)) {
            return false;
        } else {
            ContourPolygon other = (ContourPolygon) o;
            if (this.getPoints() == null && other.getPoints() == null) {
                return true;
            } else if (this.getPoints() == null || other.getPoints() == null) {
                return false;
            }
            return this.getPoints().size() == other.getPoints().size() && this.points.containsAll(other.getPoints()) && other.getPoints().containsAll(this.getPoints());
        }
    }

}
