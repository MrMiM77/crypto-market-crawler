package evaluator.kafka;

import collector.kafka.CandleStickSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import data.CandleStick;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.Map;


public class CandleStickDeserializer implements Deserializer<CandleStick> {
    private final static Logger logger = LogManager.getLogger(CandleStickSerializer.class);
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public CandleStick deserialize(String topic, byte[] data) {
        try {
            if (data == null){
                logger.warn("null received at deserializing");
                return null;
            }
            logger.info("Deserializing...");
            return objectMapper.readValue(new String(data, StandardCharsets.UTF_8), CandleStick.class);
        } catch (Exception e) {
            throw new SerializationException("Error when deserializing byte[] to MessageDto");
        }
    }

    @Override
    public void close() {
    }
}