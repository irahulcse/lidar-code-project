package thesis.common.csv;

import org.apache.kafka.common.serialization.Serializer;
import thesis.context.data.LidarData;
import thesis.context.data.PointCloud;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;

public class LidarSerial implements Serializer<PointCloud> {

        @Override
        public void configure(Map<String, ?> configs, boolean isKey) {
                // No additional configuration is needed
        }

        @Override
        public byte[] serialize(String topic, PointCloud data) {
                try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                     ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                        oos.writeObject(data);
                        return baos.toByteArray();
                } catch (IOException e) {
                        throw new RuntimeException("Error serializing LidarData", e);
                }
        }

        @Override
        public void close() {
                // No resources to release
        }
}
