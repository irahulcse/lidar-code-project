package thesis.common.csv;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import thesis.common.GlobalConfig;
import thesis.context.data.LidarData;
import thesis.context.data.Point3D;
import thesis.context.data.PointCloud;

import java.io.IOException;
import java.nio.file.Files; 
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class LidardataProducer {

    public static void main(String[] args) {
        KafkaProducer<String, PointCloud> producer = createProducer(); // Change producer type

        // Read data from multiple CSV files
        try {
            List<PointCloud> lidarDataList = readCSVFiles(); // Change list type
            for (PointCloud lidarData : lidarDataList) { // Iterate over LidarData objects
                System.out.println("Data Send" + lidarData);
                ProducerRecord<String, PointCloud> record = new ProducerRecord<>("p1", lidarData);
                producer.send(record);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            producer.close();
        }
    }

    private static KafkaProducer<String, PointCloud> createProducer() {
        // Set up Kafka producer properties
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "192.168.221.213:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, LidarSerial.class.getName());
        return new KafkaProducer<>(properties);
    }
    private static List<PointCloud> readCSVFiles() throws IOException {
        List<PointCloud> lidarDataList = new ArrayList<>();
        // Iterate over CSV files
        for (int i = 1; i <= 2; i++) {
            String filePath = String.format("%s/lidar/%d.csv", GlobalConfig.textInputSource, i);
            List<Point3D> point3DList = readCSVSource(filePath);
            PointCloud pointCloud = new PointCloud(point3DList);

            lidarDataList.add(pointCloud);
        }
        return lidarDataList;
    }

    private static List<Point3D> readCSVSource(String filePath) throws IOException {
        List<Point3D>  trace = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        for (int i = 1; i < lines.size() - 1; i++) {
            String thisLine = lines.get(i);
            String nextLine = lines.get(i + 1);

            double thisX = getX(thisLine);
            double thisY = getY(thisLine);
            double thisZ = getZ(thisLine);
            double nextX = getX(nextLine);
            double nextY = getY(nextLine);
            double nextZ = getZ(nextLine);

            int numberOfIntermediate = getTime(nextLine);
            double incrementX = (nextX - thisX) / numberOfIntermediate;
            double incrementY = (nextY - thisY) / numberOfIntermediate;
            double incrementZ = (nextZ - thisZ) / numberOfIntermediate;

            trace.add(new Point3D(thisX, thisY, thisZ));
            for (int j = 1; j < numberOfIntermediate; j++) {
                trace.add(new Point3D(thisX + j * incrementX, thisY + j * incrementY, thisZ + j * incrementZ));
            }
            if (i == lines.size() - 2) {
                trace.add(new Point3D(nextX, nextY, nextZ));
            }
        }
        return trace;
    }

    public static double getX(String line) {
        return Double.parseDouble(line.split(",")[0].trim());
    }

    public static double getY(String line) {
        return Double.parseDouble(line.split(",")[1].trim());
    }

    public static double getZ(String line) {
        return Double.parseDouble(line.split(",")[2].trim());
    }

    public static int getTime(String line) {
        return Integer.parseInt(line.split(",")[3].trim());
    }
}
