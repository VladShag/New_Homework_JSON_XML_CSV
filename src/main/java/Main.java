import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
        writeString(json, "data.json");
        List<Employee> list1 = parseXML("data.xml");
        String json1 = listToJson(list1);
        writeString(json1, "data1.json");
        String jsonString = readString("data1.json");
        List<Employee> finalList = jsonToList(jsonString);
        for (Employee emp : finalList) {
            System.out.println(emp);
        }


    }

    public static List<Employee> parseCSV(String[] columnMapping, String file) {
        try (CSVReader csvReader = new CSVReader(new FileReader(file));) {
            ColumnPositionMappingStrategy columnPositionMappingStrategy = new ColumnPositionMappingStrategy();
            columnPositionMappingStrategy.setType(Employee.class);
            columnPositionMappingStrategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader).withMappingStrategy(columnPositionMappingStrategy).build();
            return csv.parse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String listToJson(List<Employee> list) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        return gson.toJson(list, listType);
    }

    public static void writeString(String file, String fileName) {
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(file);
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static List<Employee> parseXML(String file) {
        List<Employee> result = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(file));
            Node root = doc.getDocumentElement();
            NodeList nodeList = root.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (Node.ELEMENT_NODE == node.getNodeType()) {
                    Element elem = (Element) node;
                    result.add(new Employee(Long.parseLong(elem.getElementsByTagName("id").item(0).getTextContent()),
                            elem.getElementsByTagName("firstName").item(0).getTextContent(),
                            elem.getElementsByTagName("lastName").item(0).getTextContent(),
                            elem.getElementsByTagName("country").item(0).getTextContent(),
                            Integer.parseInt(elem.getElementsByTagName("age").item(0).getTextContent())));
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static String readString(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Employee> jsonToList(String json) {
        JSONParser jsonParser = new JSONParser();
        List<Employee> result = new ArrayList<>();
        try {
            JSONArray list = (JSONArray) jsonParser.parse(json);
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            for (Object o : list) {
                JSONObject jsonObject = (JSONObject) o;
                result.add(gson.fromJson(String.valueOf(jsonObject), Employee.class));
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}

