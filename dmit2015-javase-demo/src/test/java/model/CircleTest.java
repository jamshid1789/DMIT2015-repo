package model;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.*;
public class CircleTest {
    @Test
    void area_whenRadiusIs5_ShouldReturnCorrectAnswer(){
        Circle circle = new Circle();
        circle.setRadius(5);

        assertThat(Circle.area()).isCloseTo(78.54, within(0.01));
    }
}
