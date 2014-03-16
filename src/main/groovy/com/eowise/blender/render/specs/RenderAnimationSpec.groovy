package com.eowise.blender.render.specs

/**
 * Created by aurel on 16/03/14.
 */
class RenderAnimationSpec extends RenderSceneSpec {

    int start
    int end

    public RenderAnimationSpec() {
        super()
    }

    def start(int value) {
        start = value
    }

    def end(int value) {
        end = value;
    }
}
