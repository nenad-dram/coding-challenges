# Space Surface Mapper

We are building software for space colony ship. Ship has landed on a rocky area sometimes flooded
with water. We have area map in file (file name passed in command line param).

Sample map:

_000130210  
001240145  
00255#220  
001121110_

Legend:  
_0-9_ – terrain square. Number reflects how height of this square. 0 – zero meters from base level,
9 – 90 meters from base level.  
_#_ - colony ship. 200 meters high

The task is to develop a Java command line program that takes a file name with terrain as the first
input,
flood level in meters (could be from 0 to 80) as a second input and outputs the original map where
squares that are suitable for building houses are marked as @.

Square is suitable for building a house if it is not flooded, and we can build a road to this
square.
Road is buildable only between
colony ship and any square near the ship between squares with height difference smaller or equal to
10

For instance, for a given map and flood level 15, result should be:

_0001@0210  
0012@0145  
002@@#@@0  
001121110_  