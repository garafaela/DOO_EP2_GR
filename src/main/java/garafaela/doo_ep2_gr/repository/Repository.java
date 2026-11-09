package garafaela.doo_ep2_gr.repository;


import garafaela.doo_ep2_gr.dao.PersistenciaException;
import garafaela.doo_ep2_gr.model.Curso;

import java.util.List;

public interface Repository <T extends Curso> {

    void cargar() throws PersistenciaException;
    void agregar(T obj) throws PersistenciaException;
	void eliminar(T obj) throws PersistenciaException;

    List<T> listar();
}
