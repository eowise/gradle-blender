package com.eowise.blender.render

import com.eowise.blender.render.specs.RenderAnimationSpec
import com.eowise.blender.render.specs.RenderSceneSpec
import org.gradle.api.DefaultTask
import org.gradle.api.Task
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

/**
 * Created by aurel on 12/03/14.
 */
class RenderAnimation extends DefaultTask {

    RenderAnimationSpec spec;

    RenderAnimation() {
        spec = new RenderAnimationSpec()
    }

    @InputFile
    File getBlendFile() {
        return spec.blendFile
    }

    @Input
    String getScene() {
        return spec.scene
    }

    @OutputDirectory
    File getOutputDirectory() {
        return spec.outputPath
    }

    @Input
    int getStart() {
        return spec.start
    }

    @Input
    int getEnd() {
        return spec.end
    }

    Task configure(Closure configureClosure) {

        project.configure(spec, configureClosure)

        return this;
    }

    @TaskAction
    def run() {
        project.delete project.fileTree(dir: spec.blendFile.getParent(),  include: '*.png')
        project.delete spec.blendFile.getParent() + '/tmp'

        project.exec {
            commandLine 'blender', '-b', getBlendFile(), '-o', "${temporaryDir}/${name}-####", '-F', 'PNG', '-S', getScene(), '-s', getStart(), '-e', getEnd(), '-a'
        }

        project.copy {
            from temporaryDir
            into getOutputDirectory()
            include '*.png'
            rename ~/${name}-(\d+)\.png/, spec.scene.toLowerCase() + '-$1.png'
        }
    }
}