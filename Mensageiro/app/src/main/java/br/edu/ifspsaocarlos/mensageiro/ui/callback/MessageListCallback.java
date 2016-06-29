package br.edu.ifspsaocarlos.mensageiro.ui.callback;

import br.edu.ifspsaocarlos.mensageiro.model.Contact;

/**
 * @author maiko.trindade
 * @since 29/06/2016
 */
public interface MessageListCallback {
    void openContactMessages(Contact contact);
}