package garafaela.doo_ep2_gr.model;

public interface ConDiploma {

    /**
     * Indica si el curso tiene un diploma emitido.
     *
     * @return true si el diploma fue emitido
     */
    boolean tieneDiplomaEmitido();

    /**
     * Registra la emisión del diploma del curso.
     */
    void emitirDiploma();
}
