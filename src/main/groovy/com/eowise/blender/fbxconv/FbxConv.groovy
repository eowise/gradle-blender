package com.eowise.blender.fbxconv

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.file.FileTree
import org.gradle.api.file.FileVisitDetails
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.incremental.IncrementalTaskInputs

/**
 * Created by aurel on 23/01/14.
 */
class FbxConv extends DefaultTask {

    @Input
    def String format

    @InputFiles
    def FileTree fbxFiles

    @OutputDirectory
    def File outputDir

    def FbxConv() {
        format = 'G3DB'
    }

    def convert(FileCollection files) {
        fbxFiles = files.getAsFileTree()
    }

    def convert(FileTree files) {
        fbxFiles = files
    }

    def into(File file) {
        outputDir = file
    }

    def format(String value) {
        format = value
    }

    @TaskAction
    void run(IncrementalTaskInputs incrementalInputs) {
        FileCollection changedFiles = project.files()
        FileCollection removedFiles = project.files()

        incrementalInputs.outOfDate {
            change ->
                changedFiles.from(change.file)
        }

        incrementalInputs.removed {
            remove ->
                removedFiles.from(remove.file)
        }


        fbxFiles.visit {
            FileVisitDetails visitor ->
                if (changedFiles.contains(visitor.getFile())) {
                    if (!visitor.isDirectory()) {
                        String outputFile = visitor.getName().replaceFirst(~/\.[^\.]+$/, '') + ".${format.toLowerCase()}"
                        project.exec {
                            commandLine 'fbx-conv'
                            args '-f', '-o', format, visitor.getFile()
                        }
                        project.copy {
                            from visitor.getFile().parentFile
                            include outputFile
                            into "${outputDir}/${visitor.getRelativePath().getParent()}"
                        }
                        project.delete("${visitor.getFile().parentFile}/${outputFile}")
                    }
                    else if (removedFiles.contains(visitor.getFile())) {
                        project.delete("${path}/${visitor.getPath()}")
                    }
                }
        }
    }
}
