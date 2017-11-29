package com.eowise.blender.test.integration.fbxConv

import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification

/**
 * Created by aurel on 08/12/15.
 */
class FbxConvTest extends Specification {

    def "convert FBX to G3DJ"() {
        def result = new File("src/test/resources/cube.g3dj")

        result.delete()

        when:
        GradleRunner.create()
                .withProjectDir(new File("src/test/resources"))
                .withArguments("testFbxConv", "--stacktrace", "--rerun-tasks")
                .build()

        then:
        result.exists()
    }
}
