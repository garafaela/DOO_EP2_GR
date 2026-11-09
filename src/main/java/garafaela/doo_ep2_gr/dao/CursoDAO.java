package garafaela.doo_ep2_gr.dao;

import garafaela.doo_ep2_gr.model.Curso;

import java.util.List;

public interface CursoDAO<T extends Curso> {
    List<T> cargarDAO() throws PersistenciaException;
    void guardarDAO(List<T> cursos) throws PersistenciaException;
}
