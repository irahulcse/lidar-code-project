package thesis.context.data;

import org.apache.kafka.common.protocol.types.Field;

import java.io.Serializable;
import java.util.List;

public class PointCloud implements Serializable {
    private List<Point3D> points;

    public PointCloud(List<Point3D> points) {
        this.points = points;
    }

    public List<Point3D> getPoints() {
        return points;
    }

    public void setPoints(List<Point3D> points) {
        this.points = points;
    }

    public String getAllCoordinatesAsString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Point3D point : points) {
            double x = point.getX();
            double y = point.getY();
            double z = point.getZ();
            stringBuilder.append("Point: (").append(x).append(", ").append(y).append(", ").append(z).append(")\n");
        }
        return stringBuilder.toString();
    }


    // Method to get x coordinates of all points in the point cloud

    @Override
    public String toString() {
        return "PointCloud{" +
                "points=" + points +
                '}';
    }
}
