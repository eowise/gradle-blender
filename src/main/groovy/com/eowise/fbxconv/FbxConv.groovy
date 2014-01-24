package com.eowise.fbxconv

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

/**
 * Created by aurel on 23/01/14.
 */
class FbxConv extends DefaultTask {

    @InputFiles
    protected FileCollection fbxFiles

    @OutputDirectory
    protected File outputDir

    def convert(FileCollection files) {
        fbxFiles = files
    }

    def into(File file) {
        outputDir = file
    }

    @TaskAction
    def run() {
        fbxFiles.each {
            File file ->
                String outputFile = file.name.replaceFirst(~/\.[^\.]+$/, '') + '.g3db'
                project.exec {
                    commandLine 'fbx-conv'
                    args '-o', 'G3DB', file
                }
                project.copy {
                    from file.parentFile
                    include outputFile
                    into outputDir
                }
                project.delete(outputFile)
        }
    }
}
