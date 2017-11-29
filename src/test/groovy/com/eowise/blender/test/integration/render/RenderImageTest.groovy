package com.eowise.blender.test.integration.render

import org.gradle.testkit.runner.GradleRunner

import spock.lang.Specification

/**
 * Created by aurel on 07/12/15.
 */
class RenderImageTest extends Specification {

    void "render an image"() {
        def result = new File("src/test/resources/cube.png")

        result.delete()

        when:
        GradleRunner.create()
                .withProjectDir(new File("src/test/resources"))
                .withArguments("testRenderImage", "--stacktrace", "--rerun-tasks")
                .build()

        then:
        result.exists()
    }
}
