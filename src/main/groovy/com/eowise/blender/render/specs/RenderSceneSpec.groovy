package com.eowise.blender.render.specs

import org.gradle.api.Project

/**
 * Created by aurel on 15/03/14.
 */
class RenderSceneSpec {

    private Project project
    File blendFile
    String scene
    File outputFile

    public RenderSceneSpec(Project project) {
        this.project = project
    }

    def from(Object file) {
        blendFile = project.file(file)
    }

    def scene(String sceneName) {
        scene = sceneName
    }

    def into(Object file) {
        outputFile = project.file(file)
    }

    String getOutputPath() {
        return outputFile.absolutePath.substring(0, outputFile.absolutePath.lastIndexOf(File.separator))
    }
}
