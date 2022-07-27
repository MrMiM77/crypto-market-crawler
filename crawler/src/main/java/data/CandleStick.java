package data;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CandleStick {
    private String symbol;
    private long startTime;
    private long finishTime;
    private double close;
    private double open;
    private double low;
    private double high;

}
