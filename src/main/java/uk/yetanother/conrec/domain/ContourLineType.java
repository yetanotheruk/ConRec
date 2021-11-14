package uk.yetanother.conrec.domain;

public enum ContourLineType {

    /**
     *      vertex 4 +-------------------+ vertex 3
     *               | \               / |
     *               |   \           /   |
     *               |     \       /     |
     *               |       \   /       |
     *               |         X         |
     *               |       /   \       |
     *               |     /       \     |
     *               |   /           \   |
     *               | /               \ |
     *      vertex 1 +-------------------+ vertex 2
     */

    NO_LINE, // No line to draw
    V1_AND_V2, // Line between vertices 1 and 2
    V2_AND_V3, // Line between vertices 2 and 3
    V3_AND_V1, // Line between vertices 3 and 1
    V1_AND_S23, // Line between vertex 1 and side 2-3
    V2_AND_S31, // Line between vertex 2 and side 3-1
    V3_AND_S12, //  Line between vertex 3 and side 1-2
    S12_AND_S23, // Line between sides 1-2 and 2-3
    S23_AND_S31, // Line between sides 2-3 and 3-1
    S31_AND_S12; // Line between sides 3-1 and 1-2

}
