package com.eowise.blender.render.specs

/**
 * Created by aurel on 15/03/14.
 */
class RenderSceneSpec {

    File blendFile
    String scene
    File outputPath
    Closure rename

    public RenderSceneSpec() {
        rename = { s -> s }
    }

    def from(File file) {
        blendFile = file
    }

    def render(String sceneName) {
        scene = sceneName
    }

    def into(File path) {
        outputPath = path
    }

    def rename(Closure c) {
        rename = c
    }

}
