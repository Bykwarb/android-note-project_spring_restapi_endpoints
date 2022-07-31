package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@JsonFilter("roomFilter")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = User.class, cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(name = "creator_id")
    @JsonIgnoreProperties({"email", "password", "nickName", "rooms", "registrationDate"})
    private User creator;
    private String roomName;

    @ManyToMany(targetEntity = User.class, cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"email", "password", "nickName", "rooms", "registrationDate"})
    private List<User> users;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "room")
    @JsonIgnoreProperties({"room", "creationDate"})
    private List<Note> notes;

    @Column(name = "creation_date")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date creationDate;

    public Room() {
        creationDate = new Date();
        users = new ArrayList<>();
        notes = new ArrayList<>();
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return Objects.equals(id, room.id) && Objects.equals(creator, room.creator) && Objects.equals(users, room.users) && Objects.equals(notes, room.notes) && Objects.equals(creationDate, room.creationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creator, users, notes, creationDate);
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", creator=" + creator +
                ", users=" + users +
                ", notes=" + notes +
                ", creationDate=" + creationDate +
                '}';
    }
}
