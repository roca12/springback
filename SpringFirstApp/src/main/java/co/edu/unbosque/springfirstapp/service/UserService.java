package co.edu.unbosque.springfirstapp.service;

import co.edu.unbosque.springfirstapp.dto.UserDTO;
import co.edu.unbosque.springfirstapp.model.User;
import co.edu.unbosque.springfirstapp.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements CRUDOperation<UserDTO> {

  @Autowired private UserRepository userRepo;

  @Autowired private ModelMapper modelMapper;

  public UserService() {}

  @Override
  public long count() {
    return userRepo.count();
  }

  @Override
  public boolean exist(Long id) {
    return userRepo.existsById(id) ? true : false;
  }

  @Override
  public int create(UserDTO data) {
    User entity = modelMapper.map(data, User.class);
    if (findUsernameAlreadyTaken(entity)) {
      return 1;
    } else {
      userRepo.save(entity);
      return 0;
    }
  }

  @Override
  public List<UserDTO> getAll() {
    List<User> entityList = userRepo.findAll();
    List<UserDTO> dtoList = new ArrayList<>();
    entityList.forEach(
        (entity) -> {
          UserDTO dto = modelMapper.map(entity, UserDTO.class);
          dtoList.add(dto);
        });

    return dtoList;
  }

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

  public int deleteByUsername(String username) {
    Optional<User> found = userRepo.findByUsername(username);
    if (found.isPresent()) {
      userRepo.delete(found.get());
      return 0;
    } else {
      return 1;
    }
  }

  @Override
  public int updateById(Long id, UserDTO newData) {
    Optional<User> found = userRepo.findById(id);
    Optional<User> newFound = userRepo.findByUsername(newData.getUsername());

    if (found.isPresent() && !newFound.isPresent()) {
      User temp = found.get();
      temp.setUsername(newData.getUsername());
      temp.setPassword(newData.getPassword());
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

  public UserDTO getById(Long id) {
    Optional<User> found = userRepo.findById(id);
    if (found.isPresent()) {
      return modelMapper.map(found.get(), UserDTO.class);
    } else {
      return null;
    }
  }

  public boolean findUsernameAlreadyTaken(User newUser) {
    Optional<User> found = userRepo.findByUsername(newUser.getUsername());
    if (found.isPresent()) {
      return true;
    } else {
      return false;
    }
  }

  public int validateCredentials(String username, String password) {
    // encriptado del front
    // username = AESUtil.decrypt("keyfrontfirstenc", "iviviviviviviviv", username);
    // password = AESUtil.decrypt("keyfrontfirstenc", "iviviviviviviviv", password);
    // a encriptrado del back
    // username = AESUtil.encrypt(username);
    // password = AESUtil.encrypt(password);
    for (UserDTO u : getAll()) {
      if (u.getUsername().equals(username)) {
        if (u.getPassword().equals(password)) {
          return 0;
        }
      }
    }
    return 1;
  }
}
