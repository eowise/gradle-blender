package com.eowise.blender.fbxconv

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryTree
import org.gradle.api.file.FileCollection
import org.gradle.api.file.FileTree
import org.gradle.api.file.FileVisitDetails
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.incremental.IncrementalTaskInputs
import org.gradle.api.tasks.util.PatternSet

import java.nio.file.Paths

/**
 * Created by aurel on 23/01/14.
 */
class FbxConv extends DefaultTask {

    String format
    boolean flip = false
    boolean pack = false
    Integer maxVertices = null
    Integer maxBones = null
    Integer maxBoneWeights = null
    PatternSet patternSet

    @Input
    Object baseDir

    @OutputDirectory
    File outputDir

    public FbxConv() {
        this.patternSet = new PatternSet()
    }

    def from(Object path) {
        baseDir = path
        outputDir = project.file(baseDir)
    }

    def include(Closure closure) {
        patternSet.include(closure)
    }

    def include(String... includes) {
        patternSet.include(includes)
    }

    def exclude(Closure closure) {
        patternSet.exclude(closure)
    }

    def exclude(String... excludes) {
        patternSet.exclude(excludes)
    }

    def into(Object path) {
        outputDir = project.file(path)
    }

    def fbx() {
        format = 'FBX'
    }

    def g3dj() {
        format = 'G3DJ'
    }

    def g3db() {
        format = 'G3DB'
    }

    def flip() {
        flip = true
    }

    def pack() {
        pack = true
    }

    def maxVertices(int m) {
        maxVertices = m
    }

    def maxBones(int b) {
        maxBones = b
    }

    def maxBoneWeights(int w) {
        maxBoneWeights = w
    }

    @Input
    def getArguments() {
        def arguments = []

        if (flip) arguments.add('-f')
        if (pack) arguments.add('-p')
        if (maxVertices != null) arguments.addAll('-m', maxVertices)
        if (maxBones != null) arguments.addAll('-b', maxBones)
        if (maxBoneWeights != null) arguments.addAll('-w', maxBoneWeights)
        arguments.addAll('-o', format)

        return arguments
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

        FileTree fbxFiles = project.fileTree(baseDir).matching(patternSet)

        fbxFiles.visit {
            FileVisitDetails visitor ->
                if (!incrementalInputs.isIncremental() || changedFiles.contains(visitor.getFile())) {
                    if (!visitor.isDirectory()) {
                        String outputFile = visitor.getName().replaceFirst(~/\.[^\.]+$/, '') + ".${format.toLowerCase()}"
                        def arguments = getArguments()

                        arguments.add(visitor.getFile())
                        arguments.add(Paths.get(outputDir.toString(), visitor.getRelativePath().getParent().toString(), outputFile))

                        project.exec {
                            commandLine 'fbx-conv'
                            args arguments
                        }
                    }
                    else if (removedFiles.contains(visitor.getFile())) {
                        project.delete("${path}/${visitor.getPath()}")
                    }
                }
        }
    }
}
