package data;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CandleStick {
    private String symbol;
    private long startTime;
    private long finishTime;
    private long close;
    private long open;
    private long low;
    private long high;

}
