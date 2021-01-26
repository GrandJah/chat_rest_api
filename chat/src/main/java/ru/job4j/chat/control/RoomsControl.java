package ru.job4j.chat.control;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import ru.job4j.chat.model.Message;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.repository.MessageRepository;
import ru.job4j.chat.repository.PersonRepository;
import ru.job4j.chat.repository.RoomRepository;

@Slf4j
@RestController
@RequestMapping("/room")
public class RoomsControl {
  private final RoomRepository rooms;

  private final MessageRepository messages;

  private final Person stubPerson;

  public RoomsControl(RoomRepository room, MessageRepository messages, PersonRepository persons) {
    this.rooms = room;
    this.messages = messages;
    this.stubPerson = persons.save(Person.of(String.format("stubUser_created:%s", Instant.now().toString())));
  }

  @GetMapping("/")
  public List<Room> findAllRooms() {
    return StreamSupport.stream(this.rooms.findAll()
                                          .spliterator(), false)
                        .collect(Collectors.toList());
  }

  @GetMapping("/{name}")
  public ResponseEntity<Room> findByNameRoom(@PathVariable String name) {
    Optional<Room> findRoom = this.rooms.findByNameRoom(name);
    return new ResponseEntity<>(findRoom.orElse(new Room()),
      findRoom.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
  }

  @PostMapping("/")
  public ResponseEntity<Room> create(@RequestBody Room room) {
    log.info("room {}", room);
    Room newRoom = Room.of(room.getNameRoom(), stubPerson);
    this.rooms.save(newRoom);
    return new ResponseEntity<>(newRoom, HttpStatus.CREATED);
  }

  @PostMapping("/{name}")
  public ResponseEntity<Void> createMessage(@PathVariable String name,
    @RequestBody Message message) {
    log.info("name: {} , message: {}", name, message);
    Optional<Room> findRoom = this.rooms.findByNameRoom(name);
    log.info("findRoom: {} ", findRoom);
    if (findRoom.isPresent()) {
      Room room = findRoom.get();
      room.addMessage(Message.of(message.getMessage(), stubPerson));
      this.rooms.save(room);
      return ResponseEntity.status(HttpStatus.CREATED)
                           .build();
    }
    return ResponseEntity.notFound()
                         .build();
  }

  @PutMapping("/{name}")
  public ResponseEntity<Void> update(@PathVariable String name, @RequestBody Message newMessage) {
    Optional<Message> findMessage = this.messages.findById(newMessage.getId());
    findMessage.ifPresent(message -> {
      if (message.getRoom()
                 .getNameRoom()
                 .equals(name)) {
        message.setMessage(newMessage.getMessage());
        this.messages.save(message);
      }
    });
    return ResponseEntity.ok()
                         .build();
  }


  @DeleteMapping("/{name}")
  public ResponseEntity<Void> delete(@PathVariable String name) {
    this.rooms.deleteByNameRoom(name);
    return ResponseEntity.ok()
                         .build();
  }
}
