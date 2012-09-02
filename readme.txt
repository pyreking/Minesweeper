This is my Minesweeper clone made in Java. 

Originally a school project, I decided to go above and beyond 
what I was told to do in order to recreate the most authentic 
Minesweeper expirence.

There are two available difficulties.
1) Beginner - A standard 9x9 board with 10 mines.
2) Advanced - An intermediate 16x16 board with 40 mines.

There are two versions of this program.
1) Application - A standard desktop program. This is the 
recommended version. The executable .jar file can be found in 
application/bin.
2) Applet - A version playable in your browser. Unfortunately, 
this causes many different problems so I do not test it as 
thoroughly as I do for the main version. The .html and 
accompanying .jar file can be found in applet/bin.

If you would like to compile the program by yourself, keep in 
mind a couple of things:
1) javac MinesweeperApplication.java compiles the program.
2) java MinesweeperApplication runs the program.
3) You'll have to move the res folder to the same location as 
the .class files in order for the program to work correctly.
4) If running MinesweeperApplication.jar from the command line, 
type: java -jar [full path to the jar file].

This code is licensed under the Open Software Licence 3.0.