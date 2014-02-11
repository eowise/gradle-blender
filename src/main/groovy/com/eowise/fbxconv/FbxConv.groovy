package com.eowise.fbxconv

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

/**
 * Created by aurel on 23/01/14.
 */
class FbxConv extends DefaultTask {

    @Input
    protected String format

    @InputFiles
    protected FileCollection fbxFiles

    @OutputDirectory
    protected File outputDir

    def FbxConv() {
        format = 'G3DB'
    }

    def convert(FileCollection files) {
        fbxFiles = files
    }

    def into(File file) {
        outputDir = file
    }

    def format(String value) {
        format = value
    }

    @TaskAction
    def run() {
        fbxFiles.each {
            File file ->
                String outputFile = file.name.replaceFirst(~/\.[^\.]+$/, '') + ".${format.toLowerCase()}"
                project.exec {
                    commandLine 'fbx-conv'
                    args '-o', format, file
                }
                project.copy {
                    from file.parentFile
                    include outputFile
                    into outputDir
                }
                project.delete("${file.parentFile}/${outputFile}")
        }
    }
}
