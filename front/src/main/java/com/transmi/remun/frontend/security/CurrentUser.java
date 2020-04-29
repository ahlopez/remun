package com.transmi.remun.frontend.security;

import com.transmi.remun.backend.data.entity.User;

@FunctionalInterface
public interface CurrentUser
{

  User getUser();

}// CurrentUser
