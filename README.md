# Distance-vector routing simulation
This project was developed for the Computer Networks course at unibz.

The aim of this simulation is to show how routing tables are computed given
a topology file in the following form:
```
RouterA RouterB 3
RouterB RouterA 2
RouterC RouterB 1
RouterB RouterC 4
```
and a file containing the order in which distance vectors should be exchanged:
```
RouterA RouterB RouterC
```

The topology file is used in order for each node to know its neighbours, which is needed
in order for the distance vectors exchange to be done.

The messages file is used to specify in which order each router will send its distance vector to his neighbours.

The algorithm used for evaluating the distance vectors uses the Bellman-Ford equation in order to find the shortest paths.

TL;DR: the shortest paths are going to be computed with respect to the messages order specified in the file, therefore
the optimal solution is not guaranteed to be always found. This is exactly what happens in real networks where the delays,
link failures... can lead to routing loops and slow convergence.

For more information about distance-vector routing you can look on [wikipedia](https://en.wikipedia.org/wiki/Distance-vector_routing_protocol).