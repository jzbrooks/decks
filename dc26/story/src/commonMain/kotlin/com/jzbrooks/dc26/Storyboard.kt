package com.jzbrooks.dc26

import com.jzbrooks.dc26.scenes.bakeTransformations
import com.jzbrooks.dc26.scenes.bakeTransformationsAlgorithm
import com.jzbrooks.dc26.scenes.closing
import com.jzbrooks.dc26.scenes.commandVariant
import com.jzbrooks.dc26.scenes.conversion
import com.jzbrooks.dc26.scenes.curvesToArcs
import com.jzbrooks.dc26.scenes.curvesToArcsAlgorithm
import com.jzbrooks.dc26.scenes.howGraphicsWork
import com.jzbrooks.dc26.scenes.howPathsWork
import com.jzbrooks.dc26.scenes.kotlinMultiplatformFunnel
import com.jzbrooks.dc26.scenes.llvmFunnel
import com.jzbrooks.dc26.scenes.mergePaths
import com.jzbrooks.dc26.scenes.mergePathsAlgorithm
import com.jzbrooks.dc26.scenes.motivationMultipleFormats
import com.jzbrooks.dc26.scenes.motivationNode
import com.jzbrooks.dc26.scenes.motivationVectorPain
import com.jzbrooks.dc26.scenes.optimizationCategories
import com.jzbrooks.dc26.scenes.pipeline
import com.jzbrooks.dc26.scenes.printIr
import com.jzbrooks.dc26.scenes.r8Funnel
import com.jzbrooks.dc26.scenes.simplifyBezierAlgorithm
import com.jzbrooks.dc26.scenes.simplifyCommands
import com.jzbrooks.dc26.scenes.title
import com.jzbrooks.dc26.scenes.usingVgo
import com.jzbrooks.dc26.scenes.vat
import com.jzbrooks.dc26.scenes.vgoName
import com.jzbrooks.dc26.scenes.whatIsVgo
import com.jzbrooks.dc26.theme.VgoTheme
import dev.bnorm.storyboard.SceneFormat
import dev.bnorm.storyboard.Storyboard
import dev.bnorm.storyboard.layout.Keynote
import dev.bnorm.storyboard.layout.template.section

fun createStoryboard(): Storyboard =
    Storyboard.build(
        title = "Shrinking Vector Artwork",
        format = SceneFormat.Keynote,
        decorator = VgoTheme,
    ) {
        title()

        motivationVectorPain()
        motivationNode()
        motivationMultipleFormats()

        section("What are vector graphics, actually?") {
            howGraphicsWork()
            conversion(false)
        }

        section("Which got me thinkin’") {
            r8Funnel()
            kotlinMultiplatformFunnel()
            llvmFunnel()
            vgoName()
        }

        section("What is vgo?") {
            whatIsVgo()
            conversion(true)
            printIr()
        }

        section("How Paths Work") {
            howPathsWork()
        }

        section("Optimization Pipeline") {
            optimizationCategories()
            pipeline()
            commandVariant()
            bakeTransformations()
            bakeTransformationsAlgorithm()
            simplifyCommands()
            simplifyBezierAlgorithm()
            curvesToArcs()
            curvesToArcsAlgorithm()
            mergePaths()
            mergePathsAlgorithm()
        }

        section("Using vgo") {
            usingVgo()
        }

        section("Extras") {
            vat()
        }

        closing()
    }
