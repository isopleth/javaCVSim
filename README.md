This is a port of the CVSim 1.16 cardiovascular simulation program to pure
Java.  CVSim had a Java GUI and a C simulation engine; this program has the
simulation engine ported to Java and some small changes made to it, mainly
to improve readability.

## Table of Contents
- [Installation](#installation)
- [Documentation](#documentation)

Installation
------------

The code is written in Java.  These are the instructions for a Linux terminal window.

- Run ```git clone https://github.com/isopleth/javaCVSim.git``` to obtain the source code
- ```cd javaCVSim/jCVSim``` to change the default directory to the source tree
- Run ```ant``` to build the project using build.xml and the sources in the src tree
- Run ```java -jar dist/jcvsim.jar``` (or the Bash script ```run-jCVSim```) to run the program

Alternatively there is a copy of the jar file in the dist directory.

Documentation
-------------

This is a port of the original Java/C version from
https://physionet.org/physiotools/cvsim/ to pure Java, with some
changes to improve on the readability of the source code of the
original. The Infonode library is included as source code, and the
Jdesktop library is not included as it is no longer needed.


The code also contains a few trivial changes compared to CVSim 1.16,
the only one of note is a fix to an array length+1 error in the
Turning algorithm code.
