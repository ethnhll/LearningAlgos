package edu.ohio_state.cse.LearningAlgos;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Provides a basic structure for a state which maintains an ID, an updating
 * utility, the reward for that state, the policy (or best action that state can
 * currently make), and an adjacency list of all the states reachable from this
 * state.
 * 
 * @author Ethan Hill
 * 
 */
public class StateNode implements Comparable<Object>{

	/**
	 * an identifier for this state
	 */
	private String nodeID;

	/**
	 * the adjacency list of states reachable from {@code this}
	 */
	private Map<Character, Set<Pair<StateNode, Double>>> adjacencyList;

	/**
	 * the overall utility that is updated by the bellman equation as an Agent
	 * learns the state space
	 */
	private double utility;

	/**
	 * the benefit of a state, indicates terminal states
	 */
	private double reward;

	/**
	 * the best action this state can take
	 */
	private char policy;

	/**
	 * the last iteration where this state saw a change in utility
	 */
	private int iterationSinceLastChange;

	/**
	 * Class constructor
	 * 
	 * @param nodeID
	 * @param reward
	 */
	public StateNode(String nodeID, double reward) {

		this.nodeID = nodeID;
		this.reward = reward;
		this.adjacencyList = new HashMap<Character, Set<Pair<StateNode, Double>>>();
		this.utility = 0d;
		this.policy = '!';
		this.iterationSinceLastChange = 0;
	}

	/**
	 * Returns the reward associated with visiting this state
	 * 
	 * @return the reward associated with this particular state
	 */
	public double getReward() {
		return this.reward;
	}

	/**
	 * Returns the current utility of this state
	 * 
	 * @return the current utility of this state at the current time
	 */
	public double getUtility() {
		return this.utility;
	}

	/**
	 * Assign the utility value of this state to a new value
	 */
	public void setUtility(double utility) {
		this.utility = utility;
	}

	/**
	 * Add adjacent nodes and their various other required information needed
	 * for processing and exploring
	 */
	public void addToAdjacencyList(Character action,
			Set<Pair<StateNode, Double>> stateProbabilityPairs) {

		this.adjacencyList.put(action, stateProbabilityPairs);
	}

	/**
	 * Returns the unique identifier for this state
	 * 
	 * @return the identifier for this state
	 */
	public String getNodeID() {
		return this.nodeID;
	}

	/**
	 * 
	 * @return
	 */
	public Map<Character, Set<Pair<StateNode, Double>>> getAdjacencyList() {
		/*
		 * give a copy of the adjacency list, we don't want this list to be
		 * altered in anyway outside of this class
		 */
		return new HashMap<Character, Set<Pair<StateNode, Double>>>(
				this.adjacencyList);
	}

	/**
	 * Sets the best action that this state can currently make
	 * 
	 * @param policy
	 *            the best action this state can currently make
	 */
	public void setPolicy(Character policy) {
		this.policy = policy;
	}

	/**
	 * Returns the action corresponding to state to move to with the highest
	 * utility
	 * 
	 * @return the action that corresponds to the highest utility state to move
	 *         to
	 */
	public char getPolicy() {
		return this.policy;

	}

	/**
	 * Sets the iteration number of the last time this state saw a utility
	 * change
	 * 
	 * @param iteration
	 *            the iteration number at which this state saw a change in
	 *            utility
	 */
	public void setLastChangeIteration(int iteration) {
		this.iterationSinceLastChange = iteration;
	}

	/**
	 * Returns the last iteration number of most recent utility change
	 * 
	 * @return the iteration number of the last time this state saw a change in
	 *         utility
	 */
	public int getLastChangeIteration() {
		return this.iterationSinceLastChange;
	}

	public int compareTo(Object obj){
		assert obj.getClass() == this.getClass(): "obj did not match class";
		StateNode objNode = (StateNode) obj;
		return (Integer.parseInt(this.nodeID) - Integer.parseInt(objNode.getNodeID()));
	}
}
