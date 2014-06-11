// Name - Ankit Sablok
// UB Email Id - ankitsab@buffalo.edu

// this class implements the coarse Read Write lock
@SuppressWarnings("unused")
public class CDLListFineRW<T> extends CDLList<T> {

	// this is the constructor for the CDLCoarse class
	public CDLListFineRW(T v) {
		super(v);
	}

	// this method is used to retrieve the head of the linked list
	public CDLList<T>.Element head() {

		// call the superclass head method
		return super.head();
	}

	// this returns a CDLCoarseRW cursor object
	public CDLListFineRW<T>.Cursor reader(CDLList<T>.Element from) {

		CDLListFineRW<T>.Cursor newCursor = new Cursor(from);
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

			// get the current position of the cursor
			CDLList<T>.Element currentElement = this.current();
			
			try {
				// lock the current element and perform the modification
				currentElement.lockRead();
				super.previous();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// unlock the object after performing the modification
			currentElement.unlockRead();
		}

		// this moves the cursor to its next position and hence is a write
		// operation
		public void next() {

			// get the current position of the cursor
			CDLList<T>.Element currentElement = this.current();
			try {
				
				// lock the current element and perform the modification
				currentElement.lockRead();
				super.next();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// unlock the object after performing the modification
			currentElement.unlockRead();
		}

		// this depends on the current position of the cursor and hence locking
		// is internal
		public CDLListFineRW<T>.Writer writer() {

			CDLListFineRW<T>.Writer writer = new Writer(current());
			return writer;
		}
	}

	public class Writer extends CDLList<T>.Writer {

		public Writer(CDLList<T>.Element cursorPosition) {
			super(cursorPosition);
		}

		// this is a write method and hence needs a lock
		public boolean insertBefore(T val) {
			
			// get the current position of the writer
			CDLList<T>.Element currentElement = getWriterPosition();
			
			try{
				// lock the current node for writing
				currentElement.lockWrite();
				CDLList<T>.Element previousElement = currentElement.getNodePrevious();
				
				if(currentElement == previousElement){
					super.insertBefore(val);
					currentElement.unlockWrite();
					return true;
				} else{
					
					previousElement.lockWrite();
					super.insertBefore(val);
					previousElement.unlockWrite();
					currentElement.unlockWrite();
					
					return true;
				}
			} catch(InterruptedException e){
				return false;
			}
		}

		// this again is a write method and hence needs a lock
		public boolean insertAfter(T val) {
			
			// get the current position of the writer
			CDLList<T>.Element currentElement = getWriterPosition();
			
			try{
				// lock the current node for writing
				currentElement.lockWrite();
				CDLList<T>.Element nextElement = currentElement.getNodeNext();
				
				if(currentElement == nextElement){
					super.insertAfter(val);
					currentElement.unlockWrite();
					return true;
				} else{
					
					nextElement.lockWrite();
					super.insertAfter(val);
					nextElement.unlockWrite();
					currentElement.unlockWrite();
					
					return true;
				}
			} catch(InterruptedException e){
				return false;
			}
		}
	}

}