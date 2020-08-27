package edu.vandy.simulator.managers.palantiri.reentrantLockHashMapSimpleSemaphore;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class defines a counting semaphore with "fair" semantics that
 * are implemented using a Java ReentrantLock and ConditionObject.
 */
public class SimpleSemaphore {
    /**
     * Define a count of the number of available permits.
     */
    // TODO - you fill in here.  Ensure that this field will ensure
    // its values aren't cached by multiple threads..
    volatile int permits;

    /**
     * Define a Lock to protect critical sections.
     */
    // TODO - you fill in here
    Lock lock;

    /**
     * Define a Condition that's used to wait while the number of
     * permits is 0.
     */
    // TODO - you fill in here
    Condition permitNotEmpty;

    /**
     * Default constructor used for regression tests.
     */
    public SimpleSemaphore() {
    }

    /**
     * Constructor initialize the fields.
     */
    public SimpleSemaphore(int permits) {
        // TODO -- you fill in here making sure the ReentrantLock has
        // "fair" semantics.
        this.permits = permits;
        lock = new ReentrantLock(true);
        permitNotEmpty = lock.newCondition();
    }

    /**
     * Acquire one permit from the semaphore in a manner that can be
     * interrupted.
     */
    public void acquire()
            throws InterruptedException {
        // TODO -- you fill in here, make sure the lock is always
        // released, e.g., even if an exception occurs.
        lock.lockInterruptibly();
        try {
            // using while here
            while (permits <= 0) {
                // auto-release lock here
                permitNotEmpty.await();
                // auto-reacquire lock
            }
            permits--;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Acquire one permit from the semaphore in a manner that cannot
     * be interrupted.  If an interrupt occurs while this method is
     * running make sure the interrupt flag is reset when the method
     * returns.
     */
    public void acquireUninterruptibly() {
        // TODO -- you fill in here, make sure the lock is always
        // released, e.g., even if an exception occurs.
        boolean interrupted = false;
        while (true) {
            try {
                acquire();
                break;
            } catch (InterruptedException ie) {
                interrupted = true;
            }
        }
        if (interrupted) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Return one permit to the semaphore.
     */
    public void release() {
        // TODO -- you fill in here, make sure the lock is always
        // released, e.g., even if an exception occurs.
        lock.lock();
        try {
            if (++permits > 0)
                permitNotEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Returns the current number of permits.
     */
    protected int availablePermits() {
        // TODO -- you fill in here, replacing 0 with the
        // appropriate field.
        lock.lock();
        try {
            return permits;
        } finally {
            lock.unlock();
        }
    }
}
