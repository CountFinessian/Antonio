# My notes
1. Ways that Java is different from C++
The integer types in C and C++ programs depend on the processor for which a program is compiled
The Java Operators and control structures are very similar to those of C or JavaScript.

2. How to get and install Java and an IDE
You get it from the Oracle website.

3. Why Java is portable and fast
The Java source code is first compiled into binary byte code, then a JRE runs the application
So you can proablby share this binary byte code with the homies and they can run it on any JVM.

4. How to compile and run Java code
By hitting the green arrow on InteliJ

5. The primitive data types available in Java
Java has eight primitivie types: four signed integral types, two floating-point types, char and boolean.
byte = -128 to 127, short = -32,768 to 32,767, int =-2,147,483,648 to 2,147,483,647, long is too long (8 bytes)
These denote fractional parts --> float 4 bytes, double 8 bytes 
A char is a UTF-16 character encoding while a boolean has two true or false values.

6. What's the difference between creating a string
with new and creating one by just specifying it in double quotes?
New allocates space on the heap, doube quotes may pass a pointer.

7. How to declare, create and intialize arrays
int[] numbers = {1, 2, 3, 4,5};

8. How to find the length of an array
int length = numbers.length;

9. How to create and access arrays of arrays
ArrayList<String> friends = new ArrayList<String>();

10. How to specify command-line parameters in IntelliJ
11. The relationship between Packages, Imports and the CLASSPATH environment variable

12. How to use a Scanner to read a text file of words, seperated by whitespace 
var in = new Scanner(pathm StandardCharsets.UTF-8);
while (in.hasNextDouble()) {
double value = in.nextDouble()

When you call System.out.println, output is sent to the "standard output stream" and showsup in the terminal window
A Scanner tied to System.in lets you read terminal input.

OutputStream outStream = ...;
var out = new OutputStreamWriter(out Stream, charset);
out.write(str);

Files.copy(fromPath, toPath);
Files.move(fromPath, toPath);

Operators: & (and), ^ (or)

Built-in garbage collection
References instead of pointers

server: a program that handles network requests to play and create games.
client: the command line program users use to create and play a game.
shared: a code library that contains the rules and representations of a chess game

what are object overrides inside of Java?
For example, in class, we go. Code > Generate ... > equals() and hashCode()

