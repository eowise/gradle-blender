package com.eowise.blender.test.integration.render

import org.gradle.testkit.runner.GradleRunner

import spock.lang.Specification

/**
 * Created by aurel on 07/12/15.
 */
class RenderImageTest extends Specification {

    void "render an image"() {
        when:
        GradleRunner.create()
                .withProjectDir(new File(""))
                .withArguments("testRenderImage", "--stacktrace", "--rerun-tasks")
                .build();

        then:
        new File("cube.png").exists()
    }
}
