/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filmoteca;

import java.util.LinkedList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author MEDAC
 */
public class TableModelPeliculas extends AbstractTableModel {

    private static final String[] columnNames = {"Titulo", "Año", "Puntuación", "Sinopsis"};

    public LinkedList<Pelicula> getList() {
        return list;
    }

    private LinkedList<Pelicula> list;

    public TableModelPeliculas(LinkedList<Pelicula> lista) {
        this.list = lista;
        // Notifica a la vista que el contenido ha cambiado para que se refresque.
        fireTableDataChanged();
    }

    public TableModelPeliculas() {
        try {
            list = new LinkedList<Pelicula>();
            cargarPeliculas();
        } catch (Exception e) {
            System.out.println("Error creando crontrolador");
        }
    }

    public Pelicula getValueAt(int rowIndex) {
        return list.get(rowIndex);
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return list.get(rowIndex).getTitulo();
            case 1:
                return list.get(rowIndex).getAnyo();
            case 2:
                return list.get(rowIndex).getPuntuacion();
            case 3:
                return list.get(rowIndex).getSinopsis();
        }
        return null;
    }

    public void insertarPeliculas(int id, String titulo, int anyo, int puntuacion, String sinopsis) {

        try {

            Pelicula p = new Pelicula(id, titulo, anyo, puntuacion, sinopsis);
            ConexionDB4O.getInstance().añadirPeliculas(p);
        } catch (Exception e) {
            System.out.println("error creando crontrolador");
        }
        cargarPeliculas();
    }

    public void borrarPelicula(int id) {
        try {
            ConexionDB4O.getInstance().borrarPelicula(id);
        } catch (Exception e) {
            System.out.println("Error borrando la pelicula");
        }
        cargarPeliculas();

    }

    public void cargarPeliculas() {
        try {
            LinkedList<Pelicula> peli = ConexionDB4O.getInstance().listarPeliculas();

            list.clear();
            list.addAll(peli);
        } catch (Exception e) {
            System.out.println("Error cargando pelis");
        }

        fireTableDataChanged();
    }

    void actualizarPeliculas(int id, String titulo, int anyo, int puntuacion, String sinopsis) {
        try {
            ConexionDB4O.getInstance().actualizarPelicula(id, titulo, anyo, puntuacion, sinopsis);
        } catch (Exception e) {
            System.out.println("Error actualizando pelicula");
        }
        cargarPeliculas();
    }

    public void filtroMayor(int anyo, int puntuacion) {
        try {
            ConexionDB4O.getInstance().eliminarPorAnyoYPuntuacionMayor(anyo, puntuacion);
        } catch (Exception e) {
            System.out.println("Error eliminando por filtro mayor");
        }
        cargarPeliculas();
    }

    public void filtroMenor(int anyo, int puntuacion) {
        try {
            ConexionDB4O.getInstance().eliminarPorAnyoYPuntuacionMenor(anyo, puntuacion);
        } catch (Exception e) {
            System.out.println("Error eliminando por filtro menor");
        }
        cargarPeliculas();
    }

    public void filtroIgual(int anyo, int puntuacion) {
        try {
            ConexionDB4O.getInstance().eliminarPorAnyoYPuntuacionIgual(anyo, puntuacion);
        } catch (Exception e) {
            System.out.println("Error eliminando por filtro igual");
        }
        cargarPeliculas();
    }

}
