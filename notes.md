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
Compiled code: fast
Interpreted code: portable

Java is a compiled and interperted language, compiled code is portable and fast.
The Java source code is first compiled into binary byte code, then a JRE runs the application
So you can proablby share this binary byte code with the homies and they can run it on any JVM.

Inline functions, are used when they are callled multiple times.
C++ doesn't have heaursitc statistics
Hotspot VM recompiles code while it's running.
Generational garbage collection: 
ethier need memory onetime or everytime.

4. How to compile and run Java code
By hitting the green arrow on InteliJ
Java name is the same name as the class name MyClass and MyClass.Java = source file, MyClass.class = executable file
One parameter, which is a string array.
public static void main (String...args).

public class SimpleJavaClass {
	public static void main (String [] args) {
		System.out.println("Hello BYU!);
	}
}
A function that is inside of a class is a method.



In Java, everything goes into a class. 

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
shared: a code library that contains the rules and representations of a chess game (both client and server code.)

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

Your're turning your chess board into a string to back and forth.
What is the difference between static and non-static methods?
//    public static void main (String[] args) {
//       ChessPiece My_Pawn = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
//    }
Save Equals and Hash Code Test for Monday. Or, go to the TA labs instead.

TODO
To String for Chess Board and Piece (White is uppercase)
ass in an arraylist of possible moves.

Notes
King can move every combination, to stop reusing code
Pawn one step forward for middle of the board
Pawn Piece two step forward for same color 

ChessPiece.PieceType
Got stuck, realized a pawn cannot leapfrog another player 
and when it reaches the end, need to return every possible promotion type.

5.6.2024 Lecuture Notes
class is the blueprint for the house, the object is the instance.
To create an object means allocating the space for it's variables.

Create an object(instance of the class) with the new keyword, followed by a constructor invocation
Strings and Arrays are objects, but they aren't created with a constructor.

Object's get allocated in the heap
Date dt; --> makes dt 0x123
dt = new Date(); --> Heap goes to day: 1, month: 1, year: 2018
dt --> it

Object References do not allow pointer arithmetic.

Even though the objects have the same values, becuase they have different addresses, == is false.
equals is the same thing as == by default (reference equality) You have to write the code to do that.

Instance: Each object (instance) get its own copy, ex: allows two date objects to represent different dates.
Instance Methods: Associated with the object.

Static Variables: Associated with the class and not the instance.
Static Methods: If you're making everything static, you might as well program in c++
If you're inside of a static method, you need to make an instance inside of youe main method which you can call.

Object Oriented Programming: Classes. 

Getters and Setters: 
public class Person {
private String firstName;
Use right click and generate getters and Setters
return firstName
this.firstName = firstName

public static final is a constant
Constructers: Code executed at object creation.
MyClass{
public MyClass {
x = 5;}
}

Constructers are not Inherited becuase it means you as a programmer don't lose control.
Method Overiding:  Replaces an inherited method by redefining it. (Argument list must be the same.)
Extends is how you use inheritance, To access class functions inside of another class.

Common Methods to Override: 
public String toString(); --> return "first name" + first name
public boolean equals(Object obj); --> 1. type check, 2. reference check, 3. Actually checks eqaulity.
public int hashCode(); --> if two objects are equal according to equal, the calling hashcode must produce the same integer result
							Its not garaunteed that two unequuel objects have unequeal hashcodes.

Final Keyword: Final Variables can't be changed after a value is assigned.
public final int myVariable = 10; (eqivalent of a constant)
public final ArrayList list = new ArrayList(); means that the reference is final.
This refers to the current object. 
Enum: public enum Gender {
			Male, Female;
			@ Override
			public String toString().
Chess Piece Enum. Values that are acceptable and no other values can be used. 

Object-Oriented Design Overview:
-Decompose a Progrma into Classes
Not supposed to make a KING, QUEEN, ROOK, KNIGT, BISHOP, PAWN class.
Something about de-cerializing. (Is-A, Has-A and Uses-A).

Class Design Guidelines:
-Keep Data private
-Not alll fields need individual getters and Setters
-Break up classes that have too many responsibilities.
-Class have noun names, methods usually have verb names.
-Use Static methods as an exception, not a general rule.

Standard Class Structure:
	public class MyClass {
	//Static Variables
	//Instance variables
	//Main Method (if it exists)
	//COnstructors
	//Methods (gruoped by functionality)
	
Records:
Automatic Getters, Equals, hashcode, toString. 

Programming Exam: Uphold whole code and screensshots of test cases passing.
Start at the same starting point as you did in Phase 0.
Page of Notes, I want the function implimentations.
	
Java Collections:
Interfaces are things that provide a signature foro a method body
<<interace>> List (index, object), <<interface>> Set, <<interface>> Queue (order you put them in)

List: A Sequence of ele,emts accessed by index.
Linked List(Doubly-linked list implementation)

Set: A collection that contains no duplicates
add(value), contains(value), remove(value)

Queue: for saving things up,
A collection for holding elements prior to processing

Deque: queue that supports insertion and removal at both ends.
Stack (Is Deprecated): If you need a stack, use a Deque
Removed becuase it's thread safe, which makes it slower.

push() => Deque.addFirst()
pop() => Deque.removeFirst()
peek() => Deque.peekFirst()

Map: A Collection that maps keys to values
put(key, value), get(key), contains(key), remove(key)

Each element is iterable inside of an interface.
Override equals method to correctly use contains.
Hash Code is used to assert equals, so change it.

Sorted Collections: 
Question: Difference between class and method

Copying Object: A shallow copies the reference values to the object
A deep copy takes those references and kaoes copies out of them.
Copy the objects and all of the references assoicaited with it.

Immutable Objects: WHen making a copy, immutable (unchangeable) objects do not need to be copied
Strings, Integer, Boolean, Double,        Copy Constructror

The reason it's safe to make a shallow copy is if the references are immuttable.
public Object clone() {return super.clone();

Deep Copy:
Person2 clone = (Person 2) clone()
Date cloneBirthdate = (Date) get Birthdate().clone();
clone.setBirthdate(clonedBirthdate);

Clones the person and then sets the value birthdate to new birthdate reference of same birthday.
How would I declare a implimentation of a set iterator?

Defining a class inisde of another class:
if the class is only useful inside of that code.
Declare things as close as possible as they're used.

private static class DataStructureIterator: best placed to put the Code
overide has next, return incremennt += 1 and set increment += 1. (next += increment)

local inner class: delcare a class inside of the method that needs it.
local inner classes can only access final or effectively final variables.

return new interface() {overides}
TODO: Videos 30:35 minutes before Monday.