package com.transmi.remun.frontend.crud;

public class Message
{

   public static final String CONFIRM_CAPTION_DELETE = "Confirme la eliminación";
   public static final String CONFIRM_MESSAGE_DELETE = "Está seguro de eliminar el parámetro seleccionado? Esta acción no se puede deshacer.";
   public static final String BUTTON_CAPTION_DELETE  = "Eliminar";
   public static final String BUTTON_CAPTION_CANCEL  = "Cancelar";

   public static final MessageSupplier UNSAVED_CHANGES = createMessage("Cambios no guardados", "Descartar", "Continuar Editando",
         "Hay modificaciones no guardadas a %s. Descarte los cambios?");

   public static final MessageSupplier CONFIRM_DELETE = createMessage(CONFIRM_CAPTION_DELETE, BUTTON_CAPTION_DELETE,
         BUTTON_CAPTION_CANCEL, CONFIRM_MESSAGE_DELETE);

   private final String caption;
   private final String okText;
   private final String cancelText;
   private final String message;

   public Message(String caption, String okText, String cancelText, String message) {
      this.caption = caption;
      this.okText = okText;
      this.cancelText = cancelText;
      this.message = message;
   }

   private static MessageSupplier createMessage(String caption, String okText, String cancelText, String message) {
      return (parameters) -> new Message(caption, okText, cancelText, String.format(message, parameters));
   }

   public String getCaption() {
      return caption;
   }

   public String getOkText() {
      return okText;
   }

   public String getCancelText() {
      return cancelText;
   }

   public String getMessage() {
      return message;
   }

   @FunctionalInterface
   public interface MessageSupplier {
      Message createMessage(Object... parameters);
   }

}//Message
