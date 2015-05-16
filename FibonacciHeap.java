
import java.util.*; 


public final class FibonacciHeap<T> {

	public static final class FibNode<T> {
		private int     mDegree = 0;       // Number of children
		private boolean mIsMarked = false; // Whether this node is marked

		private FibNode<T> mNext;   // Next and previous elements in the list
		private FibNode<T> mPrev;

		private FibNode<T> mParent; // Parent in the tree, if any.

		private FibNode<T> mChild;  // Child node, if any.

		private T      mElem;     // Element being stored here
		private double mPriority; // Its priority

		
		public T getValue() {
			return mElem;
		}
		
		public void setValue(T value) {
			mElem = value;
		}

		
		public double getPriority() {
			return mPriority;
		}

		
		private FibNode(T elem, double priority) {
			mNext = mPrev = this;
			mElem = elem;
			mPriority = priority;
		}
	}

	/* Pointer to the minimum element in the heap. */
	private FibNode<T> mMin = null;

	/* Cached size of the heap, so we don't have to recompute this explicitly. */
	private int mSize = 0;

	/**
	 * Inserts the specified element into the Fibonacci heap with the specified
	 * priority.  Its priority must be a valid double, so you cannot set the
	 * priority to NaN.
	 */
	public FibNode<T> enqueue(T value, double priority) {
		checkPriority(priority);

		/* Create the entry object, which is a circularly-linked list of length
		 * one.
		 */
		FibNode<T> result = new FibNode<T>(value, priority);

		/* Merge this singleton list with the tree list. */
		mMin = mergeLists(mMin, result);

		/* Increase the size of the heap; we just added something. */
		++mSize;

		/* Return the reference to the new element. */
		return result;
	}

	
	public FibNode<T> min() {
		if (isEmpty())
			throw new NoSuchElementException("Heap is empty.");
		return mMin;
	}

	
	public boolean isEmpty() {
		return mMin == null;
	}

	
	public int size() {
		return mSize;
	}

	
	public static <T> FibonacciHeap<T> merge(FibonacciHeap<T> one, FibonacciHeap<T> two) {
		/* Create a new FibonacciHeap to hold the result. */
		FibonacciHeap<T> result = new FibonacciHeap<T>();

		result.mMin = mergeLists(one.mMin, two.mMin);

		result.mSize = one.mSize + two.mSize;

		/* Clear the old heaps. */
		one.mSize = two.mSize = 0;
		one.mMin  = null;
		two.mMin  = null;

		/* Return the newly-merged heap. */
		return result;
	}

	/**
	 * Dequeues and returns the minimum element of the Fibonacci heap.  If the
	 * heap is empty, this throws a NoSuchElementException.
	 */
	public FibNode<T> dequeueMin() {
		if (isEmpty())
			throw new NoSuchElementException("Heap is empty.");

		--mSize;

		FibNode<T> minElem = mMin;

		if (mMin.mNext == mMin) { // Case one
			mMin = null;
		}
		else { // Case two
			mMin.mPrev.mNext = mMin.mNext;
			mMin.mNext.mPrev = mMin.mPrev;
			mMin = mMin.mNext; // Arbitrary element of the root list.
		}

		if (minElem.mChild != null) {
			/* Keep track of the first visited node. */
			FibNode<?> curr = minElem.mChild;
			do {
				curr.mParent = null;
				curr = curr.mNext;
			} while (curr != minElem.mChild);
		}

		mMin = mergeLists(mMin, minElem.mChild);

		if (mMin == null) return minElem;

		List<FibNode<T>> treeTable = new ArrayList<FibNode<T>>();

		
		List<FibNode<T>> toVisit = new ArrayList<FibNode<T>>();

		for (FibNode<T> curr = mMin; toVisit.isEmpty() || toVisit.get(0) != curr; curr = curr.mNext)
			toVisit.add(curr);

		/* Traverse this list and perform the appropriate unioning steps. */
		for (FibNode<T> curr: toVisit) {
			/* Keep merging until a match arises. */
			while (true) {
				/* Ensure that the list is long enough to hold an element of this
				 * degree.
				 */
				while (curr.mDegree >= treeTable.size())
					treeTable.add(null);
				if (treeTable.get(curr.mDegree) == null) {
					treeTable.set(curr.mDegree, curr);
					break;
				}

				FibNode<T> other = treeTable.get(curr.mDegree);
				treeTable.set(curr.mDegree, null); // Clear the slot

				FibNode<T> min = (other.mPriority < curr.mPriority)? other : curr;
				FibNode<T> max = (other.mPriority < curr.mPriority)? curr  : other;

				max.mNext.mPrev = max.mPrev;
				max.mPrev.mNext = max.mNext;

				/* Make it a singleton so that we can merge it. */
				max.mNext = max.mPrev = max;
				min.mChild = mergeLists(min.mChild, max);
				max.mParent = min;
				max.mIsMarked = false;
				++min.mDegree;

				/* Continue merging this tree. */
				curr = min;
			}

			if (curr.mPriority <= mMin.mPriority) mMin = curr;
		}
		return minElem;
	}

	public void decreaseKey(FibNode<T> entry, double newPriority) {
		checkPriority(newPriority);
		if (newPriority > entry.mPriority)
			throw new IllegalArgumentException("New priority exceeds old.");
		decreaseKeyUnchecked(entry, newPriority);
	}

	public void delete(FibNode<T> entry) {
		decreaseKeyUnchecked(entry, Double.NEGATIVE_INFINITY);
		dequeueMin();
	}

	private void checkPriority(double priority) {
		if (Double.isNaN(priority))
			throw new IllegalArgumentException(priority + " is invalid.");
	}
	
	private static <T> FibNode<T> mergeLists(FibNode<T> one, FibNode<T> two) {
		
		if (one == null && two == null) { // Both null, resulting list is null.
			return null;
		}
		else if (one != null && two == null) { // Two is null, result is one.
			return one;
		}
		else if (one == null && two != null) { // One is null, result is two.
			return two;
		}
		else { // Both non-null; 
			FibNode<T> oneNext = one.mNext; 
			one.mNext = two.mNext;
			one.mNext.mPrev = one;
			two.mNext = oneNext;
			two.mNext.mPrev = two;

			/* Return a pointer to whichever's smaller. */
			return one.mPriority < two.mPriority? one : two;
		}
	}


	private void decreaseKeyUnchecked(FibNode<T> entry, double priority) {
		/* First, change the node's priority. */
		entry.mPriority = priority;
		if (entry.mParent != null && entry.mPriority <= entry.mParent.mPriority)
			cutNode(entry);
		if (entry.mPriority <= mMin.mPriority)
			mMin = entry;
	}


	private void cutNode(FibNode<T> entry) {
		entry.mIsMarked = false;

		/* Base case: If the node has no parent*/
		if (entry.mParent == null) return;
		
		if (entry.mNext != entry) { // Has siblings
			entry.mNext.mPrev = entry.mPrev;
			entry.mPrev.mNext = entry.mNext;
		}

		if (entry.mParent.mChild == entry) {
			if (entry.mNext != entry) {
				entry.mParent.mChild = entry.mNext;
			}
			else {
				entry.mParent.mChild = null;
			}
		}
		--entry.mParent.mDegree;

		entry.mPrev = entry.mNext = entry;
		mMin = mergeLists(mMin, entry);

		/* Mark the parent and recursively cut it if it's already been
		 * marked.
		 */
		if (entry.mParent.mIsMarked)
			cutNode(entry.mParent);
		else
			entry.mParent.mIsMarked = true;

		/* Clear the relocated node's parent; it's now a root. */
		entry.mParent = null;
	}
}