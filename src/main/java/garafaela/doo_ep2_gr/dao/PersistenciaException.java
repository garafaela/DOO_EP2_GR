package garafaela.doo_ep2_gr.dao;

public class PersistenciaException extends Exception {
    public final static String ALERTA_LECTURA = "Error al leer archivo";
    public final static String ALERTA_ESCRITURA = "Error al escribir archivo";

    public PersistenciaException(String message,Throwable cause) {
        super(message, cause);
    }
    public PersistenciaException(String message) {
        super(message);
    }
}
