package com.bwh.lwjglengine.models;

import com.bwh.lwjglengine.graphics.Mesh;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class ObjModel {
    public final List<Vector4f> vertices;
    public final List<Vector3f> normals;
    public final List<Vector2f> texCoords;
    public final List<Face> faces;

    public ObjModel(
            final List<Vector4f> vertices,
            final List<Vector3f> normals,
            final List<Vector2f> texCoords,
            final List<Face> faces
    ) {
        this.vertices = vertices;
        this.normals = normals;
        this.texCoords = texCoords;
        this.faces = faces;
    }

    public Mesh toMesh() {
        List<Integer> indices = new ArrayList<>();

        float[] verticesArray = new float[vertices.size() * 4];
        int i = 0;
        for (final Vector4f vertex : vertices) {
            verticesArray[i * 4] = vertex.x;
            verticesArray[i * 4 + 1] = vertex.y;
            verticesArray[i * 4 + 2] = vertex.z;
            verticesArray[i * 4 + 3] = vertex.w;
        }

        final float[] texCoordsArray = new float[texCoords.size() * 2];
        final float[] normalsArray = new float[normals.size() * 3];

        for (final Face face : faces) {
            final IndexGroup[] indexGroups = face.indices;
            for (final IndexGroup index : indexGroups) {
                processFaceVertex(index, texCoords, normals, indices, texCoordsArray, normalsArray);
            }
        }

        final int[] indicesArray = indices.stream().mapToInt((Integer v) -> v).toArray();

        return new Mesh(verticesArray, texCoordsArray, normalsArray, indicesArray);
    }

    private static void processFaceVertex(
            IndexGroup index,
            List<Vector2f> textCoordList,
            List<Vector3f> normList,
            List<Integer> indicesList,
            float[] texCoordArr,
            float[] normArr
    ) {
        int vertexIndex = index.vertex;
        indicesList.add(vertexIndex);

        // Reorder texture coordinates
        if (index.texCoord >= 0) {
            Vector2f texCoord = textCoordList.get(index.texCoord);
            texCoordArr[vertexIndex * 2] = texCoord.x;
            texCoordArr[vertexIndex * 2 + 1] = texCoord.y;
        }

        // Reorder vertex normals
        if (index.normal >= 0) {
            Vector3f normal = normList.get(index.normal);
            normArr[vertexIndex * 3] = normal.x;
            normArr[vertexIndex * 3 + 1] = normal.y;
            normArr[vertexIndex * 3 + 2] = normal.z;
        }
    }
}
