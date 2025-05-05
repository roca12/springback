package co.edu.unbosque.springfirstapp.service;

import java.util.List;

/**
 * Interfaz genérica que define operaciones CRUD (Crear, Leer, Actualizar, Eliminar) básicas.
 *
 * @param <T> Tipo de entidad sobre la que se realizarán las operaciones CRUD
 */
public interface CRUDOperation<T> {

  /**
   * Crea una nueva entidad.
   *
   * @param data La entidad a crear
   * @return Código de resultado de la operación (generalmente 1 para éxito, 0 para fallo)
   */
  public int create(T data);

  /**
   * Obtiene todas las entidades.
   *
   * @return Lista con todas las entidades
   */
  public List<T> getAll();

  /**
   * Elimina una entidad por su identificador.
   *
   * @param id Identificador de la entidad a eliminar
   * @return Código de resultado de la operación (generalmente 1 para éxito, 0 para fallo)
   */
  public int deleteById(Long id);

  /**
   * Actualiza una entidad existente por su identificador.
   *
   * @param id Identificador de la entidad a actualizar
   * @param newData Nuevos datos para la entidad
   * @return Código de resultado de la operación (generalmente 1 para éxito, 0 para fallo)
   */
  public int updateById(Long id, T newData);

  /**
   * Cuenta el número total de entidades.
   *
   * @return Número total de entidades
   */
  public long count();

  /**
   * Verifica si existe una entidad con el identificador especificado.
   *
   * @param id Identificador a verificar
   * @return true si existe una entidad con ese identificador, false en caso contrario
   */
  public boolean exist(Long id);
}
