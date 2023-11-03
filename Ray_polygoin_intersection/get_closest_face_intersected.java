import Jama.Matrix;

public class CuboidIntersection {
    public static void main(String[] args) {
        // Define the vertices of the cuboid in 3D space
        double[][] vertices = {
            {0, 0, 0},  // Vertex 0
            {1, 0, 0},  // Vertex 1
            {1, 1, 0},  // Vertex 2
            {0, 1, 0},  // Vertex 3
            {0, 0, 1},  // Vertex 4
            {1, 0, 1},  // Vertex 5
            {1, 1, 1},  // Vertex 6
            {0, 1, 1}   // Vertex 7
        };

        // Define the origin point A
        double[] A = {0, 0, 0};

        // Define the endpoint B (you need to specify this point)
        double[] B = {1, 0.5, 0.5}; // Replace with your desired endpoint coordinates

        // Define the faces of the cuboid as indices of vertices
        int[][] faces = {
            {0, 1, 2, 3}, // Face 0 (bottom)
            {4, 5, 6, 7}, // Face 1 (top)
            {0, 1, 5, 4}, // Face 2 (front)
            {2, 3, 7, 6}, // Face 3 (back)
            {0, 3, 7, 4}, // Face 4 (left)
            {1, 2, 6, 5}  // Face 5 (right)
        };

        // Initialize variables to store the closest intersection point and face
        double[] closestIntersection = null;
        int closestFace = -1;

        for (int i = 0; i < faces.length; i++) {
            int[] faceIndices = faces[i];

            // Get the vertices of the current face
            double[][] faceVertices = new double[4][3];
            for (int j = 0; j < 4; j++) {
                faceVertices[j] = vertices[faceIndices[j]];
            }

            // Calculate the normal vector of the current face
            Matrix normal = calculateNormalVector(faceVertices[0], faceVertices[1], faceVertices[2]);

            // Calculate the intersection point between the line AB and the current face
            double[] intersectionPoint = calculateIntersectionPoint(A, B, faceVertices[0], normal);

            // Check if the intersection point is within the bounds of the face
            if (isPointWithinFaceBounds(intersectionPoint, faceVertices)) {
                // If the intersection point is within the face, check if it's closer
                if (closestIntersection == null || distance(A, intersectionPoint) < distance(A, closestIntersection)) {
                    closestIntersection = intersectionPoint;
                    closestFace = i;
                }
            }
        }

        if (closestFace != -1) {
            System.out.println("The line from A to B intercepts Face " + closestFace + " of the cuboid at point " + Arrays.toString(closestIntersection));
        } else {
            System.out.println("The line does not intercept any face of the cuboid.");
        }
    }

    // Calculate the normal vector of a plane given three points on the plane
    private static Matrix calculateNormalVector(double[] point1, double[] point2, double[] point3) {
        double[] v1 = subtract(point2, point1);
        double[] v2 = subtract(point3, point1);
        double[] normalArray = crossProduct(v1, v2);
        return new Matrix(normalArray, 1);
    }

    // Calculate the intersection point of a line with a plane
    private static double[] calculateIntersectionPoint(double[] A, double[] B, double[] pointOnPlane, Matrix planeNormal) {
        Matrix AMatrix = new Matrix(A, 1);
        Matrix BMatrix = new Matrix(B, 1);
        Matrix pointOnPlaneMatrix = new Matrix(pointOnPlane, 1);

        Matrix AB = BMatrix.minus(AMatrix);
        Matrix APoint = AMatrix.minus(pointOnPlaneMatrix);
        double t = planeNormal.times(APoint.transpose()).get(0, 0) / planeNormal.times(AB.transpose()).get(0, 0);

        double[] intersectionPoint = new double[3];
        for (int i = 0; i < 3; i++) {
            intersectionPoint[i] = A[i] + t * (B[i] - A[i]);
        }

        return intersectionPoint;
    }

    // Check if a point is within the bounds of a face
    private static boolean isPointWithinFaceBounds(double[] point, double[][] faceVertices) {
        double[] minCoords = new double[3];
        double[] maxCoords = new double[3];
        for (int i = 0; i < 3; i++) {
            double[] coords = new double[4];
            for (int j = 0; j < 4; j++) {
                coords[j] = faceVertices[j][i];
            }
            minCoords[i] = min(coords);
            maxCoords[i] = max(coords);
        }
        return point[0] >= minCoords[0] && point[0] <= maxCoords[0] &&
               point[1] >= minCoords[1] && point[1] <= maxCoords[1] &&
               point[2] >= minCoords[2] && point[2] <= maxCoords[2];
    }

    // Calculate the cross product of two vectors
    private static double[] crossProduct(double[] a, double[] b) {
        double[] result = new double[3];
        result[0] = a[1] * b[2] - a[2] * b[1];
        result[1] = a[2] * b[0] - a[0] * b[2];
        result[2] = a[0] * b[1] - a[1] * b[0];
        return result;
    }

    // Calculate the Euclidean distance between two points
    private static double distance(double[] a, double[] b) {
        double sum = 0.0;
        for (int i = 0; i < 3; i++) {
            double diff = a[i] - b[i];
            sum += diff * diff;
        }
        return Math.sqrt(sum);
    }

    // Find the minimum value in an array
    private static double min(double[] arr) {
        double min = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < min) {
                min = arr[i];
            }
        }
        return min;
    }

    // Find the maximum value in an array
    private static double max(double[] arr) {
        double max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }
        return max;
    }
}
