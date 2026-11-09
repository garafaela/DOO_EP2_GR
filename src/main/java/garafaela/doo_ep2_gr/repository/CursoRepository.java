package garafaela.doo_ep2_gr.repository;

import garafaela.doo_ep2_gr.dao.CursoDAO;
import garafaela.doo_ep2_gr.dao.PersistenciaException;
import garafaela.doo_ep2_gr.model.Curso;
import garafaela.doo_ep2_gr.model.CursoCertificado;
import garafaela.doo_ep2_gr.model.CursoLibre;

import java.util.ArrayList;
import java.util.List;

public class CursoRepository implements Repository<Curso>{

    private final List<Curso> todo = new ArrayList<>();
    private final CursoDAO<CursoCertificado> daoCursoCertificado;
    private final CursoDAO<CursoLibre> daoCursoLibre;


    public CursoRepository(CursoDAO<CursoCertificado> daoCursoCertificado,  CursoDAO<CursoLibre> daoCursoLibre) {
        this.daoCursoCertificado = daoCursoCertificado;
        this.daoCursoLibre = daoCursoLibre;
    }

    @Override
    public void cargar() throws PersistenciaException{
        todo.clear();
        todo.addAll(daoCursoCertificado.cargarDAO());
        todo.addAll(daoCursoLibre.cargarDAO());
    }

    @Override
    public void agregar(Curso newCurso) throws PersistenciaException{
        todo.add(newCurso);
        guardarJson();
    }

    @Override
    public void eliminar(Curso newCurso) throws PersistenciaException{
        todo.remove(newCurso);
        guardarJson();
    }

    @Override
    public List<Curso> listar(){
        return new ArrayList<>(todo);
    }


    public void guardarJson() throws PersistenciaException{

        List<CursoCertificado> certificados = new ArrayList<>();
        List<CursoLibre> libres = new ArrayList<>();

        for (Curso c : this.todo){
            if(c instanceof CursoCertificado){
                certificados.add((CursoCertificado) c);
            } else if (c instanceof CursoLibre) {
                libres.add((CursoLibre) c);
            }
        }

        daoCursoCertificado.guardarDAO(certificados);
        daoCursoLibre.guardarDAO(libres);
    }

}
