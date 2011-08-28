Application PF
==============

Once upon a time just around New Year and its congratulations in my country called PF (Pour féliciter) I wanted to make a special one: to draw a picture which consists of one line covering nearly whole squared paper.
This line goes always between two opposite cornes of square and the uncovered places made a text "PF 2011".
Some tries made by hand looked horribly, so I made a program.
Coding took more time than expected and some more or less functional versions were produced.
That time I also changed zápočťák for Programming I to this one.
Milan liked it and wanted to add two extra features which completely changed whole design and I doubt there is a line common.

The first change was radical, loading schema from file instead of hard typed "PF 2011".
The second one was to allow speed control of animation.

What it is for
--------------
First of all, you can use this as an editor of simple regular (mostly planar) graphs.
These graphs can be saved to file either as list of edges or as schema to be used in other applications.
Another way how you can use this application is presentation how BFS and DFS go through a graph animated step by step.
But the main and most interesting one is to draw one ot more lines which cover whole graph.
If there exists only one such line, it is called Eulerian path.
Drawing of these paths is animated, so you can watch how it is growing.

How to use
----------
Run application, select New dialog from menu Board and for the first tries use custom one with adjusted size.
You can save your current board to file, this way you don't have to set a new one from scratch.
Later you can change grid type and also its control points (advanced only).
If you are unsatisfied with graph you have, change it in edit mode by dragging cursor along an edge to toggle its present in graph.
You can assign animator to this board or do it later; switch to run mode and enjoy animation.

Board properties
----------------
You can set type of grid, there are four of them available.
* SQUARE (1,1) - The simplest one, the basic formation is a parallelogram.
* DIAGONAL (1,1) - SQUARE with diagonals, which don't intersect each other.
* DIAGONALX (2,2) - DIAGONAL with diagonals intersecting each other.
* TRIANGLE (8,7) - Like SQUARE with only one diagonal line.

To ensure coordinates being integers, each of these types has has different size of regular unit - the smallest piece.
Horizontal and vertical size of regular unit is in brackets.
Set size to these values to see one unit.

Control points are rather misterious.
SQUARE,DIAGONAL,TRIANGLE control points are corners.
DIAGONALX has one control point in the intersection of diagonals to ensure its coordinates are integers.

Regular form defines control points which makes all parallelograms square or triangles equilateral.
Saving to file as schema is supported only in this form.
Free is the opposite - user can change control points.

Pattern determines which edges will the graph contain.

Painting properties
---------------
Each grid consists of several (2..4) directions of parallel lines.
Each of this direction can be configured independently.
Color and stroke can be set for ordinary lines and for main lines (each n-th).
Stroke defines width (part before @) and dash attributes (alternating of paint and gap) (part after @)

Edges can be used and unused and have similar painting possibilities.

Vertices are painted as rings either with filled center or not.
Its diameter and color can be set depending on degree of the vertex. 
For example: paint all odd-degree vertices red.

There is always a row called "common" in settings which shows whether there is a common value in a column.
Setting its value sets all values in that column, exactly like Milan wanted.
