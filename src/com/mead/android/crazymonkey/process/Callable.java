package com.mead.android.crazymonkey.process;

import java.io.Serializable;

public interface Callable<V, T extends Throwable> extends Serializable {
	/**
	 * Performs computation and returns the result,
	 * or throws some exception.
	 */
	V call() throws T;
}
