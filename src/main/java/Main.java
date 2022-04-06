import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        List<Employee> listOfEmployees = parseXML("data.xml");
        String json = parseListToJson(listOfEmployees);
        writeString(json);
    }

// Разбор XML-файла в список
    private static List<Employee> parseXML(String filePath) throws IOException, ParserConfigurationException, SAXException {
        List<Employee> staff = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File(filePath));

        Node root = document.getDocumentElement();
        NodeList nodeList = root.getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (Node.ELEMENT_NODE == node.getNodeType()) {
                Element element = (Element) node;

                    long id = Long.parseLong(element.getElementsByTagName("id").item(0).getTextContent());
                    String firstName = element.getElementsByTagName("firstName").item(0).getTextContent();
                    String lastName = element.getElementsByTagName("lastName").item(0).getTextContent();
                    String country = element.getElementsByTagName("country").item(0).getTextContent();
                    int age = Integer.parseInt(element.getElementsByTagName("age").item(0).getTextContent());

                Employee employee = new Employee(id, firstName, lastName, country, age);
                staff.add(employee);
            }
        }
        return staff;
    }

// Сохранение Json в файл
    private static void writeString(String json) {
        try (FileWriter fileWriter = new FileWriter(new File("data.json"))) {
            fileWriter.write(json);
            fileWriter.flush();
        } catch (IOException ex) {
            System.err.println("Не удалось записать файл");
        }
    }

// Преобразование списка в Json
    private static String parseListToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        String json = gson.toJson(list, listType);
        return json;
    }
}