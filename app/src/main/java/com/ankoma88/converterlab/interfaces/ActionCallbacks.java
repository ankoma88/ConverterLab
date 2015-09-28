package com.ankoma88.converterlab.interfaces;

/**Defines menu actions, which start other activities*/
public interface ActionCallbacks {

    void onLinkClicked(String link);

    void onMapClicked(int id);

    void onPhoneClicked(String phone);

    void onDetailClicked(int id);

}
