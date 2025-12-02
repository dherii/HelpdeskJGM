package com.turmaa.helpdeskturmaa.service.exceptions;

import java.io.Serializable;

public class FieldMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	/** Nome do campo onde ocorreu o erro de validação. */
	private String fieldName;
	
	/** Mensagem descritiva explicando o erro ocorrido. */
	private String message;

	/**
     * Construtor padrão.
     * <p>Necessário para a serialização/deserialização JSON.</p>
     */
	public FieldMessage() {
		super();
	}

	/**
     * Construtor completo.
     *
     * @param fieldName nome do campo com erro
     * @param message mensagem explicando o motivo do erro
     */
	public FieldMessage(String fieldName, String message) {
		super();
		this.fieldName = fieldName;
		this.message = message;
	}

	/**
     * Obtém o nome do campo onde ocorreu o erro.
     *
     * @return nome do campo com erro
     */
	public String getFieldName() {
		return fieldName;
	}

	/**
     * Define o nome do campo onde ocorreu o erro.
     *
     * @param fieldName nome do campo
     */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	 /**
     * Obtém a mensagem explicativa do erro de validação.
     *
     * @return mensagem de erro
     */
	public String getMessage() {
		return message;
	}

	/**
     * Define a mensagem explicativa do erro de validação.
     *
     * @param message mensagem descritiva do erro
     */
	public void setMessage(String message) {
		this.message = message;
	}

}