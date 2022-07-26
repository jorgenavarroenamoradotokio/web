package com.finProyecto.fimoteca.web.negocio.exception;

public class DuplicateException extends Exception {

	private static final long serialVersionUID = 1L;

	public DuplicateException() {
		super();
	}

	public DuplicateException(String msg) {
		super(msg);
	}

	public DuplicateException(Throwable t) {
		super(t);
	}

	public DuplicateException(String msg, Throwable t) {
		super(msg, t);
	}
}
