package test

import kotlinx.benchmark.*
import kotlin.math.*

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(BenchmarkTimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = BenchmarkTimeUnit.SECONDS)
@Measurement(timeUnit = BenchmarkTimeUnit.SECONDS)
class CommonBenchmark {
    private var data = 0.0
    private lateinit var sampleText: String

    @Setup
    fun setUp() {
        data = 3.0
        sampleText = "Hello, world!"
    }

    @Benchmark
    fun exception() {
        try {
            fail()
        } catch (e: Throwable) {
            throw Exception("Failed!", e)
        }
    }

    private fun fail() {
        TODO("Not yet implemented")
    }

    @Benchmark
    fun mathBenchmark(): Double = log(sqrt(data) * cos(data), 2.0)

    @Benchmark
    fun longBenchmark(): Double {
        var value = 1.0
        repeat(1000) {
            value *= sampleText.length
        }
        return value
    }

    @Benchmark
    fun longBlackHoleBenchmark(bh: Blackhole) {
        repeat(1000) {
            bh.consume(sampleText.length)
        }
    }
}