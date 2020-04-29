package com.transmi.remun.service.exception;

public class AccessDeniedException extends RuntimeException
{
   public AccessDeniedException() {
   }

   public AccessDeniedException(String message) {
      super(message);
   }
}//AccessDeniedException
