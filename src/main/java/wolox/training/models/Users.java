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
import javax.persistence.ManyToMany;
import wolox.training.exceptions.BookAlreadyOwnedException;

@Entity
public class Users {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @Column(nullable = false)
  private String username;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private LocalDate birthday;

  @ManyToMany(cascade = { CascadeType.REFRESH, CascadeType.MERGE })
  private List<Book> books;

  public Users() {
    this.books = new Vector<Book>();
  }

  public long getId() {
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

  public boolean addBook(Book book) throws BookAlreadyOwnedException {
    if (this.books.contains(book))
      throw new BookAlreadyOwnedException();
    return this.books.add(book);
  }

  public boolean removeBook(Book book) {
    return this.books.remove(book);
  }
}
