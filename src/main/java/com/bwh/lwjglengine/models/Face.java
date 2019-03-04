package com.bwh.lwjglengine.models;

public class Face {
    public IndexGroup[] indices = new IndexGroup[3];

    public Face(final String v1, final String v2, final String v3) {
        indices[0] = parseLine(v1);
        indices[1] = parseLine(v2);
        indices[2] = parseLine(v3);
    }

    private static IndexGroup parseLine(String line) {
        IndexGroup index = new IndexGroup();

        String[] tokens = line.split("/");
        int length = tokens.length;
        index.vertex = Integer.parseInt(tokens[0]);
        if (length > 1) {
            String texCoord = tokens[1];
            index.texCoord = texCoord.length() > 0
                    ? Integer.parseInt(texCoord) - 1
                    : IndexGroup.NO_VALUE;
            if (length > 2) {
                index.normal = Integer.parseInt(tokens[2]) - 1;
            }
        }

        return index;
    }
}
