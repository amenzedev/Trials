import numpy as np
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d.art3d import Poly3DCollection

# Define the vertices of the cuboid in 3D space
vertices = np.array([
    [2, 2, 2],  # Vertex 0
    [4, 2, 2],  # Vertex 1
    [4, 4, 2],  # Vertex 2
    [2, 4, 2],  # Vertex 3
    [2, 2, 4],  # Vertex 4
    [4, 2, 4],  # Vertex 5
    [4, 4, 4],  # Vertex 6
    [2, 4, 4]   # Vertex 7
])

# Define the origin point A
A = np.array([0, 0, 0])

# Define the endpoint B (you need to specify this point)
B = np.array([3, 2, 3])  # Replace with your desired endpoint coordinates

# Define the faces of the cuboid
faces = [
    [vertices[0], vertices[1], vertices[2], vertices[3]],  # Face 0 (bottom)
    [vertices[4], vertices[5], vertices[6], vertices[7]],  # Face 1 (top)
    [vertices[0], vertices[1], vertices[5], vertices[4]],  # Face 2 (front)
    [vertices[2], vertices[3], vertices[7], vertices[6]],  # Face 3 (back)
    [vertices[0], vertices[3], vertices[7], vertices[4]],  # Face 4 (left)
    [vertices[1], vertices[2], vertices[6], vertices[5]]   # Face 5 (right)
]

# Initialize variables to store the closest intersection point and face
closest_intersection = None
closest_face = None

for i, face_vertices in enumerate(faces):
    # Calculate the normal vector of the current face
    normal = np.cross(face_vertices[1] - face_vertices[0], face_vertices[2] - face_vertices[0])

    # Calculate the intersection point between the line AB and the current face
    t = np.dot(face_vertices[0] - A, normal) / np.dot(B - A, normal)
    intersection_point = A + t * (B - A)

    # Check if the intersection point is within the bounds of the face
    min_coords = np.min(face_vertices, axis=0)
    max_coords = np.max(face_vertices, axis=0)
    if np.all(min_coords <= intersection_point) and np.all(intersection_point <= max_coords):
        # If the intersection point is within the face, check if it's closer
        if closest_intersection is None or np.linalg.norm(intersection_point - A) < np.linalg.norm(closest_intersection - A):
            closest_intersection = intersection_point
            closest_face = i

if closest_face is not None:
    print(f"The line from A to B intercepts Face {closest_face} of the cuboid at point {closest_intersection}.")
else:
    print("The line does not intercept any face of the cuboid.")

# Visualization
fig = plt.figure()
ax = fig.add_subplot(111, projection='3d')

# Plot the cuboid
ax.add_collection3d(Poly3DCollection(faces, alpha=0.25, linewidths=1, edgecolor='r'))

# Plot the line segment
ax.plot([A[0], B[0]], [A[1], B[1]], [A[2], B[2]], 'b-', label='Line Segment')

ax.set_xlabel('X')
ax.set_ylabel('Y')
ax.set_zlabel('Z')

# Set limits for the X, Y, and Z axes
ax.set_xlim(0, 10)
ax.set_ylim(0, 10)
ax.set_zlim(0, 10)

plt.legend()
plt.show()
