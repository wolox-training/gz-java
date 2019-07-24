package wolox.training.models;

import com.google.common.base.Preconditions;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Book {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @Column
  private String genre;

  @Column(nullable = false)
  private String author;

  @Column(nullable = false)
  private String image;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String subtitle;

  @Column(nullable = false)
  private String publisher;

  @Column(nullable = false)
  private String year;

  @Column(nullable = false)
  private int pages;

  @Column(nullable = false)
  private String isbn;

  @ManyToMany(mappedBy = "books")
  private List<User> users;

  public Book() {
    this.users = new Vector<User>();
  }

  public long getId() {
    return this.id;
  }

  public String getGenre() {
    return this.genre;
  }

  public void setGenre(String genre) {
    this.genre = genre;
  }

  public String getImage() {
    return this.image;
  }

  public void setImage(String image) {
    Preconditions.checkArgument(image != null && !image.isEmpty());
    this.image = image;
  }

  public String getTitle() {
    return this.title;
  }

  public void setTitle(String title) {
    Preconditions.checkArgument(title != null && !title.isEmpty());
    this.title = title;
  }

  public String getSubtitle() {
    return this.subtitle;
  }

  public void setSubtitle(String subtitle) {
    Preconditions.checkArgument(subtitle != null && !subtitle.isEmpty());
    this.subtitle = subtitle;
  }

  public String getPublisher() {
    return this.publisher;
  }

  public void setPublisher(String publisher) {
    Preconditions.checkArgument(publisher != null && !publisher.isEmpty());
    this.publisher = publisher;
  }

  public String getYear() {
    return this.year;
  }

  public void setYear(String year) {
    Preconditions.checkArgument(year != null && !year.isEmpty());
    this.year = year;
  }

  public int getPages() {
    return this.pages;
  }

  public void setPages(int pages) {
    Preconditions.checkArgument(pages > 0);
    this.pages = pages;
  }

  public String getIsbn() {
    return this.isbn;
  }

  public void setIsbn(String isbn) {
    Preconditions.checkArgument(isbn != null && !isbn.isEmpty());
    this.isbn = isbn;
  }

  public String getAuthor() {
    return this.author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public List<User> getUsers() {
    return (List<User>) Collections.unmodifiableList(this.users);
  }
}
