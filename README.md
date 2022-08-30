# Six-degrees-of-kevin-bacon

Six Degrees of Kevin Bacon or "Bacon's Law" is a parlour game based on the "six degrees of separation" concept, which posits that any two people on Earth are six or fewer acquaintance links apart. Movie buffs challenge each other to find the shortest path between an arbitrary actor and prolific actor Kevin Bacon. It rests on the assumption that anyone involved in the Hollywood film industry can be linked through their film roles to Bacon within six steps. In 2007, Bacon started a charitable organization called SixDegrees.org. 
The Bacon number of an actor is the number of degrees of separation he or she has from Bacon, as defined by the game. This is an application of the Erdős number concept to the Hollywood movie industry. The higher the Bacon number, the greater the separation from Kevin Bacon the actor is.

The computation of a Bacon number for actor X is a "shortest path" algorithm, applied to the co-stardom network:

If an actor had not been in the same sub-network, his/her Bacon number is 0.
Those actors who have worked directly with Kevin Bacon have a Bacon number of 1.
If the lowest Bacon number of any actor with whom X has appeared in any movie is N, X's Bacon number is N+1.

Implemented server/client model of software architecture and maven to design a java application in finding the shortest path between Kevin Bacon and a given actor (via shared movies) and utilized NoSQL/Graph Database (Neo4j) and created REST API endpoints that are supported by Neo4j graph databases.
