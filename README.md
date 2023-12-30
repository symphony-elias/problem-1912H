# Description

This is an attempt to solve problem [1912H](https://codeforces.com/problemset/problem/1912/H).
It tries to find a solution and returns the first encountered one, regardless of the number of jumps. The algorithm is
recursive and tries all possible city pairs until a solution is found.

The [Main.java](./src/main/java/org/ecroze/Main.java) file was submitted to codeforces without the
`package` statement but lead to a wrong answer on test 3 (suboptimal solution).

# How to build and run

To build and run tests: `mvn clean package`.

To execute: `mvn exec:java -Dexec.mainClass="org.ecroze.Main"`
