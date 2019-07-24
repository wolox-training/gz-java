package wolox.training.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Preconditions;
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
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import wolox.training.exceptions.BookAlreadyOwnedException;

@Entity
@Table(name="users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @Column(nullable = false)
  private String username;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private LocalDate birthday;

  @Column(nullable = false)
  private String password;

  @ManyToMany(cascade = { CascadeType.REFRESH, CascadeType.MERGE })
  @JsonIgnoreProperties("users")
  private List<Book> books;

  public User() {
    this.books = new Vector<Book>();
  }

  public long getId() {
    return this.id;
  }

  public String getUsername() {
    return this.username;
  }

  public void setUsername(String username) {
    Preconditions.checkArgument(username != null && !username.isEmpty());
    this.username = username;
  }

  public void setPassword(String password) {
    Preconditions.checkArgument(password != null && !password.isEmpty());
    this.password = password;
  }

  public String getPassword() {
    return this.password;
  }

  public LocalDate getBirthday(){
    return this.birthday;
  }

  public void setBirthday(LocalDate birthday) {
    Preconditions.checkArgument(birthday != null && birthday.isBefore(LocalDate.now()));
    this.birthday = birthday;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    Preconditions.checkArgument(name != null && !name.isEmpty());
    this.name = name;
  }

  public List<Book> getBooks() {
    return (List<Book>) Collections.unmodifiableList(this.books);
  }

  public boolean addBook(Book book) throws BookAlreadyOwnedException {
    if (this.books.contains(book))
      throw new BookAlreadyOwnedException();
    return this.books.add(book);
  }

  public boolean removeBook(Book book) {
    return this.books.remove(book);
  }
}
