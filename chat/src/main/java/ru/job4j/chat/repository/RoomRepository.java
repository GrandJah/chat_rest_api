package ru.job4j.chat.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import javax.transaction.Transactional;
import ru.job4j.chat.model.Message;
import ru.job4j.chat.model.Room;

public interface RoomRepository extends CrudRepository<Room, Integer> {
  Optional<Room> findByNameRoom(String name);

  @Transactional
  void deleteByNameRoom(String name);
}
