# Yet Another ConRec Java Implementation
ConRec is an algorithm that aims to represent contours by using a regular triangular mesh. 

This is a Java Implementation based on the work by Paul Bourke as published on http://paulbourke.net/papers/conrec/.     

## Getting Started

ConRec is provided as a pre-built maven library that requires no additional libraries to work.

```java
...
        Set<ContourLine> contourlines =  ContourGenerator.generate(
                data, xValues, yValues, contourLevels);
...
```
Input Parameters;
1. `double[][] data` - A two-dimensional array containing the data to be contoured.
2. `double[] xValues` - A array containing all the horizontal coordinates of each sample point.
3. `double[] yValues` - A array containing all the vertical coordinates of each sample point.
4. `double[] contourLevels` - A array of each contour value to evaluate and generate lines for.

Returns `Set<ContourLine>` each of which describe the start and end points of the line and the value of the coutour it was made for.

### Maven

To start using ConRec simply add the following dependency to your project POM.

```xml
<dependency>
	<groupId>uk.yetanother</groupId>
	<artifactId>ConRec</artifactId>
	<version>1.0.0</version>
</dependency>
```

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management

## Authors

* **Ashley Baker** - (https://github.com/ashleycbaker)

## License

This project is licensed under Apache License, Version 2.0 - see the [LICENSE.md](LICENSE.md) file for details
