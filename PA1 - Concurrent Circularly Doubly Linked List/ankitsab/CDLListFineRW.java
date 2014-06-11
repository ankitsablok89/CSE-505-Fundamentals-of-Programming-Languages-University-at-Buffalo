// NAME - ANKIT SABLOK
// UBIT-EMAIL-ID - ankitsab@buffalo.edu

public class CDLListFineRW<T> extends CDLList<T> {

	// this is the dummy node taken to avoid the deadlock condition
	CDLList<T>.Element dummyNode;

	// this is the constructor for the CDLListFine class
	public CDLListFineRW(T v) {
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
		return super.head();
	}

	public CDLListFineRW<T>.Cursor reader(CDLList<T>.Element from) {

		CDLListFineRW<T>.Cursor newCursor = new Cursor(from);
		return newCursor;
	}

	public class Cursor extends CDLList<T>.Cursor {

		// this method is used to set the position of the cursor
		public Cursor(CDLList<T>.Element from) {
			super.setCursorPosition(from);
		}

		// this method is used to return the CDLList<T>.Element which is the
		// current cursor position
		public CDLList<T>.Element current() {
			return super.current();
		}

		public void previous() {

			try {

				super.current().getNodePrevious().rwLock.lockRead();
				super.previous();
				super.current().getNodePrevious().rwLock.unlockRead();

			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

			// if the cursor position is the dummy node then there's a problem
			if (super.current() == dummyNode) {
				super.previous();
			}

		}


		public void next() {

			try {

				super.current().getNodeNext().rwLock.lockRead();
				super.next();
				super.current().getNodeNext().rwLock.unlockRead();

			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

			// if the cursor position is the dummy node then there's a problem
			if (super.current() == dummyNode) {
				super.next();
			}

		}

	public CDLListFineRW<T>.Writer writer() {

			CDLListFineRW<T>.Writer writer = new Writer(current());
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
			
			// lock the previous element and then the next element
			try {
					super.getWriterPosition().getNodePrevious().rwLock.lockWrite();
					checkInserted = super.insertBefore(val);
					super.getWriterPosition().getNodePrevious().rwLock.unlockWrite();

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			
			return checkInserted;
		}

		// this method is used to insert a value after a specific position,
		// which is the position at which the cursor points
		public boolean insertAfter(T val) {

			boolean checkInserted = false;

			// lock the previous element and then the next element
			try {
					super.getWriterPosition().getNodeNext().rwLock.lockWrite();
					checkInserted = super.insertAfter(val);
					super.getWriterPosition().getNodeNext().rwLock.unlockWrite();

				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			return checkInserted;
		}
	}
}
