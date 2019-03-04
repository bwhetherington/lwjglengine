package com.bwh.lwjglengine.models;

import com.bwh.lwjglengine.util.FileUtil;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class ObjLoader {
    public static ObjModel loadObj(String fileName) throws Exception {
        final String text = FileUtil.readFile(fileName);
        final String[] lines = text.split("\n");

        List<Vector4f> vertices = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Vector2f> texCoords = new ArrayList<>();
        List<Face> faces = new ArrayList<>();

        for (final String line : lines) {
            if (line.startsWith("#")) {
                continue;
            }
            final String[] tokens = line.split("\\s+");
            if (tokens.length > 0) {
                switch (tokens[0]) {
                    case "v":
                        final Vector4f vertex = new Vector4f(
                                Float.parseFloat(tokens[1]),
                                Float.parseFloat(tokens[2]),
                                Float.parseFloat(tokens[3]),
                                1
                        );
                        vertices.add(vertex);
                        break;
                    case "vn":
                        final Vector3f normal = new Vector3f(
                                Float.parseFloat(tokens[1]),
                                Float.parseFloat(tokens[2]),
                                Float.parseFloat(tokens[3])
                        );
                        normals.add(normal);
                        break;
                    case "vt":
                        final Vector2f texCoord = new Vector2f(
                                Float.parseFloat(tokens[1]),
                                Float.parseFloat(tokens[2])
                        );
                        texCoords.add(texCoord);
                        break;
                    case "f":
                        final Face face = new Face(tokens[1], tokens[2], tokens[3]);
                        faces.add(face);
                        break;
                    default:
                        throw new Exception("Unsupported line type: " + tokens[0]);
                }
            } else {
                throw new Exception("Invalid line format");
            }
        }

        System.out.println(faces);

        return new ObjModel(vertices, normals, texCoords, faces);
    }
}
