// NAME - ANKIT SABLOK
// UBIT-EMAIL-ID - ankitsab@buffalo.edu

public class CDLListFine<T> extends CDLList<T> {

	// this is the dummy node taken to avoid the deadlock condition
	CDLList<T>.Element dummyNode;

	// this is the constructor for the CDLListFine class
	public CDLListFine(T v) {
		// this will allocate the head of the list
		super(v);

		// create the dummy node and update the pointers
		dummyNode = new CDLList.Element();
		dummyNode.setNodeNext(this.head());
		this.head().setNodeNext(dummyNode);
		dummyNode.setNodePrevious(this.head());
		this.head().setNodePrevious(dummyNode);
	}

	// this is the getter function for the head of the circular linked list
	public CDLList<T>.Element head() {

		// although this is a read function but as we don't do any deletions the
		// head stays the same always
		return super.head();
	}

	// this method returns a cursor to a specific element, this is not a read
	// method nor a write method
	public CDLListFine<T>.Cursor reader(CDLList<T>.Element from) {

		CDLListFine<T>.Cursor newCursor = new Cursor(from);
		return newCursor;
	}

	// this is the Cursor inner class of the CDLListFine class that extends the
	// CDLList's Cursor inner class
	public class Cursor extends CDLList<T>.Cursor {

		// this method is used to set the position of the cursor
		public Cursor(CDLList<T>.Element from) {
			super.setCursorPosition(from);
		}

		// this method is used to return the CDLList<T>.Element which is the
		// current cursor position
		public CDLList<T>.Element current() {

			CDLList<T>.Element currentCursorPosition = null;

			synchronized (super.current()) {
				currentCursorPosition = super.current();
			}

			return currentCursorPosition;
		}

		// this method is used to move the cursor to a position prior to its
		// node, as this method does some modification to the cursor position
		// this needs to be synchronized
		public void previous() {

			while (true) {

				CDLList<T>.Element previousElement = this.current()
						.getNodePrevious();

				synchronized (previousElement) {
					if (this.current().getNodePrevious() == previousElement) {
						super.previous();
						break;
					}
				}
			}

			// if the cursor position is the dummy node then there's a problem
			if (super.current() == dummyNode) {

				while (true) {

					CDLList<T>.Element previousElement = this.current()
							.getNodePrevious();

					synchronized (previousElement) {
						if (this.current().getNodePrevious() == previousElement) {
							super.previous();
							break;
						}
					}
				}

			}
		}

		public void next() {

			while (true) {

				CDLList<T>.Element nextElement = this.current().getNodeNext();

				synchronized (nextElement) {
					if (this.current().getNodeNext() == nextElement) {
						super.next();
						break;
					}
				}
			}

			// if the next node is a dummy node then there's a problem
			if (super.current() == dummyNode) {

				while (true) {

					CDLList<T>.Element nextElement = this.current()
							.getNodeNext();

					synchronized (nextElement) {
						if (this.current().getNodeNext() == nextElement) {
							super.next();
							break;
						}
					}
				}

			}
		}

		public CDLListFine<T>.Writer writer() {

			CDLListFine<T>.Writer writer = new Writer(current());
			return writer;
		}
	}

	public class Writer extends CDLList<T>.Writer {

		// this just creates a new writer object and does no manipulation so
		// this needn't be synchronized
		public Writer(CDLList<T>.Element cursorPosition) {
			super(cursorPosition);
		}

		// this method is used to insert a value before a specific position,
		// which is the position at which the cursor points
		public boolean insertBefore(T val) {

			boolean checkInserted = false;

			while (true) {
				// this element is used to resolve the issues of deadlock
				CDLList<T>.Element previousNode = this.getWriterPosition()
						.getNodePrevious();

				// lock the previous element and then the next element
				synchronized (this.getWriterPosition().getNodePrevious()) {
					synchronized (this.getWriterPosition()) {

						// check if the locking condition is satisfied
						if (previousNode == this.getWriterPosition()
								.getNodePrevious()) {
							checkInserted = super.insertBefore(val);
							break;
						}
					}

					if (checkInserted)
						break;

				}

				if (checkInserted)
					break;
			}

			return checkInserted;
		}

		// this method is used to insert a value after a specific position,
		// which is the position at which the cursor points
		public boolean insertAfter(T val) {

			boolean checkInserted = false;

			while (true) {
				// this element is used to resolve the issues of deadlock
				CDLList<T>.Element nextNode = this.getWriterPosition()
						.getNodeNext();

				synchronized (this.getWriterPosition()) {
					synchronized (this.getWriterPosition().getNodeNext()) {

						// check if the locking condition is satisfied
						if (nextNode == this.getWriterPosition().getNodeNext()) {
							checkInserted = super.insertAfter(val);
							break;
						}
					}

					if (checkInserted)
						break;

				}
				if (checkInserted)
					break;
			}

			return checkInserted;
		}
	}
}
