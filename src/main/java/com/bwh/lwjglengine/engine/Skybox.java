package com.bwh.lwjglengine.engine;

import com.bwh.lwjglengine.graphics.*;
import com.bwh.lwjglengine.models.ObjLoader;

public class Skybox extends Entity {

    public Skybox(String objModel, String textureFile) throws Exception {
        super(null);
        Mesh skyBoxMesh = ObjLoader.loadMesh(objModel);
        Texture texture = new Texture(textureFile);
        skyBoxMesh.setMaterial(new Material(texture, 0));
        setMesh(skyBoxMesh);
        setPosition(0, 0, 0);
    }
}
