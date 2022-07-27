package data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.util.Calendar;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovingAverageRule extends Rule{
    Duration firstWindow;
    Duration secondWindow;
}
