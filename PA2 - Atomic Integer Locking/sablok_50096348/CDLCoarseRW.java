// Name - Ankit Sablok
// UB Email Id - ankitsab@buffalo.edu

// this class implements the coarse Read Write lock
@SuppressWarnings("unused")
public class CDLCoarseRW<T> extends CDLList<T> {

	// this is the instance of the read/write lock that we will be using for
	// performing the read/write locks
	private ReadWriteLocks rwLock = new ReadWriteLocks();

	// this is the constructor for the CDLCoarse class
	public CDLCoarseRW(T v) {
		super(v);
	}

	// this method is used to retrieve the head of the linked list
	public CDLList<T>.Element head() {

		// call the superclass head method
		return super.head();
	}

	// this returns a CDLCoarseRW cursor object
	public CDLCoarseRW<T>.Cursor reader(CDLList<T>.Element from) {

		CDLCoarseRW<T>.Cursor newCursor = new Cursor(from);
		return newCursor;
	}

	// this is the cursor class for the CDLCoarseRW object
	public class Cursor extends CDLList<T>.Cursor {

		// this constructor sets the position of a cursor at the specified
		// element
		public Cursor(CDLList<T>.Element from) {
			super.setCursorPosition(from);
		}

		// this method returns the current position of the cursor
		public CDLList<T>.Element current() {
			return super.current();
		}

		// this moves the cursor to its previous position and hence is a write
		// operation
		public void previous() {

			try {
				rwLock.lockRead();
				super.previous();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			rwLock.unlockRead();
		}

		// this moves the cursor to its next position and hence is a write
		// operation
		public void next() {

			try {
				rwLock.lockRead();
				super.next();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			rwLock.unlockRead();
		}

		// this depends on the current position of the cursor and hence locking
		// is internal
		public CDLCoarseRW<T>.Writer writer() {

			CDLCoarseRW<T>.Writer writer = new Writer(current());
			return writer;
		}
	}

	public class Writer extends CDLList<T>.Writer {

		public Writer(CDLList<T>.Element cursorPosition) {
			super(cursorPosition);
		}

		// this is a write method and hence needs a lock
		public boolean insertBefore(T val) {

			boolean checkInserted = false;

			try {
				rwLock.lockWrite();
				checkInserted = super.insertBefore(val);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			rwLock.unlockWrite();

			return checkInserted;
		}

		// this again is a write method and hence needs a lock
		public boolean insertAfter(T val) {

			boolean checkInserted = false;

			try {
				rwLock.lockWrite();
				checkInserted = super.insertAfter(val);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			rwLock.unlockWrite();

			return checkInserted;
		}
	}

}