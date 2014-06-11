// NAME - ANKIT SABLOK
// UBIT-EMAIL-ID - ankitsab@buffalo.edu

public class CDLCoarse<T> extends CDLList<T> {

	// this object will be used as a lock object for each instance of the
	// CDLCoarse class
	Object lockObject = new Object();

	// this is the constructor for the CDLCoarse class
	public CDLCoarse(T v) {
		super(v);
	}

	// this is the getter function for the head of the circular double linked
	// list created using the CDLCoarse class
	public CDLList<T>.Element head() {

		CDLList<T>.Element listHead = null;

		synchronized (lockObject) {
			listHead = super.head();
		}

		return listHead;
	}

	// this method returns a cursor to a specific element
	public CDLCoarse<T>.Cursor reader(CDLList<T>.Element from) {

		CDLCoarse<T>.Cursor newCursor = new Cursor(from);
		return newCursor;
	}

	// this is the Cursor inner class of the CDLCoarse class that extends the
	// CDLList's Cursor inner class
	public class Cursor extends CDLList<T>.Cursor {

		// this method is used to set the position of the cursor
		public Cursor(CDLList<T>.Element from) {
			super.setCursorPosition(from);
		}

		// this method is used to return the CDLList<T>.Element which is the
		// current cursor position, as this is the reader method we don't need
		// to synchronize the method
		public CDLList<T>.Element current() {

			CDLList<T>.Element currentCursorPosition = null;

			synchronized (lockObject) {
				currentCursorPosition = super.current();
			}

			return currentCursorPosition;
		}

		// this method is used to move the cursor to a position prior to its
		// node, as this method does some modification to the cursor position
		// this needs to be synchronized
		public void previous() {

			synchronized (lockObject) {
				super.previous();
			}
		}

		// this method is used to move the cursor to a position succeeding its
		// own, as this method does some modification to the cursor position
		// this needs to be synchronized
		public void next() {

			synchronized (lockObject) {
				super.next();
			}
		}

		// this method returns a writer at the current element at which the
		// cursor points, this is a read method and hence doesn't need any
		// synchronization
		public CDLCoarse<T>.Writer writer() {

			CDLCoarse<T>.Writer writer = new Writer(current());
			return writer;
		}
	}

	// this is the Writer inner class that is used to insert nodes in the
	// circularly double linked list either after a node or before the node
	// where the cursor associated with it points
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

			synchronized (lockObject) {
				checkInserted = super.insertBefore(val);
			}

			return checkInserted;
		}

		// this method is used to insert a value after a specific position,
		// which is the position at which the cursor points
		public boolean insertAfter(T val) {

			boolean checkInserted = false;

			synchronized (lockObject) {
				checkInserted = super.insertAfter(val);
			}

			return checkInserted;
		}
	}
}
