import kotlinx.benchmark.gradle.JsBenchmarkTarget
import kotlinx.benchmark.gradle.JsBenchmarksExecutor
import kotlinx.benchmark.gradle.JvmBenchmarkTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinAllOpen)
    alias(libs.plugins.kotlinBenchmark)
}

group = "org.kotlin-backend-benchmark"
version = "1.0-SNAPSHOT"

/** how to apply plugin to a specific source set */
allOpen {
    annotation("org.openjdk.jmh.annotations.State")
}

kotlin {
    jvm {
        compilations.create("benchmark").associateWith(compilations["main"])
    }
    js {
        nodejs()
        compilations.create("defaultExecutor").associateWith(compilations["main"])
        compilations.create("builtInExecutor").associateWith(compilations["main"])
    }

    @OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
    wasmJs {
        nodejs()
    }

    /** Native Targets */
    macosX64()
    linuxX64()
    macosArm64()
    mingwX64()

    /** Apply Multiplatform Source Hierarchy */
    applyDefaultHierarchyTemplate()

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlinBenchmarkRuntime)
            }
        }

        jvmMain {}

        jsMain {
            benchmark.targets.register("jsDefaultExecutor") {
                dependsOn(this@jsMain)
            }
            benchmark.targets.register("jsBuiltInExecutor") {
                dependsOn(this@jsMain)
            }
        }

        wasmJsMain {}

        nativeMain {}
    }
}

repositories {
    mavenCentral()
}

benchmark {
    configurations {
        named("main") { /** --> jvmBenchmark, jsBenchMark, <native target>Benchmark, benchmark */
            iterations = 5
            /** time in ms per iteration */
            iterationTime = 300
            iterationTimeUnit = "ms"
            /** Specifies the number of times the harness should fork*/
            advanced("jvmForks", 3)
            advanced("nativeFork", "perBenchmark")
            /** Generates special benchmark bridges to stop inlining optimizations */
            advanced("jsUseBridge", true)
        }

        register("params") {
            iterations = 5
            iterationTime = 300
            iterationTimeUnit = "ms"
            include("ParamsBenchmark")
            /** Assigns values to a public mutable property annotated with @Param */
            param("data", 5, 1, 8)
            param("unused", 6, 9)
        }

        register("fast") {
            include("Common")
            exclude("long")
            iterations = 5
            iterationTime = 300
            iterationTimeUnit = "ms"
            /** Whether to trigger garbage collection after each iteration */
            advanced("nativeGCAfterIteration", true)
        }

        register("csv") {
            include("Common")
            exclude("long")
            iterations = 1
            iterationTime = 300
            iterationTimeUnit = "ms"
            /** Report as csv format */
            reportFormat = "csv"
        }

        register("fork") {
            include("CommonBenchmark")
            iterations = 5
            iterationTime = 300
            iterationTimeUnit = "ms"
            /** Let JMH determine the amount, using the value in the @Fork annotation
             * for the benchmark function or its enclosing class
             * If not specified by @Fork, it defaults to Defaults.MEASUREMENT_FORKS(5) */
            advanced("jvmForks", "definedByJmh")
            /** Executes iterations within the same process("perBenchmark")
             * or each iteration in a separate process("perIteration") */
            advanced("nativeFork", "perIteration")
        }

    }

    targets {
        /** Register as a benchmark target */
        register("jvm") {
            this as JvmBenchmarkTarget
            jmhVersion = "1.36"
        }

        register("jvmBenchmark") {
            this as JvmBenchmarkTarget
            jmhVersion = "1.36"
        }

        named("jsDefaultExecutor")
        named("jsBuiltInExecutor") {
            this as JsBenchmarkTarget
            jsBenchmarksExecutor = JsBenchmarksExecutor.BuiltIn
        }

        register("wasmJs")

        /** Native Targets */
        register("linuxX64")
        register("macosX64")
        register("macosArm64")
        register("mingwX64")
    }
}

dependencies {

}