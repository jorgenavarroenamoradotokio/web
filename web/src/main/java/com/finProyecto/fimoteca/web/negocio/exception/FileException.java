package com.finProyecto.fimoteca.web.negocio.exception;

public class FileException extends Exception {

	private static final long serialVersionUID = 1L;

	public FileException() {
		super();
	}

	public FileException(String msg) {
		super(msg);
	}

	public FileException(Throwable t) {
		super(t);
	}

	public FileException(String msg, Throwable t) {
		super(msg, t);
	}
}
