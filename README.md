# LabyrinthSolver
Quite self-explanative.

Application designed to create a procedurally generated maze and solve it (e.g. find a path between two arbitrary non-obstacle points if there's one)
The rough result looks the following: 

X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X 
X S * * * * X       X                                                   X                       X   X 
X X X X X * X   X X X   X   X X X X X X X   X X X X X X X X X   X X X X X   X X X X X X X X X   X   X 
X   X     * * *         X           X * * * * * * * * * * * * *         X   X               X   X   X 
X   X   X   X * X X X X X   X X X X X * X   X X X X X X X X X * X X X   X   X X X X X X X   X   X   X 
X   X   X   X * * * * * * * * * * * * * X   X           X * * *     X                       X   X   X 
X   X   X   X X X X X   X X X X X X X   X   X   X X X X X * X X X X X X X X X X X X X X X X X   X   X 
X   X   X   X                       X   X   X           X * X                                   X   X 
X   X   X   X X X X X   X X X X X   X   X   X   X X X   X * X   X X X X X X X X X X X X X X X X X   X 
X       X                   X   X   X   X   X       X     * X   X                                   X 
X X X   X X X X X X X   X   X   X   X   X   X   X X X X X * X   X X X X X X X X X X X X X X X X X   X 
X               X   X   X           X   X   X   X         *     X                                   X 
X   X X X X X   X   X   X   X   X X X X X   X   X   X X X * X X X   X X X X X X X X X X X X X   X   X 
X       X   X   X       X   X               X   X   X   X *         X               X           X   X 
X X X   X   X   X X X X X   X   X   X   X X X X X   X   X * X   X   X   X   X X X   X X X X X   X   X 
X   X   X   X               X   X   X           X     * * * X   X   X   X   X                       X 
X   X   X   X X X X X X X   X   X   X X X X X   X   X * X X X X X   X X X   X X X X X X X X X X X   X 
X   X   X       X   X       X   X   X           X   X * X                   X                       X 
X   X   X   X   X   X   X   X   X   X   X X X X X   X * X   X X X   X X X   X   X X X X X X X X X   X 
X   X   X   X       X   X   X   X   X   X       X   X * X       X   X   X   X                       X 
X   X   X   X X X X X   X   X   X X X   X X X   X   X * X   X   X   X   X   X   X   X X X X X X X   X 
X   X   X               X   X               X   X   X * X   X   X   X   X   X   X               X   X 
X   X   X   X X X X X X X   X X X X X   X   X   X   X * X X X   X   X   X   X   X X X X X X X X X   X 
X   X   X               X               X   X   X     *         X           X   X                   X 
X   X   X X X   X X X X X X X X X X X   X   X   X X X * X X X X X   X X X   X   X   X X X X X X X X X 
X       X   X   X                       X   X         * * *             X   X   X       X           X 
X   X X X   X   X   X   X X X X X X X X X   X   X X X   X * X X X X X   X X X   X   X   X   X X X   X 
X   X           X   X   X                   X       X   X * X       X           X   X   X           X 
X   X X X X X   X   X X X   X X X X X X X X X X X   X   X * X X X   X X X X X X X X X   X X X   X   X 
X               X       X   X                       X   X * * * * * * * * * * * * * X       X   X   X 
X X X X X X X   X X X   X   X   X X X X X   X   X   X   X X X X X X X X X X X X X * X   X   X   X   X 
X                   X       X           X   X   X   X                       X   X * X   X   X   X   X 
X   X X X X X   X   X X X   X   X X X X X   X X X   X X X X X X X   X X X   X   X * X   X   X   X   X 
X       X   X   X   X                   X   X       X           X       X   X   X * X   X   X   X   X 
X   X   X   X   X   X   X   X X X X X   X   X   X X X   X X X   X   X X X   X   X * X   X   X   X   X 
X   X   X   X   X   X   X       X       X           X   X   X   X           X   X * X   X   X   X   X 
X   X   X   X   X   X   X   X X X   X X X X X X X X X   X   X   X   X X X X X   X * X   X   X   X   X 
X   X   X   X   X                   X   X                       X   X           X * X   X   X   X   X 
X   X   X   X   X X X   X   X X X X X   X   X X X X X   X X X X X   X   X X X X X * X   X X X   X   X 
X   X   X   X   X       X               X           X   X           X   X * * * * * X           X   X 
X X X   X   X   X X X   X X X X X X X X X   X X X X X   X   X X X   X   X * X X X X X X X X X   X   X 
X       X   X                                           X       X   X   X * X               X   X   X 
X   X X X   X X X X X   X X X X X X X X X   X X X X X X X X X X X   X   X * X   X X X   X   X   X   X 
X   X   X                   X                                       X   X * X   X       X   X   X   X 
X   X   X   X X X X X   X   X   X X X X X   X   X   X X X X X X X X X   X * X   X X X   X   X   X   X 
X   X   X   X       X   X   X           X   X   X   X                   X * * * * * X   X   X   X   X 
X   X   X   X   X   X   X   X   X X X X X   X X X   X   X   X X X X X X X X X   X * X   X   X X X   X 
X   X           X   X                               X   X   X               X   X * * * * * * * X   X 
X   X X X   X X X X X X X   X X X X X X X   X X X X X   X X X   X X X X X   X   X X X X X   X * X   X 
X                                           X                           X                   X * * F X 
X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X 
