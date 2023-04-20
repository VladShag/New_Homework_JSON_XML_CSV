import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class Tests {
    @org.junit.jupiter.api.Test
    public void testCSVToListIfFileExistAndColumnMappingCorrect() {
        List<Employee> expectedList = new ArrayList<>();
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        expectedList.add(new Employee(1,"John","Smith","USA",25));
        expectedList.add(new Employee(2,"Inav","Petrov","RU",23));
        List<Employee> result = Main.parseCSV(columnMapping,"data.csv");
        assertEquals(expectedList.toString(),result.toString());

    }
    @org.junit.jupiter.api.Test
    public void testCSVToListIfFileNotExist() {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        assertThrows(RuntimeException.class,() -> Main.parseCSV(columnMapping,"data22.csv"));

    }
    @org.junit.jupiter.api.Test
    public void testCSVToListIfColumnMappingWrong() {
        String[] columnMapping = {"something", "name", "number", "homeAdress", "profession"};
        List<Employee> result = Main.parseCSV(columnMapping,"data.csv");
        assertNotEquals(result.get(0), new Employee(1,"John","Smith","USA",25));
    }
    @org.junit.jupiter.api.Test
    public void testCSVToListIfColumnMappingWrongWithHamrest() {
        String[] columnMapping = {"something", "name", "number", "homeAdress", "profession"};
        List<Employee> result = Main.parseCSV(columnMapping,"data.csv");
        assertThat(result.get(0), not(equalTo(new Employee(1,"John","Smith","USA",25))));


    }
    @org.junit.jupiter.api.Test
    public void ListToJSONTest(){
        String expected = "[{\"id\":1,\"firstName\":\"John\",\"lastName\":\"Smith\",\"country\":\"USA\",\"age\":25},{\"id\":2,\"firstName\":\"Inav\",\"lastName\":\"Petrov\",\"country\":\"RU\",\"age\":23}]";
        List<Employee> testList = new ArrayList<>();
        testList.add(new Employee(1,"John","Smith","USA",25));
        testList.add(new Employee(2,"Inav","Petrov","RU",23));
        assertEquals(Main.listToJson(testList), expected);


    }
    @org.junit.jupiter.api.Test
    public void ListToJSONTestWithHamrest(){
        String expected = "[{\"id\":1,\"firstName\":\"John\",\"lastName\":\"Smith\",\"country\":\"USA\",\"age\":25},{\"id\":2,\"firstName\":\"Inav\",\"lastName\":\"Petrov\",\"country\":\"RU\",\"age\":23}]";
        List<Employee> testList = new ArrayList<>();
        testList.add(new Employee(1,"John","Smith","USA",25));
        testList.add(new Employee(2,"Inav","Petrov","RU",23));
        assertThat(Main.listToJson(testList),equalTo(expected));


    }
    @org.junit.jupiter.api.Test
    public void readStringFromFileJSONIfDataIsCorrect() {
        String expected = "[{\"id\":1,\"firstName\":\"John\",\"lastName\":\"Smith\",\"country\":\"USA\",\"age\":25},{\"id\":2,\"firstName\":\"Inav\",\"lastName\":\"Petrov\",\"country\":\"RU\",\"age\":23}]";
        assertThat(Main.readString("data.json"), equalTo(expected));

    }
    @org.junit.jupiter.api.Test
    public void testXMLIfDataIsCorrect() {
        List<Employee> expectedList = new ArrayList<>();
        expectedList.add(new Employee(1,"John","Smith","USA",25));
        expectedList.add(new Employee(2,"Inav","Petrov","RU",23));
        List<Employee> result = Main.parseXML("data.xml");
        assertThat(result, not(empty()));
        assertThat(result.toString(), equalTo(expectedList.toString()));

    }

}
