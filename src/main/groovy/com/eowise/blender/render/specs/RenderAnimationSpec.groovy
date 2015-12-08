package com.eowise.blender.render.specs

import org.gradle.api.Project

/**
 * Created by aurel on 16/03/14.
 */
class RenderAnimationSpec extends RenderSceneSpec {

    int start
    int end
    Closure rename

    public RenderAnimationSpec(Project project) {
        super(project)
        this.rename = { fileName -> fileName }
    }

    def start(int value) {
        start = value
    }

    def end(int value) {
        end = value;
    }

    def rename(Closure c) {
        rename = c
    }
}
