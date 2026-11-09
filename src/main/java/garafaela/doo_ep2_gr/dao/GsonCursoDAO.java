package garafaela.doo_ep2_gr.dao;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import garafaela.doo_ep2_gr.model.Curso;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class GsonCursoDAO<T extends Curso> implements CursoDAO<T> {
    private final Path nombreArchivo;
    private final Type tipoLista;
    private final Gson gson = new Gson();

    public GsonCursoDAO(Path nombreArchivo, Class<T> clase) {
        //TODO: Implementar carpeta para datos
        this.nombreArchivo = nombreArchivo;
        this.tipoLista = TypeToken.getParameterized(List.class, clase).getType();
    }

    @Override
    public List<T> cargarDAO() throws PersistenciaException{
        if(Files.notExists(this.nombreArchivo)){
            return new ArrayList<>();
        }
        try(Reader reader = Files.newBufferedReader(this.nombreArchivo, StandardCharsets.UTF_8)){
            List<T> cursosJson = this.gson.fromJson(reader, this.tipoLista);

            if(cursosJson.isEmpty() || cursosJson == null){
                return new ArrayList<>();
            }
            if(cursosJson.contains(null)){
                throw new PersistenciaException("El archivo contiene un elemento nulo, no se pudo leer.");
            }

            return new ArrayList<>(cursosJson);

        }catch (IOException | JsonParseException e){
            throw new PersistenciaException(PersistenciaException.ALERTA_LECTURA,e);
        }
    }

    @Override
    public void guardarDAO(List<T> cursos) throws PersistenciaException {

        try(Writer writer = Files.newBufferedWriter(this.nombreArchivo,StandardCharsets.UTF_8)){
            this.gson.toJson(cursos,this.tipoLista, writer);

        }catch (IOException | JsonParseException e){
            throw new PersistenciaException(PersistenciaException.ALERTA_ESCRITURA,e);
        }
    }
}