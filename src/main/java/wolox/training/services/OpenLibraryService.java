package wolox.training.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Stream;
import org.springframework.http.MediaType;
import wolox.training.models.Book;
import org.json.JSONException;
import org.json.JSONObject;

public final class OpenLibraryService {

  private static String _baseUrl = "https://openlibrary.org/api/books?bibkeys=";
  private static String _params = "&format=json&jscmd=data";
  private static String _isbnParam = "ISBN:";
  private static String _getMethod = "GET";

  private OpenLibraryService() { }


  public static Book bookInfo(String isbn) {
    try {
      String stringResponse = getOpenLibraryResponse(isbn);
      return parse(stringResponse);
    } catch (Exception e) {
      return null;
    }
  }

  private static String getOpenLibraryResponse(String isbn) throws IOException {
    String urlString = new StringBuilder().append(_baseUrl).append(_isbnParam).append(isbn).append(_params).toString();
    HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
    connection.setRequestMethod(_getMethod);
    connection.setRequestProperty("Accept", String.valueOf(MediaType.APPLICATION_JSON));
    BufferedReader buffer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    Stream<String> strings = buffer.lines();
    return String.join("", ((String[]) strings.toArray()));
  }

  private static Book parse(String response) throws JSONException {
    JSONObject bookJson = new JSONObject(response);
    Book book = new Book();
    book.setTitle(bookJson.getString("title"));
    book.setSubtitle(bookJson.getString("subtitle"));
    book.setYear(bookJson.getString("publish_date"));
    book.setPages(bookJson.getInt("number_of_pages"));
    return book;
  }
}
