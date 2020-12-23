# Solving 0/1 Knapsack Problem in 5 different ways
NOTE:
1. *Algorithm contains items chosen as well, so the time complexity would be different from conventional algorithms* 2. *Do correct me if I am wrong*

**n = Number Of Items && w = Weight of Items**

**N = Number Of Generations && P = Number Of Populations**

Name of Algorithms|Best|Average|Worst|Space Complexity|
| --- | --- | --- | --- | --- |
Dynamic Programming|Ω(nW log(n))|Θ(nW log(n))|O(nW log(n))|O(n)|
Least Cost Branch & Bound|O(n)| O(n) ~ O(2 ^ n) |O(2)|O(n)|
Memoize|Ω(nW log(n))|Θ(nW log(n))|O(nW log(n))|O(nW)
Brute Force|Ω(2n ^ n)|Θ(2n ^ n)|O(2n ^ n)|O(n)|
Genetic Programming|Ω(NP)|Θ(NP)|O(NP)|O(NP)|