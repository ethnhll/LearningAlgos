package edu.ohio_state.cse.LearningAlgos;

/**
 * This class is intended to be a generic structure for immutable Pair objects.
 * <p>
 * Based off of a Pair design found <a href=
 * "http://stackoverflow.com/questions/521171/a-java-collection-of-value-pairs-
 * tuples">here.</a>
 * </p>
 * 
 * @author Ethan Hill
 * 
 * @param <L> the left object in a Pair
 * @param <R> the right object in a Pair
 */
public class Pair<L, R> {

	/**
	 * the generic left Object of the Pair
	 */
	private final L left;

	/**
	 * the generic right Object of the Pair
	 */
	private final R right;

	/**
	 * Generic Class constructor taking in two Objects that represent the left
	 * and right of the Pair.
	 * 
	 * @param left
	 *            the left side object of the Pair object
	 * @param right
	 *            the right side object of the Pair object
	 */
	public Pair(L left, R right) {
		this.left = left;
		this.right = right;
	}

	/**
	 * Returns the left side Object of the Pair representation.
	 * 
	 * @return the left side Object of the Pair
	 */
	public L getLeft() {
		return this.left;
	}

	/**
	 * Returns the right side Object of the Pair representation.
	 * 
	 * @return the right side Object of the Pair
	 */
	public R getRight() {
		return this.right;
	}

	// Unavoidable consequence of type erasure
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object object) {
		if (object == null) {
			return false;
		}
		if (!(object instanceof Pair)) {
			return false;
		}
		Pair<L, R> pairObject = (Pair<L, R>) object;

		boolean leftEqual = this.left.equals(pairObject.getLeft());
		boolean rightEqual = this.right.equals(pairObject.getRight());

		return (leftEqual && rightEqual);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append('(');
		builder.append(this.left.toString());
		builder.append(", ");
		builder.append(this.right.toString());
		builder.append(')');

		return builder.toString();
	}

}