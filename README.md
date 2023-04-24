## Compiler Design Project
# Project Work
This project is a compiler for a programming language that supports integers and floats. The compiler is capable of:
- Detecting complex tokens
- Analyzing syntax
- Generating an Abstract Syntax Tree (AST)
- Performing semantic checks
- Generating a symbol table
- Generating Moon code, which is the input for the Moon machine
- Running the Moon machine to execute the generated code and produce output

# Usage
To use this compiler, follow these steps:
- Install the Moon machine on your system.
- Clone this repository to your local machine.
- Open a terminal and navigate to the root directory of the project.
- Run the following command to compile a file named test.cmp:
./fileName.src

This will generate a file named test.moon, which is the input for the Moon machine.
- Run the following command to execute the generated code and produce output:
moon test.moon lib.m

# Language Features: 
The language supported by this compiler has the following features:

- Variables: The language supports variables that can hold integer or float values.
- Arithmetic Operators: The language supports addition, subtraction, multiplication, and division operators for both integer and float values.
- Comparison Operators: The language supports less than, greater than, less than or equal to, greater than or equal to, and equal to operators for both integer and float values.
- Control Flow: The language supports if-else statements and while loops for controlling the flow of execution.
Implementation Details
This compiler is implemented in Python, and uses the PLY library for lexical and syntactic analysis. The generated AST is checked for semantic correctness, and a symbol table is generated to keep track of variables. The Moon code generator converts the AST to Moon code, which is a simplified version of the Three-Address Code (3AC) used by many compilers. The Moon machine is a virtual machine that can execute Moon code, and is used to run the generated code and produce output.

# Future Work
Possible future improvements to this compiler include:
- Adding support for more data types, such as strings and booleans.
- Adding support for more control flow structures, such as for loops and switch statements.
- Optimizing the generated code to make it more efficient.
