# My notes
1. Ways that Java is different from C++
C++ is the parent of Java.
The integer types in C and C++ programs depend on the processor for which a program is compiled
The Java Operators and control structures are very similar to those of C or JavaScript.
Data types can be different sizes for different platforms (C and C++)
In C and C++, strings are character arrays, while in Java, they're an object.

IN C++, there are no checks for the boundaries of an array.
Generational Garbage Collector: garabage collection checks newer memory.

2. How to get and install Java and an IDE
Download Oracle JDK
JVM is the interpereter
Get the Inteliji Idea for the Java IDE

3. Why Java is portable and fast
Java is a compiled and interperted language, compiled code is portable and fast.
The Java source code is first compiled into binary byte code, then a JRE runs the application
So you can proablby share this binary byte code with the homies and they can run it on any JVM.

4. How to compile and run Java code
By hitting the green arrow on InteliJ
Java name is the same name as the class name MyClass and MyClass.Java
One parameter, which is a string array.
public static void main (String...args)

5. The primitive data types available in Java
Java has eight primitivie types: four signed integral types, two floating-point types, char and boolean.
byte = -128 to 127, short = -32,768 to 32,767, int =-2,147,483,648 to 2,147,483,647, long is too long (8 bytes)
These denote fractional parts --> float 4 bytes, double 8 bytes 
A char is a UTF-16 character encoding while a boolean has two true or false values.
int anInt = 5; byte aByte = -128; 

6. What's the difference between creating a string
with new and creating one by just specifying it in double quotes?
New allocates space on the heap, doube quotes may pass a pointer.
System out.print(aFloat + ", " + aDouble);

7. How to declare, create and intialize arrays
int[] numbers = {1, 2, 3, 4,5};
int[] intArrays and intArray2.length
system.out.print(/b/b)


8. How to find the length of an array
int length = numbers.length;

9. How to create and access arrays of arrays
ArrayList<String> friends = new ArrayList<String>();
squares[position.getRow()][position.getColumn()] = piece;
Two Dimensional Array in Java: int[][] myArray = new int[4][2];

10. How to specify command-line parameters in IntelliJ
11. The relationship between Packages, Imports and the CLASSPATH environment variable
Packages provide a way to organize classes into groups, 

12. How to use a Scanner to read a text file of words, seperated by whitespace 
var in = new Scanner(pathm StandardCharsets.UTF-8);
while (in.hasNextDouble()) {
double value = in.nextDouble()
Scanner scanner = new Scanner(file)
A scanner.hasNext() will just pass in the next string 

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

Git Notes (4/30)
Linus Torvalds was mad about not being able to track his changes.
It's a subversion repository for a directory. (Can go back to any previous commit)
You can have a team project and have one common repo that everyone checks their code into.]
Git can even tell if there is a problem; ie. top of code and bottom of code is changed.

mkdir gitTest && cd gitTest
git init
ls -la
create a text file and edit it and git add it
git log
make a new change, modify and commit it
use git head and the two hashes to compare the versions
then you can revert to the old version of the code.
Git branch and merge when you're done working on it. 
git checkout main, git branch (your name) (your hash)
branch is the same thing as the main that it's taking from
git checkout, you want to know what git does. 
To list the branches, use git branch.
git rm t1.txt, now that file doesn't exist. 
git commit -m "deleted t1.txt"
git checkout -b thirdBranch (branch from main)
git add t1.tx, git commit -m "another change"
git merge thirdBranch

rebase: allows you to change the order of things (don't mess with it)
git mv t1.txt file1.txt
Github Assignment. GitRemote Add.WHy COnnects local git repo to remote (online) git repo.

Project 0 Chess Moves Notes:
Piece Moves doesn't account for pieces in it's place.
You're supposed to overide the To string method for easier debugging
How do I overide Code > Generate... > equals() and Hashcode()?


