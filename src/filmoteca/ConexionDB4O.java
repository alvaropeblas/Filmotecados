/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filmoteca;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Constraint;
import com.db4o.query.Query;
import java.util.LinkedList;

/**
 * Clase para la conexión con una base de datos DB4O
 *
 */
public class ConexionDB4O {

    private static ObjectContainer bd;
    private final String rutabd;
    private static ConexionDB4O instance; // Solo habrá una conexión en toda la aplicación

    /**
     * Constructor
     */
    private ConexionDB4O(String rutabd) {
        this.rutabd = rutabd;
    }

    /**
     * @brief Crea una conexión a una base de datos
     *
     * @param rutabd La ruta de la base de datos
     *
     * @throw Exception Si ocurre un error durante la creación de la conexión
     */
    public static void crearConexion(String rutabd) throws Exception {

        instance = new ConexionDB4O(rutabd);

    }

    /**
     * @brief Devuelve una instancia de la clase ConexionDB4O
     *
     * @return La instancia de la clase ConexionDB4O
     *
     * @throw Exception Si no se ha creado una instancia de ConexionDB4O antes
     * de acceder
     */
    public static ConexionDB4O getInstance() throws Exception {
        if (instance == null) {
            throw new Exception("Debe crear una conexion antes de acceder");
        }
        return instance;
    }

    /**
     * @brief Conecta la base de datos utilizando Db4oEmbedded.
     *
     * Abre un archivo de base de datos utilizando la configuración por defecto
     * y la ruta especificada. Imprime un mensaje en la consola indicando que la
     * conexión ha sido establecida con éxito.
     */
    public void conectar() {
        bd = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), rutabd);
        System.out.print("[Conectado]");
    }

    /**
     * @brief Desconecta la base de datos actualmente abierta.
     *
     * Cierra el archivo de base de datos actualmente abierto. Imprime un
     * mensaje en la consola indicando que la desconexión ha sido realizada con
     * éxito.
     */
    public void desconectar() {
        bd.close();
        System.out.print("[Desconectado]");
    }

    /**
     * @brief Devuelve todas las peliculas de la base de datos
     *
     * Añade las peliculas que encuentra en la base de datos a una lista y las retorna.
     *
     * @param id clave para identificar la pelicula a eliminar
     * 
     * @return pelis 
     */
    public LinkedList<Pelicula> listarPeliculas() {
        LinkedList<Pelicula> pelis = new LinkedList<>();
        try {
            Query consulta = bd.query();
            consulta.constrain(Pelicula.class);
            ObjectSet<Pelicula> res = consulta.execute();
            while (res.hasNext()) {
                pelis.add(res.next());
            }
        } catch (Exception e) {
            System.out.println("Error");
        }
        return pelis;

    }

    /**
     * @brief Añade una nueva película a la base de datos.
     *
     * Almacena la información de la película proporcionada en la base de datos.
     *
     * @param p Objeto Pelicula a añadir.
     */
    public void añadirPeliculas(Pelicula p) {
        bd.store(p);
    }

    /**
     * @brief Elimina una película a la base de datos.
     *
     * Elimina la película proporcionada en la base de datos.
     *
     * @param id clave para identificar la pelicula a eliminar
     */
    public void borrarPelicula(int id) {
        Query consulta = bd.query();
        consulta.constrain(Pelicula.class);
        consulta.descend("id").constrain(id).equal();
        ObjectSet<Pelicula> res = consulta.execute();
        while (res.hasNext()) {
            Pelicula p = (Pelicula) res.next();
            bd.delete(p);
        }

    }

    void actualizarPelicula(int id, String titulo, int anyo, int puntuacion, String sinopsis) {
        Query consulta = bd.query();
        consulta.constrain(Pelicula.class);
        consulta.descend("id").constrain(id).equal();
        ObjectSet<Pelicula> res = consulta.execute();
        while (res.hasNext()) {
            Pelicula p = res.next();
            p.setTitulo(titulo);
            p.setAnyo(anyo);
            p.setPuntuacion(puntuacion);
            p.setSinopsis(sinopsis);
            bd.store(p);
        }

    }

    public void eliminarPorAnyoYPuntuacionMenor(int anyo, int puntuacion) {
        Query consulta = bd.query();
        consulta.constrain(Pelicula.class);
        Constraint consPuntuacion = consulta.descend("puntuacion").constrain(puntuacion).smaller();
        consulta.descend("anyo").constrain(anyo).equal().and(consPuntuacion);
        ObjectSet<Pelicula> res = consulta.execute();
        while (res.hasNext()) {
            bd.delete(res.next());

        }
    }

    public void eliminarPorAnyoYPuntuacionMayor(int anyo, int puntuacion) {
        Query consulta = bd.query();
        consulta.constrain(Pelicula.class);
        Constraint consPuntuacion = consulta.descend("puntuacion").constrain(puntuacion).greater();
        consulta.descend("anyo").constrain(anyo).equal().and(consPuntuacion);
        ObjectSet<Pelicula> res = consulta.execute();
        while (res.hasNext()) {
            bd.delete(res.next());

        }
    }

    public void eliminarPorAnyoYPuntuacionIgual(int anyo, int puntuacion) {
        Query consulta = bd.query();
        consulta.constrain(Pelicula.class);
        Constraint consPuntuacion = consulta.descend("puntuacion").constrain(puntuacion).equal();
        consulta.descend("anyo").constrain(anyo).equal().and(consPuntuacion);
        ObjectSet<Pelicula> res = consulta.execute();
        while (res.hasNext()) {
            bd.delete(res.next());

        }
    }
}
