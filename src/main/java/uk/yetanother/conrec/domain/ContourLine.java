package uk.yetanother.conrec.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@AllArgsConstructor
public class ContourLine {

    private double value;
    private Coordinate start;
    private Coordinate end;

}
