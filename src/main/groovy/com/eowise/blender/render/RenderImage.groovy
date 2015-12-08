package com.eowise.blender.render

import com.eowise.blender.render.specs.RenderSceneSpec
import org.gradle.api.DefaultTask
import org.gradle.api.Task
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

/**
 * Created by aurel on 12/03/14.
 */
class RenderImage extends DefaultTask {

    RenderSceneSpec spec;

    RenderImage() {
        spec = new RenderSceneSpec(project)
    }

    @InputFile
    File getBlendFile() {
        return spec.blendFile
    }

    @Input
    String getScene() {
        return spec.scene
    }


    @OutputFile
    File getOutputFile() {
        return spec.outputFile
    }

    @Override
    Task configure(Closure configureClosure) {

        project.configure(spec, configureClosure)

        return this;
    }

    @TaskAction
    def run() {
        project.delete project.fileTree(temporaryDir).include('*')

        project.exec {
            commandLine 'blender', '-b', getBlendFile(), '-S', getScene(), '-o', "${temporaryDir}/${name}", '-F', 'PNG', '-f', 1
        }

        project.copy {
            from "${temporaryDir}"
            into spec.getOutputPath()
            rename { fileName -> getOutputFile().getName() }
        }
    }
}
