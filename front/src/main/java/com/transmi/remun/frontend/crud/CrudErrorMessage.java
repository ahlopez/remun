package com.transmi.remun.frontend.crud;

public final class CrudErrorMessage
{
   public static final String ENTITY_NOT_FOUND = "No encontró la entidad seleccionada.";

   public static final String CONCURRENT_UPDATE = "Alguien más probablemente modificó los datos. Por favor refresque todo y trate de nuevo.";

   public static final String OPERATION_PREVENTED_BY_REFERENCES = "La operación no se pudo ejecutar pues hay referencias a la entidad en la base de datos.";

   public static final String REQUIRED_FIELDS_MISSING = "Por favor ingrese los datos requeridos obligatoriamente antes de continuar.";

   private CrudErrorMessage() {
   }
}//CrudErrorMessage
