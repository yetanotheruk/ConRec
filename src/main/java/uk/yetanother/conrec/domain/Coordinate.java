package uk.yetanother.conrec.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@AllArgsConstructor
public class Coordinate {

    private double x;
    private double y;

}
