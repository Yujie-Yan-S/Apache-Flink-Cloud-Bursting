package operator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class ControllerProcessFunction extends ProcessAllWindowFunction<String, Tuple2<String, Boolean>, TimeWindow> {

    private static final int MEASURE_INTERVAL_MS = 450;
    private static final int INPUT_THRESHOLD = 2000;
    private static final double CPU_THRESHOLD = 0.06;

    private long lastMeasureTime;
    private int messageCount;
    boolean shouldOffload = false;

    public ControllerProcessFunction() {
        lastMeasureTime = 0;
        messageCount = 0;
    }

    @Override
    public void process(Context context, Iterable<String> values, Collector<Tuple2<String, Boolean>> out) throws Exception {
        long currentTime = System.currentTimeMillis();
        Random rand = new Random();
        messageCount++;

        if (currentTime - lastMeasureTime >= MEASURE_INTERVAL_MS) {
            String job_id = MetricUtilities.getjobid();
            Double inputRate = MetricUtilities.getInputRate(job_id, getRuntimeContext().getTaskName());
            Long busyTimeMs = MetricUtilities.getBusyTime(job_id, getRuntimeContext().getTaskName());

            Double busyTimeRatio = Math.max((double) (busyTimeMs/1000), 0.8);
            System.out.println("busy time ratio" + busyTimeRatio);

            double offloading_ratio = rand.nextDouble()*inputRate;
            if(busyTimeRatio > CPU_THRESHOLD) {
                shouldOffload = true;
            } else {
                if(offloading_ratio < INPUT_THRESHOLD) {
                    shouldOffload = true;
                } else {
                    shouldOffload = false;
                }
            }
            messageCount = 0;
            lastMeasureTime = currentTime;
            for(String value:values) {
                out.collect(new Tuple2<>(value, shouldOffload));
            }
        } else {
            for(String value:values) {
                out.collect(new Tuple2<>(value, shouldOffload));
            }
        }
    }
}
