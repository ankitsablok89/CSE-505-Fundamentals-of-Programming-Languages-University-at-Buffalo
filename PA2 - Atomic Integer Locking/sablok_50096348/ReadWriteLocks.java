// Name - Ankit Sablok
// UB Email Id - ankitsab@buffalo.edu

import java.util.concurrent.atomic.AtomicInteger;

public class ReadWriteLocks {

	// initial value of 1 indicates that the lock is in an unlocked state
	public AtomicInteger rwLock = new AtomicInteger(1);

	// this method allocates a lock to the reading thread
	public void lockRead() throws InterruptedException {

		while (true) {

			// get the current lock value
			int currentLockValue = rwLock.get();

			// increase the number of readers if this condition holds
			if (currentLockValue >= 1 && rwLock.compareAndSet(currentLockValue,currentLockValue + 1)) {
				break;
			}

			synchronized (this) {
				wait();
			}
		}

	}

	// this method gets a lock back from the reading thread
	public void unlockRead() {

		while (true) {

			// get the current lock value
			int currentLockVal = rwLock.get();
			
			// to test if there is a write request in the queue so we increment lock value by 1 to make the lock avaialble to the writer in case the lockVal + 1 == 0
			if (currentLockVal < 0 && rwLock.compareAndSet(currentLockVal, currentLockVal + 1))
				break;
			
			// otherwise just decrement the number of readers in the critical section
			if (currentLockVal > 0 && rwLock.compareAndSet(currentLockVal, currentLockVal - 1))
				break;
		}
		
		// after giving up the lock check the value of the lock if its unlocked or locked for write
		int lockValAfterUnlocking = rwLock.get();

		if (lockValAfterUnlocking == 1 || lockValAfterUnlocking == -1) {

			synchronized (this) {
				notifyAll();
			}
		}
	}

	// this method is used to allocate a lock to the writing thread
	public void lockWrite() throws InterruptedException {

		while (true) {

			// get the current value of the lock
			int currentLockValue = rwLock.get();

			if ((currentLockValue == 1 && rwLock.compareAndSet( currentLockValue, 0)) || (currentLockValue == -1 && rwLock.compareAndSet( currentLockValue, 0)))
				break;

			if (currentLockValue > 1)
				rwLock.compareAndSet(currentLockValue, -currentLockValue);

			synchronized (this) {
				wait();
			}
		}
	}

	// this method is used to take the lock away from the thread
	public void unlockWrite() {

		while (true) {

			// get the lock's current value
			int currentLockValue = rwLock.get();
			
			// only a single writer can work inside the critical section at a time so we take its lock away and set the lock's current value to 1 to indicate its in unlocked state 
			if (currentLockValue == 0 && rwLock.compareAndSet(currentLockValue, 1))
				break;
		}

		synchronized (this) {
			notifyAll();
		}
	}
}
