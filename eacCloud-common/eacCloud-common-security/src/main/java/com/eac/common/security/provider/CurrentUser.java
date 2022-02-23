package com.eac.common.security.provider;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.LinkedHashMap;

/**
 * <pre>
 * Title :  TODO
 * </pre>
 *
 * @author : SunJiYong
 * @since : 2022-01-13
 **/
public class CurrentUser extends User {

    private LinkedHashMap<String, String> currentUser;

    public LinkedHashMap<String,String>  getCurrentUser(){
        return currentUser;
    }


    public CurrentUser(String username, String password, Collection<? extends GrantedAuthority> authorities , LinkedHashMap<String, String> user) {
        super(username, password, authorities);
        this.currentUser=user;
    }


}
