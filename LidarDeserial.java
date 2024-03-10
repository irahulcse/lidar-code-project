package thesis.common.csv;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import thesis.context.data.LidarData;
import thesis.context.data.LocationData;
import thesis.context.data.Point3D;
import thesis.context.data.PointCloud;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;

public class LidarDeserial implements Deserializer<PointCloud> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // No additional configuration is needed
    }

    @Override
    public PointCloud deserialize(String topic, byte[] data) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            return (PointCloud) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new SerializationException("Error deserializing LocationData", e);
        }
    }

    @Override
    public void close() {
        // No resources to release
    }
}
