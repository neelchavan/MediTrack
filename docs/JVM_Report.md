# Java Virtual Machine (JVM)

This short report explains core JVM concepts at a basic level.

1) Class Loader

- The Class Loader is responsible for loading .class files (compiled Java bytecode) into the JVM.
- It finds class definitions from locations like the application classpath or libraries and brings them into the runtime so the JVM can use them.
- Class loading is typically done in three steps: loading, linking (verification, preparation, resolution), and initialization.

2) Runtime Data Areas

These are the main memory areas JVM uses while a program runs:

- Heap: A shared memory area where all Java objects and arrays live. The garbage collector runs on the heap to free unused objects.
- Stack: Each thread has its own stack. A stack stores frames for method calls, including local variables, operand stack, and return addresses. When a method is called, a new frame is pushed; when it returns, the frame is popped.
- Method Area: A shared area that holds class-level information such as method bytecode, field and method metadata, and constant pool entries. (In some JVM implementations this is part of the "metaspace".)
- PC Register: Each thread has a Program Counter (PC) register that keeps the address of the current instruction being executed in the bytecode for that thread.

3) Execution Engine

- The Execution Engine reads bytecode and executes it. It acts as the JVM's runtime processor.
- It can use an interpreter to read and execute bytecode instruction-by-instruction, and it cooperates with the JIT compiler for faster execution of hot code paths.

4) JIT Compiler vs Interpreter

- Interpreter: Reads and executes bytecode one instruction at a time. It is simple and starts running quickly, but it's slower for code that runs many times.
- JIT (Just-In-Time) Compiler: Compiles frequently executed bytecode into native machine code at runtime. This speeds up repeated execution because native code runs faster than interpreted bytecode.
- Many modern JVMs use a mix: they interpret first, and when a method is used often (hot), the JIT compiles it to native code.

5) "Write Once, Run Anywhere"

- This is Java's core promise: Java code compiled to bytecode can run on any platform that has a compatible JVM.
- Because the JVM abstracts the underlying hardware and operating system, you do not need to recompile Java code for each platform — the same .class files can run anywhere a JVM exists.

Conclusion

The JVM makes Java portable and reasonably efficient. Class loading, the runtime data areas, the execution engine, and JIT compilation are the basic pieces that work together to run Java programs.

---
This document is intentionally short and introductory. For more depth, explore official JVM specification resources and JVM implementation guides.


