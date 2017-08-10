## Boggle Game Solver Summary:

### Introduction
* This project focus on writing a program to solve/play the word game Boggle®. Rules and more details about Boggle  Game can be found [here](https://en.wikipedia.org/wiki/Boggle).
* `BoggleBoard` is provided in algs4 assignment  to represent Boggle game boards. It includes constructors for creating Boggle boards from either the 16 Hasbro dice, the distribution of letters in the English language, a file, or a character array.
* `Boggle solver` is created and improved to find all valid words in a given game board, using a given dictionary. It is used a computer opponent in the interactive game.
* Interactive game interface of Boggle Game is shown as below:

<div align=center>
	<img src="http://i.imgur.com/vvmP6z7.png" width="40%"  />
	<img src="http://i.imgur.com/1KYFQye.png" width="40%" />
</div>

*  Following is a 5 x 5  board Boggle game. 

<div align=center>
	<img src="http://i.imgur.com/6OzFyhy.png" width="50%" />
</div>

#### Implementation
* __Dictionary__: select `trie` as data-structure for dictionary. 
* __Get All Valid Word__: typical recursive `Depth First Search` is used in this project to find out all the valid words in giving dictionary.
* This project is based on algs4, please check [Instruction](http://coursera.cs.princeton.edu/algs4/assignments/seamCarving.html) and [Check List](http://coursera.cs.princeton.edu/algs4/checklists/seamCarving.html) for more details. 
#### Optimization log
* All the test run-time is average run-time for solving _10,000 random game boards_ using [yawl dictionary]().
* __version 0.0__: 
	* use 26-way trie and DFS. __test run-time: 7.662(s)__
* __version 0.1__: 
	* use `ArrayList` instead of `HashSet` to store adjacent letters. __test run-time: 2.963(s)__
* __version 0.2__: 
	* in DFS search, directly call DFS on each adjacent letter instead of using for-loop for recursion.  __test run-time: 2.484(s)__
* __version 0.3__: 
	* using `String` instead of `StringBuilder`  __test run-time: 2.344(s)__
* __version 0.4__: 
	* instead of go through board and store each letter into a 2D array, save board as an instance variable, in DFS search directly call `board.getLetter()`.
	* __test run-time: 2.117(s)__
* __version 0.5__:
	* save result as an instance variable. __test run-time 2.168(s)__ no obvious improvement.
* __version 0.6__:
	* using LinkedList to store vaild words.
	* In Tries Class, combine `contains` and `isPrefix` into `get` method. To avoid duplicate results, in `Trie`, when `get(String)` has value of 1, change the value of this trie node to 2, and record that node in a `LinkedList`. 
	* In DFS, stop search when value is -1(not in the dictionary for sure), add to result if value is 1,  for other values (0 or 2), continue DFS search. After each DFS search, reset those node back to 1. 
	* __test run-time: 1.9(s)__
* __version 0.7__: 
	* merge `Tries` into the `BoggleSolver` Class.
	* __No improvement__
* __version 1.0__:
	* use private class `Node` for DFS search: 
		* In every recursive call, instead of starting all over again from root of trie, new version of DFS continues searching from current node. Search ends till a null node is hit. Note: Need to check if node is NULL or MARKED first in DFS
	*   Since node with Q need to go one step further to node U, if Node Q itself is already NULL, node.next[] will throw a `NullPointerException`. So after Node Q is update to Node U, a second NULL check is a must.
	* __test run-time: 1.05(s)__