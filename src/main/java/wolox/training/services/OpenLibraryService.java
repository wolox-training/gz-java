package wolox.training.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import wolox.training.exceptions.NullValueException;
import wolox.training.models.Book;

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

  private static Book parse(String response, String isbn) throws NullValueException, IOException {
    JsonNode rootNode = new ObjectMapper().readTree(response);
    JsonNode values = rootNode.findValue(new StringBuilder().append(_isbnParam).append(isbn).toString());
    Book book = new Book();
    book.setIsbn(isbn);
    if (values.has("title"))
      book.setTitle(values.get("title").asText());
    if (values.has("subtitle"))
      book.setSubtitle(values.get("subtitle").asText());
    if (values.has("publish_date"))
      book.setYear(values.get("publish_date").asText());
    if (values.has("number_of_pages"))
      book.setPages(values.get("number_of_pages").asInt());
    if (values.has("publishers"))
      book.setPublisher(buildStringFromArray(values.get("publishers")));
    if (values.has("authors"))
      book.setAuthor(buildStringFromArray(values.get("authors")));
    return book;
  }

  private static String buildStringFromArray(JsonNode jsonNode) {
    Iterator<JsonNode> elements = jsonNode.elements();
    StringBuilder stringBuilder = new StringBuilder();
    while (elements.hasNext()) {
      JsonNode element = elements.next();
      stringBuilder.append(element.get("name").asText());
      stringBuilder.append(", ");
    }
    stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
    return stringBuilder.toString();
  }
}
