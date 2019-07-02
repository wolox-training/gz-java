package wolox.training.models;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class Users {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  @Column(nullable = false)
  private String username;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private LocalDate birthday;

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(name = "book_user",
      joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
  private List<Book> books;

  public Users() {
    this.books = new Vector<Book>();
  }

  public int getId() {
    return this.id;
  }

  public String getUsername() {
    return this.username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public LocalDate getBirthday(){
    return this.birthday;
  }

  public void setBirthday(LocalDate birthday) {
    this.birthday = birthday;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Book> getBooks() {
    return (List<Book>) Collections.unmodifiableList(this.books);
  }
}
