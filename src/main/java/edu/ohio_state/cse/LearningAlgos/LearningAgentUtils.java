package edu.ohio_state.cse.LearningAlgos;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

/**
 * Provides various functionality to the Learning Agent.
 * @author Ethan Hill
 * 
 */
public class LearningAgentUtils {

	/**
	 * Threshold for no change in utility
	 */
	private static final int NO_CHANGE_THRESHOLD = 50;
	/**
	 * Given that GAMMA is 1, as per instruction
	 */
	private static final double GAMMA = 1d;

	/**
	 * iteration counter
	 */
	private static int iterations = 0;

	/**
	 * restart counter
	 */
	private static int restartCounter = 0;

	// prevent instantiation
	private LearningAgentUtils() {
		// no code necessary
	}

	private static boolean isTerminalState(StateNode state) {
		/*
		 * In general, a state with no adjacency list is a terminal state
		 */
		return state.getAdjacencyList().isEmpty();
	}

	public static List<StateNode> explore(List<StateNode> states) {

		int rand = new Random().nextInt(states.size());
		StateNode currentNode = states.get(rand);
		boolean done = false;

		while (!done) {

			/*
			 * Here we determine whether the states have converged by checking
			 * the iteration on which they last were changed. If the difference
			 * between the current iteration and the iteration that the utility
			 * was last changed
			 */
			int passedThresholdCount = 0;
			for (StateNode node : states) {
				int deltaIteration = iterations
						- node.getLastChangeIteration();
				if (deltaIteration >= NO_CHANGE_THRESHOLD) {
					passedThresholdCount++;
				}
				if (passedThresholdCount == states.size()) {
					done = true;
				}
			}

			if (isTerminalState(currentNode)) {
				restartCounter++;
				currentNode.setUtility(currentNode.getReward());
				// Output the current state of affairs
				System.out.println("State: " + currentNode.getNodeID()
						+ " Best: Terminal! " + currentNode.getUtility()
						+ " Restart Count: " + restartCounter);

				rand = new Random().nextInt(states.size());
				currentNode = states.get(rand);

			} else {
				bellmanUpdate(currentNode);
				/*
				 * Looks a little convoluted but we need to go down into the
				 * adjacency list to find the next node with the highest utility
				 * to explore. The maxUtility is set initially to -2 (an
				 * otherwise impossible utility) so that we may find the node
				 * with maximal utility.
				 */
				StateNode best = null;
				double maxUtility = -2d;
				Set<Pair<StateNode, Double>> pairs = currentNode
						.getAdjacencyList().get(currentNode.getPolicy());
				for (Pair<StateNode, Double> pair : pairs) {
					if (pair.getLeft().getUtility() > maxUtility) {
						maxUtility = pair.getLeft().getUtility();
						best = pair.getLeft();
					}
				}
				currentNode = best;
				iterations++;
			}

		}

		return states;
	}

	private static void bellmanUpdate(StateNode currentNode) {
		/*
		 * Sum for each action, probability * utility of adjacent states
		 */
		List<Double> sums = new LinkedList<Double>();
		Map<Character, Double> actionSums = new TreeMap<Character, Double>();
		for (Character action : currentNode.getAdjacencyList().keySet()) {
			double sum = 0d;
			for (Pair<StateNode, Double> stateProbPair : currentNode
					.getAdjacencyList().get(action)) {
				/*
				 * Get the adjacent nodes utility and multiply it by the
				 * probability of that reaching that node from an action.
				 */
				sum += (stateProbPair.getLeft().getUtility() * stateProbPair
						.getRight().doubleValue());
			}
			actionSums.put(action, Double.valueOf(sum));
			sums.add(Double.valueOf(sum));
		}

		// Now find the largest sum
		Collections.sort(sums);
		int posOfMax = sums.size() - 1;
		double maxSum = sums.get(posOfMax);

		// Find an action that corresponds to that sum
		for (Map.Entry<Character, Double> entry : actionSums.entrySet()) {
			if (Double.compare(entry.getValue(), maxSum) == 0) {
				currentNode.setPolicy(entry.getKey());
			}
		}

		// Output the current state of affairs
		System.out.print("State: " + currentNode.getNodeID() + " Reward: "
				+ " Best: " + currentNode.getPolicy() + " Old Utility: "
				+ currentNode.getUtility() + " New Utility: ");
		double newUtility = currentNode.getReward() + GAMMA * maxSum;
		if (currentNode.getUtility() != newUtility) {
			currentNode.setLastChangeIteration(iterations);
		}
		currentNode.setUtility(newUtility);
		System.out.print(currentNode.getUtility() + "\n");
	}

}
