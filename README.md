# Lidar Code Project:

# Integration;

https://stackoverflow.com/questions/16607444/how-to-serialize-object-to-json 

First, add the Jackson dependency to your pom.xml:

<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.12.3</version> <!-- Use the latest version -->
</dependency>

# Question: Serialisation of Java Object to JSON?. Possible or not?.

Answer: The Gson library you mentioned is a Java library used for converting Java Objects into their JSON representation. It can also be used to convert a JSON string to an equivalent Java object. However, GSON cannot be used in a Node.js environment as it's not a JavaScript library.

The challenge here is that the data you're sending from your Java application is serialized Java objects. These are binary data and can't be directly converted to JSON or JavaScript objects in Node.js.

Deserializing Java objects in Node.js would require running a JVM in your Node.js process which can be complex and may not always work correctly due to differences between the JVM and Node.js runtime environments. The `node-java` library provides this functionality but it has many nuances and can be difficult to work with, especially when dealing with complex objects and classpaths.

Therefore, the recommended approach is to modify your Java producer to send data in a format that can be easily consumed in any language, like JSON. This way, you won't need to worry about deserializing Java objects in Node.js. You'll just receive JSON strings, which you can parse using `JSON.parse()` to get JavaScript objects.

If modifying the Java producer is not an option, another approach could be to create a new Java application that consumes the Kafka messages, transforms them to JSON, and then sends them to your Node.js server over HTTP or WebSocket. But this would be more complex than just modifying the existing Java producer to send JSON.

Let me know if you have any questions or if there's anything else you'd like to know!

# Issues of using Node.js and JVM environments for serialisation and deserialisation:

const java = require('java');

java.classpath.pushDir('/path/to/your/java/classes'); // Add the directory containing your Java classes to the classpath

// Define the fully qualified names of the classes you'll use
const ObjectInputStream = java.import('java.io.ObjectInputStream');
const ByteArrayInputStream = java.import('java.io.ByteArrayInputStream');

const consumer = kafka.consumer({ groupId: 'mygroup2' });

async function run() {
await consumer.connect();
await consumer.subscribe({ topic: 'p1', fromBeginning: true });

await consumer.run({
eachMessage: async ({ topic, partition, message }) => {
try {
const bais = new ByteArrayInputStream(message.value);
const ois = new ObjectInputStream(bais);
const pointCloud = ois.readObjectSync(); // Use readObjectSync because readObject is asynchronous in node-java

        console.log('Message received', pointCloud);
        io.emit('lidar-data', pointCloud); // Emit the Lidar data to clients
      } catch (err) {
        console.error('Error deserializing message value', err);
      }
    },

});
}

run().catch(console.error);
This code tries to deserialize the Kafka messages as Java objects using ObjectInputStream. Please replace /path/to/your/java/classes with the actual path to the directory containing your compiled Java classes (PointCloud.class, Point3D.class, etc.).

However, this is a very basic example and might not work out-of-the-box for your specific use case. The node-java library has many nuances and can be difficult to work with, especially when dealing with complex objects and classpaths. You might encounter issues related to class not found exceptions, incompatible types, JVM options, multithreading, etc.

Also, keep in mind that even if you manage to deserialize the Java objects in Node.js, you still need to convert them to JavaScript objects to use them in your React application. This conversion process can also be complex and error-prone.

Given these potential issues, I would strongly recommend considering other alternatives, such as modifying your Java producer to send JSON strings instead of serialized Java objects. This approach would be much simpler, more reliable, and more efficient. If you decide to go with this approach, I'd be happy to guide you on how to modify your Java producer code.

# Formal of data being sent from the Kafka

[PointCloud{points=[(72.0, 0.0, 0.0), (72.75, 1.25, 1.0), (73.5, 2.5, 2.0), (74.25, 3.75, 3.0), (75.0, 5.0, 4.0), (75.0, 13.75, 4.5), (75.0, 22.5, 5.0), (75.0, 31.25, 5.5), (75.0, 40.0, 6.0), (75.0, 48.75, 6.5), (75.0, 57.5, 7.0), (75.0, 66.25, 7.5), (75.0, 75.0, 8.0), (81.22222222222223, 75.83333333333333, 8.555555555555555), (87.44444444444444, 76.66666666666667, 9.11111111111111), (93.66666666666667, 77.5, 9.666666666666666), (99.88888888888889, 78.33333333333333, 10.222222222222221), (106.11111111111111, 79.16666666666667, 10.777777777777779), (112.33333333333334, 80.0, 11.333333333333334), (118.55555555555556, 80.83333333333333, 11.88888888888889), (124.77777777777777, 81.66666666666667, 12.444444444444445), (131.0, 82.5, 13.0), (137.22222222222223, 83.33333333333333, 13.555555555555555), (143.44444444444446, 84.16666666666667, 14.11111111111111), (149.66666666666669, 85.0, 14.666666666666668), (155.88888888888889, 85.83333333333333, 15.222222222222221), (162.11111111111111, 86.66666666666667, 15.777777777777779), (168.33333333333331, 87.5, 16.333333333333336), (174.55555555555554, 88.33333333333333, 16.88888888888889), (180.77777777777777, 89.16666666666667, 17.444444444444443), (187.0, 90.0, 18.0), (186.5, 90.75, 14.5), (186.0, 91.5, 11.0), (185.5, 92.25, 7.5), (185.0, 93.0, 4.0), (180.0, 99.0909090909091, 4.636363636363637), (175.0, 105.18181818181819, 5.2727272727272725), (170.0, 111.27272727272728, 5.909090909090909), (165.0, 117.36363636363636, 6.545454545454545), (160.0, 123.45454545454545, 7.181818181818182), (155.0, 129.54545454545456, 7.818181818181818), (150.0, 135.63636363636363, 8.454545454545453), (145.0, 141.72727272727272, 9.09090909090909), (140.0, 147.8181818181818, 9.727272727272727), (135.0, 153.9090909090909, 10.363636363636363), (130.0, 160.0, 11.0), (129.0, 159.0, 8.333333333333334), (128.0, 158.0, 5.666666666666667), (127.0, 157.0, 3.0), (118.33333333333333, 155.83333333333334, 3.6666666666666665), (109.66666666666667, 154.66666666666666, 4.333333333333333), (101.0, 153.5, 5.0), (92.33333333333334, 152.33333333333334, 5.666666666666666), (83.66666666666667, 151.16666666666666, 6.333333333333333), (75.0, 150.0, 7.0), (66.88888888888889, 150.0, 7.222222222222222), (58.77777777777778, 150.0, 7.444444444444445), (50.66666666666667, 150.0, 7.666666666666667), (42.55555555555556, 150.0, 7.888888888888889), (34.44444444444444, 150.0, 8.11111111111111), (26.333333333333336, 150.0, 8.333333333333334), (18.22222222222223, 150.0, 8.555555555555555), (10.111111111111114, 150.0, 8.777777777777779), (2.0, 150.0, 9.0), (2.0, 150.0, 8.875), (2.0, 150.0, 8.75), (2.0, 150.0, 8.625), (2.0, 150.0, 8.5), (2.0, 150.0, 8.375), (2.0, 150.0, 8.25), (2.0, 150.0, 8.125), (2.0, 150.0, 8.0), (1.5, 148.75, 7.0), (1.0, 147.5, 6.0), (0.5, 146.25, 5.0), (0.0, 145.0, 4.0), (0.0, 135.9375, 4.75), (0.0, 126.875, 5.5), (0.0, 117.8125, 6.25), (0.0, 108.75, 7.0), (0.0, 99.6875, 7.75), (0.0, 90.625, 8.5), (0.0, 81.5625, 9.25), (0.0, 72.5, 10.0), (0.0, 63.4375, 10.75), (0.0, 54.375, 11.5), (0.0, 45.3125, 12.25), (0.0, 36.25, 13.0), (0.0, 27.1875, 13.75), (0.0, 18.125, 14.5), (0.0, 9.0625, 15.25), (0.0, 0.0, 16.0), (7.5, 0.0, 15.4), (15.0, 0.0, 14.8), (22.5, 0.0, 14.2), (30.0, 0.0, 13.6), (37.5, 0.0, 13.0), (45.0, 0.0, 12.4), (52.5, 0.0, 11.8), (60.0, 0.0, 11.2), (67.5, 0.0, 10.600000000000001), (75.0, 0.0, 10.0), (76.0, 0.0, 9.0), (77.0, 0.0, 8.0), (78.0, 0.0, 7.0), (79.0, 0.0, 6.0), (80.0, 0.0, 5.0), (80.66666666666667, 0.0, 4.333333333333333), (81.33333333333333, 0.0, 3.666666666666667), (82.0, 0.0, 3.0)]}]
[PointCloud{points=[(72.0, 0.0, 0.0), (72.75, 1.25, 1.0), (73.5, 2.5, 2.0), (74.25, 3.75, 3.0), (75.0, 5.0, 4.0), (75.0, 13.75, 4.5), (75.0, 22.5, 5.0), (75.0, 31.25, 5.5), (75.0, 40.0, 6.0), (75.0, 48.75, 6.5), (75.0, 57.5, 7.0), (75.0, 66.25, 7.5), (75.0, 75.0, 8.0), (81.22222222222223, 75.83333333333333, 8.555555555555555), (87.44444444444444, 76.66666666666667, 9.11111111111111), (93.66666666666667, 77.5, 9.666666666666666), (99.88888888888889, 78.33333333333333, 10.222222222222221), (106.11111111111111, 79.16666666666667, 10.777777777777779), (112.33333333333334, 80.0, 11.333333333333334), (118.55555555555556, 80.83333333333333, 11.88888888888889), (124.77777777777777, 81.66666666666667, 12.444444444444445), (131.0, 82.5, 13.0), (137.22222222222223, 83.33333333333333, 13.555555555555555), (143.44444444444446, 84.16666666666667, 14.11111111111111), (149.66666666666669, 85.0, 14.666666666666668), (155.88888888888889, 85.83333333333333, 15.222222222222221), (162.11111111111111, 86.66666666666667, 15.777777777777779), (168.33333333333331, 87.5, 16.333333333333336), (174.55555555555554, 88.33333333333333, 16.88888888888889), (180.77777777777777, 89.16666666666667, 17.444444444444443), (187.0, 90.0, 18.0), (186.5, 90.75, 14.5), (186.0, 91.5, 11.0), (185.5, 92.25, 7.5), (185.0, 93.0, 4.0), (180.0, 99.0909090909091, 4.636363636363637), (175.0, 105.18181818181819, 5.2727272727272725), (170.0, 111.27272727272728, 5.909090909090909), (165.0, 117.36363636363636, 6.545454545454545), (160.0, 123.45454545454545, 7.181818181818182), (155.0, 129.54545454545456, 7.818181818181818), (150.0, 135.63636363636363, 8.454545454545453), (145.0, 141.72727272727272, 9.09090909090909), (140.0, 147.8181818181818, 9.727272727272727), (135.0, 153.9090909090909, 10.363636363636363), (130.0, 160.0, 11.0), (129.0, 159.0, 8.333333333333334), (128.0, 158.0, 5.666666666666667), (127.0, 157.0, 3.0), (118.33333333333333, 155.83333333333334, 3.6666666666666665), (109.66666666666667, 154.66666666666666, 4.333333333333333), (101.0, 153.5, 5.0), (92.33333333333334, 152.33333333333334, 5.666666666666666), (83.66666666666667, 151.16666666666666, 6.333333333333333), (75.0, 150.0, 7.0), (66.88888888888889, 150.0, 7.222222222222222), (58.77777777777778, 150.0, 7.444444444444445), (50.66666666666667, 150.0, 7.666666666666667), (42.55555555555556, 150.0, 7.888888888888889), (34.44444444444444, 150.0, 8.11111111111111), (26.333333333333336, 150.0, 8.333333333333334), (18.22222222222223, 150.0, 8.555555555555555), (10.111111111111114, 150.0, 8.777777777777779), (2.0, 150.0, 9.0), (2.0, 150.0, 8.875), (2.0, 150.0, 8.75), (2.0, 150.0, 8.625), (2.0, 150.0, 8.5), (2.0, 150.0, 8.375), (2.0, 150.0, 8.25), (2.0, 150.0, 8.125), (2.0, 150.0, 8.0), (1.5, 148.75, 7.0), (1.0, 147.5, 6.0), (0.5, 146.25, 5.0), (0.0, 145.0, 4.0), (0.0, 135.9375, 4.75), (0.0, 126.875, 5.5), (0.0, 117.8125, 6.25), (0.0, 108.75, 7.0), (0.0, 99.6875, 7.75), (0.0, 90.625, 8.5), (0.0, 81.5625, 9.25), (0.0, 72.5, 10.0), (0.0, 63.4375, 10.75), (0.0, 54.375, 11.5), (0.0, 45.3125, 12.25), (0.0, 36.25, 13.0), (0.0, 27.1875, 13.75), (0.0, 18.125, 14.5), (0.0, 9.0625, 15.25), (0.0, 0.0, 16.0), (7.5, 0.0, 15.4), (15.0, 0.0, 14.8), (22.5, 0.0, 14.2), (30.0, 0.0, 13.6), (37.5, 0.0, 13.0), (45.0, 0.0, 12.4), (52.5, 0.0, 11.8), (60.0, 0.0, 11.2), (67.5, 0.0, 10.600000000000001), (75.0, 0.0, 10.0), (76.0, 0.0, 9.0), (77.0, 0.0, 8.0), (78.0, 0.0, 7.0), (79.0, 0.0, 6.0), (80.0, 0.0, 5.0), (80.66666666666667, 0.0, 4.333333333333333), (81.33333333333333, 0.0, 3.666666666666667), (82.0, 0.0, 3.0)]}, PointCloud{points=[(72.3, 0.0, 0.0), (72.3, 0.0, 0.0), (72.3, 0.0, 0.0), (72.3, 0.0, 0.0), (72.3, 0.0, 0.0), (72.3, 0.0, 0.0), (72.3, 0.0, 0.0), (72.3, 0.0, 0.0), (72.3, 0.0, 0.0), (72.3, 0.0, 0.0), (72.3, 0.0, 0.0)]}]
2024-03-10 17:34:21 INFO  NetworkClient:977 - [Consumer clientId=consumer-lidar_consumer_group-111-1, groupId=lidar_consumer_group-111] Node -1 disconnected.