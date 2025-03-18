# Kotlin Compiler Back-end Performance Measurement Sample

## Introduction
> **Summary:**
> 
> This work is for micro-benchmarking each Kotlin Compiler's Targets(e.g. JVM, JS, Native, Wasm) to measure the native-target back-end (using LLVM) compiles either more or less efficiently than other ones.

Although Kotlin has been developed for multi-target, many developers consider the open-source language as long-term replacement for Java and usually compile `.kt` source code to JVM bytecode. However, native-target back-end is still important for Kotlin compiler infrastructure because it is essential part for Kotlin Multiplatform (KMP) applications to be executed by platform-specific binaries without the effort from any virtual machine and the web. Due to this property of language ecosystem, Kotlin developers can release applications for many architectures all at once. So, improving backend performance for generating platform-specific binaries has been recognized as the crucial mission of this multi-targeted language.
This work - as base for the optimization process - presents the compiler-performance measured by each back-end target (Java Virtual Machine, Native-macOS-Arm64, Javascript, and WebAssembly) to use `kotlinx-benchmark` toolkit.

> Figure. **How Kotlin/Native Compiler Back-end works with LLVM**
> 
> <img width="100%" alt="image" src="https://github.com/user-attachments/assets/b964d4b7-90fd-4c23-b233-4aef6e077a19"/>

## Feature

## Structure
### Project

### Gradle

## Measurement Results
> Lower means better performance.
> 
> Unit :  **Nanoseconds per operation** `ns / op`

- **`<target>Benchmark.kt`** <img width="100%" alt="image" src="https://github.com/user-attachments/assets/185ef871-5148-4d1d-8d95-2ee776985593" />
    - Cosine operation that targets Kotlin/Native is executed <span style="background:#d3f8b6">1.788x slower</span> than one targets Kotlin/JVM
    - Square-root operation for native is executed <span style="background:#d3f8b6">6.415x slower</span> than one for JVM


- **`ParamsBenchmark.kt`** <img width="100%" alt="image" src="https://github.com/user-attachments/assets/ea3c9547-f0c1-4466-9c33-61b75b174d15" />
    - With the compilation environment targeting Kotlin/Native, Square-root operation that includes receiving params and type-converting call is executed <span style="background:#d3f8b6">4.144x slower</span> than the one targeting JVM.
    - Benchmarking of concatenating two strings does not show statistically significant difference between targeted platforms.


- **`CommonBenchmark.kt`** with Forking <img width="100%" alt="image" src="https://github.com/user-attachments/assets/93ed93bd-dbff-42a5-b455-5b8c485389b1" />
    - The function that has one thousand-time iterations and targeted native binaries is executed <span style="background:#d3f8b6">2.101x slower</span> than the one that targeted to Java Bytecode.
    - Language processing of loop including `Blackhole.consume()` method is <span style="background:#fdbfff">80.641x slower</span> in case using Kotlin/Native back-end than Kotlin/JVM's one.
    - For mathBenchmark, generating native binaries is <span style="background:#d3f8b6">1.283x slower</span> than compilation to Java Bytecode and using JVM.

## How to Run
1. Clone and gradle build in `Intelij IDEA`
2. If gradle build is successful, In terminal,
```terminal
./gradlew <targetName><configName>Benchmark
```


