package uk.yetanother.conrec.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Contour line that describes the start and end coordinate and the Contour Level it is for.
 */
@Data
@EqualsAndHashCode
@AllArgsConstructor
public class ContourLine {

    private double value;
    private Coordinate start;
    private Coordinate end;

}
