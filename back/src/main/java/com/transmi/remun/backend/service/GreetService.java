package com.transmi.remun.backend.service;

import java.io.Serializable;

import org.springframework.stereotype.Service;

import com.transmi.remun.service.main.Greetings;

@Service
public class GreetService implements Greetings, Serializable
{
  public GreetService()
  {}

  @Override
  public String greet(String name) {

    System.out.println(">>> Llegué a greet, con parametro[" + name + "]");
    return name == null || name.isEmpty() ? "Hello anonymous user" : "Hello " + name;
  }

}// GreetService