package test

import kotlinx.benchmark.*
import kotlin.math.*

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(BenchmarkTimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = BenchmarkTimeUnit.SECONDS)
@Measurement(timeUnit = BenchmarkTimeUnit.SECONDS)
class ParamsBenchmark {

    @Param("1", "2", "3")
    var data = "0"

    @Param("4", "5", "6")
    var value = "0"

    @Param("""a "string" with quotes""")
    var text: String = ""

    @Benchmark
    fun mathBenchmark() = log((sqrt(data.toFloat()) * cos(value.toFloat())).toDouble(), 2.0)

    @Benchmark
    fun stringBenchmark() = data + value

    @Benchmark
    fun textCheckBenchmark(): String {
        check(text.length == 22)
        check(text.count { it == ' ' } == 2)
        return text
    }
}