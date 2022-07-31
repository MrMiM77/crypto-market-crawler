package collector.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import data.CandleStick;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class CandleStickSerializer implements Serializer {
    private final static Logger logger = LogManager.getLogger(CandleStickSerializer.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public byte[] serialize(String topic, Object o) {
        CandleStick data = (CandleStick) o;
        try {
            if (data == null){
                logger.warn("null received");
                return null;
            }
            logger.info("serializing : " + data);
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new SerializationException("Error when serializing MessageDto to byte[]");
        }
    }
    @Override public void close() {
    }

}

