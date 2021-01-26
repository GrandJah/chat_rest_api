package ru.job4j.chat.model;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class Room {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(unique = true, nullable = false)
  private String nameRoom;

  @ManyToOne
  @JoinColumn(nullable = false)
  private Person ownership;

  @OneToMany(mappedBy = "room")
  @Cascade(value = CascadeType.ALL)
  private List<Message> messageHistory = new ArrayList<>();

  public static Room of(String name, Person ownership) {
    Room r = new Room();
    r.nameRoom = name;
    r.ownership = ownership;
    return r;
  }

  public void addMessage(Message message) {
    message.setRoom(this);
    messageHistory.add(message);
  }
}
