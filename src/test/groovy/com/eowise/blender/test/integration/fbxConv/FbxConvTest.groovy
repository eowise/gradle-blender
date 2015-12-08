package com.eowise.blender.test.integration.fbxConv

import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification

/**
 * Created by aurel on 08/12/15.
 */
class FbxConvTest extends Specification {

    def "convert FBX to G3DJ"() {
        when:
        GradleRunner.create()
                .withProjectDir(new File(""))
                .withArguments("testFbxConv", "--stacktrace", "--rerun-tasks")
                .build();

        then:
        new File("cube.g3dj").exists()
    }
}
