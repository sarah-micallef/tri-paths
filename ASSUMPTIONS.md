# Assumptions

The following assumptions are made:
* The graph is connected.
* There is only one minimum path.
* The `GraphTestWrapper#getSuccessorsByWeight` assumes that the successors of a given node have a unique weight. This assumption is only being made in the test and not the implementation. Should the test data need to be changed, the test must be refactored accordingly.