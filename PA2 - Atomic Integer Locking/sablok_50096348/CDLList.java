// Name - Ankit Sablok
// UB Email Id - ankitsab@buffalo.edu

// the generic class specification given in the project assignment
public class CDLList<T> {

	// every circular double linked list got to have a "head" pointer that
	// points to the first element of the linked list, the list head is
	// initially null as there are no elements in the list
	private Element listHead = null;

	// the constructor for the class, it actually creates a new doubly linked
	// list by creating a new node with value v and making the head of the list
	// point to the new node
	public CDLList(T v) {

		// create a new cdllist by creating a new head for the list
		listHead = new Element();
		listHead.setNodeValue(v);
		listHead.setNodeNext(listHead);
		listHead.setNodePrevious(listHead);
	}

	// this is the getter function for the head of the circular double linked
	// list, this needn't be synchronized as any thread can read the value of
	// the head of the list
	public Element head() {
		return this.listHead;
	}

	// all the nodes in the circular double linked list data structure created
	// using this class will have nodes of types "Element", this is an inner
	// class for the list data structure
	public class Element {

		// each element node will have a value, a next pointer and a previous
		// pointer
		private T nodeValue;
		private Element nodeNext;
		private Element nodePrevious;
		
		// we define here an instance of the read write lock class so that we can lock individual elements of the list
		private ReadWriteLocks rwLock = new ReadWriteLocks();

		// the following methods are the getters and setter for the node of type
		// "Element"
		public T value() {
			return this.nodeValue;
		}

		public void setNodeValue(T nodeValue) {
			this.nodeValue = nodeValue;
		}

		public Element getNodeNext() {
			return this.nodeNext;
		}

		public void setNodeNext(Element nodeNext) {
			this.nodeNext = nodeNext;
		}

		public Element getNodePrevious() {
			return this.nodePrevious;
		}

		public void setNodePrevious(Element nodePrevious) {
			this.nodePrevious = nodePrevious;
		}
		
		public void lockRead() throws InterruptedException{
			this.rwLock.lockRead();
		}
		
		public void unlockRead(){
			this.rwLock.unlockRead();
		}
		
		public void lockWrite() throws InterruptedException{
			this.rwLock.lockWrite();
		}
		
		public void unlockWrite(){
			this.rwLock.unlockWrite();
		}

	}

	// this method returns a cursor to a specific element in the cdllist
	public Cursor reader(Element from) {

		Cursor newCursor = new Cursor();
		newCursor.setCursorPosition(from);

		return newCursor;
	}

	// this is the cursor inner class that provides a cursor object that is
	public class Cursor {

		// this private variable stores the cursor's position
		private Element cursorPosition;

		// these are the various methods supported by the cursor class
		public void setCursorPosition(Element cursorPosition) {
			this.cursorPosition = cursorPosition;
		}

		// returns the current position of the cursor
		public Element current() {
			return this.cursorPosition;
		}

		// this method moves the current cursorPosition and makes it point to
		// the element that precedes the current cursor position
		public void previous() {
			this.cursorPosition = cursorPosition.getNodePrevious();
		}

		// this method moves the current cursorPosition and makes it point to
		// the element that follows the current cursor position
		public void next() {
			this.cursorPosition = cursorPosition.getNodeNext();
		}

		// this method returns a writer at the current element at which the
		// cursor points
		public Writer writer() {

			Writer newWriter = new Writer(cursorPosition);
			return newWriter;

		}
	}

	// this is the Writer inner class that is used to insert nodes in the
	// circularly double linked list either after a node or before the node
	// where the cursor associated with it points
	public class Writer {

		// this variable stores the writer's current position
		private Element writerPosition;

		// getter for the writer position
		public Element getWriterPosition() {
			return writerPosition;
		}

		// this is a constructor for the Writer class
		public Writer(Element cursorPosition) {
			writerPosition = cursorPosition;
		}

		// this method adds a new node before the the element where the writer
		// currently points
		public boolean insertBefore(T val) {

			try {
				// create a new node of type Element that needs to be inserted
				// into the cdllist
				Element newNode = new Element();
				newNode.setNodeValue(val);

				newNode.setNodeNext(writerPosition);
				newNode.setNodePrevious(writerPosition.getNodePrevious());
				writerPosition.getNodePrevious().setNodeNext(newNode);
				writerPosition.setNodePrevious(newNode);

				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}

			return false;
		}

		// this method adds a new node after the element where the writer
		// currently points
		public boolean insertAfter(T val) {
			try {

				// create a new node of type Element that needs to be inserted
				// into the cdllist
				Element newNode = new Element();
				newNode.setNodeValue(val);
				newNode.setNodePrevious(writerPosition);
				newNode.setNodeNext(writerPosition.getNodeNext());
				newNode.getNodeNext().setNodePrevious(newNode);
				writerPosition.setNodeNext(newNode);

				return true;

			} catch (Exception e) {
				e.printStackTrace();
			}

			return false;
		}
	}
}
