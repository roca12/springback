package co.edu.unbosque.springfirstapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import co.edu.unbosque.springfirstapp.dto.UserDTO;
import co.edu.unbosque.springfirstapp.model.User;
import co.edu.unbosque.springfirstapp.repository.UserRepository;

/**
 * Servicio que proporciona operaciones CRUD y funcionalidades adicionales para usuarios.
 * Implementa la interfaz CRUDOperation para operaciones básicas de creación, lectura, actualización y eliminación.
 */
@Service
public class UserService implements CRUDOperation<UserDTO> {

	/**
	 * Repositorio para acceder a los datos de usuarios en la base de datos.
	 */
	@Autowired
	private UserRepository userRepo;

	/**
	 * Mapeador para convertir entre entidades User y DTOs UserDTO.
	 */
	@Autowired
	private ModelMapper modelMapper;

	/**
	 * Codificador para encriptar contraseñas de usuarios.
	 */
	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * Constructor por defecto.
	 */
	public UserService() {

	}

	/**
	 * Cuenta el número total de usuarios en la base de datos.
	 * 
	 * @return Número total de usuarios
	 */
	@Override
	public long count() {
		return userRepo.count();
	}

	/**
	 * Verifica si existe un usuario con el ID especificado.
	 * 
	 * @param id ID del usuario a verificar
	 * @return true si el usuario existe, false en caso contrario
	 */
	@Override
	public boolean exist(Long id) {
		return userRepo.existsById(id) ? true : false;
	}

	/**
	 * Crea un nuevo usuario en la base de datos.
	 * Codifica la contraseña antes de guardarla.
	 * 
	 * @param data DTO con los datos del usuario a crear
	 * @return 0 si la creación fue exitosa, 1 si el nombre de usuario ya existe
	 */
	@Override
	public int create(UserDTO data) {
		User entity = modelMapper.map(data, User.class);
		if (findUsernameAlreadyTaken(entity)) {
			return 1;
		} else {
			// Encriptar la contraseña antes de guardar
			entity.setPassword(passwordEncoder.encode(entity.getPassword()));
			// Establecer rol si se proporciona, de lo contrario se usará el rol predeterminado (USER)
			if (data.getRole() != null) {
				entity.setRole(data.getRole());
			}
			userRepo.save(entity);
			return 0;
		}
	}

	/**
	 * Obtiene todos los usuarios de la base de datos.
	 * 
	 * @return Lista de DTOs de usuarios
	 */
	@Override
	public List<UserDTO> getAll() {
		List<User> entityList = userRepo.findAll();
		List<UserDTO> dtoList = new ArrayList<>();
		entityList.forEach((entity) -> {

			UserDTO dto = modelMapper.map(entity, UserDTO.class);
			dtoList.add(dto);
		});

		return dtoList;
	}

	/**
	 * Elimina un usuario por su ID.
	 * 
	 * @param id ID del usuario a eliminar
	 * @return 0 si la eliminación fue exitosa, 1 si el usuario no existe
	 */
	@Override
	public int deleteById(Long id) {
		Optional<User> found = userRepo.findById(id);
		if (found.isPresent()) {
			userRepo.delete(found.get());
			return 0;
		} else {
			return 1;
		}
	}

	/**
	 * Elimina un usuario por su nombre de usuario.
	 * 
	 * @param username Nombre de usuario del usuario a eliminar
	 * @return 0 si la eliminación fue exitosa, 1 si el usuario no existe
	 */
	public int deleteByUsername(String username) {
		Optional<User> found = userRepo.findByUsername(username);
		if (found.isPresent()) {
			userRepo.delete(found.get());
			return 0;
		} else {
			return 1;
		}
	}

	/**
	 * Actualiza un usuario existente por su ID.
	 * Codifica la contraseña antes de guardarla.
	 * 
	 * @param id ID del usuario a actualizar
	 * @param newData DTO con los nuevos datos del usuario
	 * @return 0 si la actualización fue exitosa, 
	 *         1 si el nuevo nombre de usuario ya está en uso,
	 *         2 si el usuario a actualizar no existe,
	 *         3 en otros casos de error
	 */
	@Override
	public int updateById(Long id, UserDTO newData) {
		Optional<User> found = userRepo.findById(id);
		Optional<User> newFound = userRepo.findByUsername(newData.getUsername());

		if (found.isPresent() && !newFound.isPresent()) {
			User temp = found.get();
			temp.setUsername(newData.getUsername());
			// Encriptar la contraseña antes de guardar
			temp.setPassword(passwordEncoder.encode(newData.getPassword()));
			// Actualizar rol si se proporciona
			if (newData.getRole() != null) {
				temp.setRole(newData.getRole());
			}
			userRepo.save(temp);
			return 0;
		}
		if (found.isPresent() && newFound.isPresent()) {
			return 1;
		}
		if (!found.isPresent()) {
			return 2;
		} else {
			return 3;
		}
	}

	/**
	 * Obtiene un usuario por su ID.
	 * 
	 * @param id ID del usuario a obtener
	 * @return DTO del usuario si existe, null en caso contrario
	 */
	public UserDTO getById(Long id) {
		Optional<User> found = userRepo.findById(id);
		if (found.isPresent()) {
			return modelMapper.map(found.get(), UserDTO.class);
		} else {
			return null;
		}
	}

	/**
	 * Verifica si un nombre de usuario ya está en uso.
	 * 
	 * @param newUser Usuario con el nombre de usuario a verificar
	 * @return true si el nombre de usuario ya está en uso, false en caso contrario
	 */
	public boolean findUsernameAlreadyTaken(User newUser) {
		Optional<User> found = userRepo.findByUsername(newUser.getUsername());
		if (found.isPresent()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Verifica si un nombre de usuario ya está en uso.
	 * 
	 * @param username Nombre de usuario a verificar
	 * @return true si el nombre de usuario ya está en uso, false en caso contrario
	 */
	public boolean findUsernameAlreadyTaken(String username) {
		Optional<User> found = userRepo.findByUsername(username);
		return found.isPresent();
	}

	/**
	 * Valida las credenciales de un usuario.
	 * 
	 * @param username Nombre de usuario
	 * @param password Contraseña sin encriptar
	 * @return 0 si las credenciales son válidas, 1 si son inválidas
	 */
	public int validateCredentials(String username, String password) {
		// Buscar usuario por nombre de usuario
		Optional<User> userOpt = userRepo.findByUsername(username);

		// Verificar si el usuario existe y la contraseña coincide
		if (userOpt.isPresent()) {
			User user = userOpt.get();
			if (passwordEncoder.matches(password, user.getPassword())) {
				return 0; // Éxito
			}
		}

		return 1; // Credenciales inválidas
	}

}
