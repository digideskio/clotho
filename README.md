Clotho
========
* Project URL: https://github.com/omalley/clotho
* Owen O'Malley 
* twitter: [@owen_omalley](https://twitter.com/owen_omalley)
* web: [Owen](http://people.apache.org/~omalley)

This project is a tool to look at your Minecraft saved games with both
a JSON view of the save file and a map view of the explored world. The
file reading code was based on mcmodify, but I re-wrote a bunch of the
parser so that unknown objects didn't crash the parser and would be
written back out the same way. This allows the parser to read current
Minecraft version save games and is likely compatible with future
versions.

Here is an example of a [generated
map](https://raw.githubusercontent.com/omalley/clotho/master/Creative-map.png).
Each map color has 4 variants and the variant picked denotes the
altitude. For example, grey is low ice and white is high ice.

The name Clotho is from the youngest Greek Fate that spins the cloth
of destiny.

## Building:
1. install java 7 and maven 3.
2. Use "mvn package" to build the jars

## Running:
1. Use "java -jar target/clotho-1.0.0-jar-with-dependencies.jar level *save*" 
to create a JSON document with the information from the *save* directory.
2. Use "java -jar target/clotho-1.0.0-jar-with-dependencies.jar map *save*" 
to create a map of your world.
