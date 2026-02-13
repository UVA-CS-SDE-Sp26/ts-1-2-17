TopSecret
2/13/26
Leonardo Valli, Fal Elgizouli, Youssef Youssef

This program entry point will be the TopSecret class.

To run, enter ts-1-2-17 directory, and type the following into the terminal (any args you want to pass should follow "TopSecret" below):
./gradlew build
java -cp build/classes/java/main TopSecret


This program takes up to two arguments:
the number of a file in the data directory (indexed starting at 1)
a key for deciphering text files, ignored by our program

The program prints the contents of the file specified by the user to the terminal

Specifics:
UI specifics documented in userinterface.txt
Software design plans documented in filehandler.txt

SHORT PSEUDOCODE
if no args:
    print list of files in data directory, numbered starting at 01
else:
    fileNumber = first arg
    call FileHandler's methods to print the contents of the proper file

TESTS
Description of tests documented in programcontrol.txt
