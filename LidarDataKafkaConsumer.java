package thesis.common.csv;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import thesis.context.data.PointCloud;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class LidarDataKafkaConsumer {

    List<PointCloud> lidarDataList = new ArrayList<>();

    public LidarDataKafkaConsumer() {
        this.lidarDataList = new ArrayList<>();
    }
    public List<PointCloud> consumeLidarData() {


        KafkaConsumer<String, PointCloud> consumer = createConsumer();
        consumer.subscribe(Collections.singletonList("p1"));

        try {
            while (true) {
                ConsumerRecords<String, PointCloud> records = consumer.poll(Duration.ofMillis(100));
                if (!records.isEmpty()) {
                    for (ConsumerRecord<String, PointCloud> record : records) {
                        PointCloud lidarData = record.value();
                        lidarDataList.add(lidarData);
                        System.out.println(lidarDataList);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            consumer.close();
        }
        return lidarDataList;
    }
    private KafkaConsumer<String, PointCloud> createConsumer() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.221.213:9092");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "lidar_consumer_group-111");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, LidarDeserial.class.getName()); // Use custom deserializer
        return new KafkaConsumer<>(properties);
    }

    public static void main(String[] args) {
        LidarDataKafkaConsumer ld = new LidarDataKafkaConsumer();
        ld.consumeLidarData();
    }
}
