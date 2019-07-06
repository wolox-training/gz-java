package wolox.training.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;
import netscape.javascript.JSObject;
import org.json.JSONArray;
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
      return parse(stringResponse, isbn);
    } catch (Exception e) {
      return null;
    }
  }

  private static String getOpenLibraryResponse(String isbn) throws IOException {
    String urlString = new StringBuilder().append(_baseUrl).append(_isbnParam).append(isbn).append(_params).toString();
    HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
    connection.setRequestMethod(_getMethod);
    BufferedReader buffer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    StringBuilder json = new StringBuilder();
    String line;
    while ((line = buffer.readLine()) != null)
      json.append(line);
    return json.toString();
  }

  private static Book parse(String response, String isbn) throws JSONException {
    JSONObject bookJson = new JSONObject(response).getJSONObject(new StringBuilder().append(_isbnParam).append(isbn).toString());
    Book book = new Book();
    book.setIsbn(isbn);
    if (bookJson.has("title"))
      book.setTitle(bookJson.getString("title"));
    if (bookJson.has("subtitle"))
      book.setSubtitle(bookJson.getString("subtitle"));
    if (bookJson.has("publish_date"))
      book.setYear(bookJson.getString("publish_date"));
    if (bookJson.has("number_of_pages"))
      book.setPages(bookJson.getInt("number_of_pages"));
    if (bookJson.has("publishers"))
      book.setPublisher(buildStringFromArray(bookJson.getJSONArray("publishers")));
    if (bookJson.has("authors"))
      book.setAuthor(buildStringFromArray(bookJson.getJSONArray("authors")));
    return book;
  }

  private static String buildStringFromArray(JSONArray jsonArray) throws JSONException {
    Vector<String> stringList = new Vector();
    for (int i = 0; i < jsonArray.length(); i++)
      stringList.add(((JSONObject)jsonArray.get(i)).getString("name"));
    return String.join(", ", stringList);
  }
}
