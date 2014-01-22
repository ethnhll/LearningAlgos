package edu.ohio_state.cse.LearningAlgos;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * This is the main driver class for the Learning Agent using reinforcement
 * learning.
 * 
 * @author Ethan
 * 
 */
public class Driver {

	public static void main(String[] args) {
		/*
		 * argument 0 will be the name of the descriptions file, argument 1 will
		 * be the transitions model file
		 */

		String descrip = args[0];
		String transitions = args[1];
		String delimiterComma = "[,]";
		Scanner descripScan = null;
		Map<String, StateNode> stateSpace = new HashMap<String, StateNode>();

		try {
			descripScan = new Scanner(new File(descrip));
			while (descripScan.hasNext()) {
				// Break the input line along the commas
				String[] decripArgs = descripScan.nextLine().split(
						delimiterComma);
				/*
				 * The zero and third indices are what we are interested in to
				 * create nodes
				 */
				Double reward = Double.parseDouble(decripArgs[3]);
				StateNode node = new StateNode(decripArgs[0], reward);
				stateSpace.put(node.getNodeID(), node);
			}
		} catch (FileNotFoundException e) {
			System.out
					.println("There was a problem parsing the description file");
			e.printStackTrace();
		} finally {
			descripScan.close();
		}

		/*
		 * Now we are going to read in the transition model to create our
		 * adjacency lists
		 */
		Scanner transitionScan = null;
		try {
			transitionScan = new Scanner(new File(transitions));
			while (transitionScan.hasNext()) {
				String[] transitionArgs = transitionScan.nextLine().split(
						delimiterComma);
				/*
				 * Now we are first interested in which node whose adjacency
				 * list we will be altering, arg[0]
				 */
				StateNode nodeToAlter = stateSpace.get(transitionArgs[0]);

				char action = transitionArgs[1].charAt(0);
				int i = 2; // using 2 because we have already looked at index 0
							// and 1
				Set<Pair<StateNode, Double>> pairSet = new HashSet<Pair<StateNode, Double>>();
				while (i < transitionArgs.length) {
					/*
					 * Now we look at the arguments in chunks of two, the first
					 * is a NodeID and the second is the probability
					 */
					StateNode adjacentNode = stateSpace.get(transitionArgs[i]);
					Double probability = Double
							.parseDouble(transitionArgs[i + 1]);
					/*
					 * Now create a pair of statenode and probability
					 */
					Pair<StateNode, Double> pair = new Pair<StateNode, Double>(
							adjacentNode, probability);
					pairSet.add(pair);
					i += 2;
				}
				nodeToAlter.addToAdjacencyList(action, pairSet);
			}

			List<StateNode> stateSpaceList = new LinkedList<StateNode>(
					stateSpace.values());
			List<StateNode> alteredUtilitiesList = LearningAgentUtils
					.explore(stateSpaceList);
			Collections.sort(alteredUtilitiesList);
			
			System.out.println("----RESULTS----");
			for (StateNode node : alteredUtilitiesList) {
				System.out.println("State: " + node.getNodeID() + " Utility = "
						+ node.getUtility() + " Policy = " + node.getPolicy());
			}

		} catch (FileNotFoundException e) {
			System.out
					.println("There was a problem parsing the transition file");
			e.printStackTrace();
		} finally {
			transitionScan.close();
		}
	}
}
