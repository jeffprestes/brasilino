package com.example.ricardo.thtpaypalcarrinho.domain;

import java.util.List;

/**
 * Created by ricardo on 31/01/2015.
 */
public class UserRoot {

    private List<User> userRoot;

    public User getUserRoot(int indice){
        return userRoot.get(indice);
    }

}
