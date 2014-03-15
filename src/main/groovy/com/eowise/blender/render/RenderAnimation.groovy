package com.eowise.blender.render

import com.eowise.blender.render.specs.RenderSceneSpec
import org.gradle.api.DefaultTask
import org.gradle.api.Task
import org.gradle.api.tasks.TaskAction

/**
 * Created by aurel on 12/03/14.
 */
class RenderAnimation extends DefaultTask {

    RenderSceneSpec spec;

    RenderAnimation() {
        spec = new RenderSceneSpec()
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
            commandLine 'blender', '-b', spec.blendFile, '-F', 'PNG', '-S', spec.scene, '-s', start, '-e', end, '-a'
        }

        project.copy {
            from spec.blendFile.getParent() + '/tmp'
            into spec.outputPath
            include '*.png'
            rename ~/([^\.]+)\.png/, spec.scene.toLowerCase() + '$1.png'
        }
    }
}