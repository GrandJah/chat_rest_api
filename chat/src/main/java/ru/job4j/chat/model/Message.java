package ru.job4j.chat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Message {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(nullable = false)
  private String message;

  @ManyToOne
  @JoinColumn(nullable = false)
  private Person ownership;

  @ManyToOne
  @JsonIgnore
  @JoinColumn(nullable = false)
  private Room room;

  public static Message of(String message, Person ownership) {
    Message r = new Message();
    r.ownership = ownership;
    r.message = message;
    return r;
  }
}
