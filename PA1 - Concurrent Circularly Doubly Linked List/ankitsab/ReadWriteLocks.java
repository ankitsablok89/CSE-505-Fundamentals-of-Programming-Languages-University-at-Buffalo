// NAME - ANKIT SABLOK
// UBIT-EMAIL-ID - ankitsab@buffalo.edu

public class ReadWriteLocks {

	// these 3 variables help in creating a read write lock
	private int numberOfReaders = 0;
	private int numberOfWriters = 0;
	private int numberOfWriteRequests = 0;

	// getter method for the number of readers
	public int getNumberOfReaders() {
		return this.numberOfReaders;
	}

	// getter method for the number of writers
	public int getNumberOfWriters() {
		return this.numberOfWriters;
	}

	// getter method for the number of write requests
	public int getNumberOfWriteRequests() {
		return this.numberOfWriteRequests;
	}

	// this function checks if a thread can acquire the lock
	public synchronized void lockRead() throws InterruptedException {
		
		while (numberOfWriters > 0 || numberOfWriteRequests > 0) {
			this.wait();
		}

		// increment the number of readers
		++numberOfReaders;
	}

	// this function unlocks a lock occupied by a reader thread
	public synchronized void unlockRead() {
		
		// decrement the number of readers
		--numberOfReaders;
		notifyAll();
	}

	// this function checks if a thread can acquire the write lock
	public synchronized void lockWrite() throws InterruptedException {
		
		// increase the number of write requests
		++numberOfWriteRequests;
		
		while (numberOfReaders > 0 || numberOfWriters > 0){
			this.wait();
		}

		--numberOfWriteRequests;
		++numberOfWriters;
		
	}

	// this function is used to take a thread away from the lock
	public synchronized void unlockWrite() {
		
		// decrement the number of writers
		--numberOfWriters;
		// notify all the threads
		notifyAll();
	}

}
