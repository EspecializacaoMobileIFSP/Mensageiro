package br.edu.ifspsaocarlos.mensageiro.ui.contract;

import android.support.v4.app.Fragment;
import android.view.View;

/**
 * @author maiko.trindade
 * @since 29/06/2016
 */
public interface BaseActivityView {

    void initFragment(final Fragment fragment, final String title);

    void changeFragment(final Fragment fragment, final String title);

    void showMessage(View view, int messageResourceId);

}
