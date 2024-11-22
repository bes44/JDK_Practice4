package PhoneData;

/*
Создать справочник сотрудников
Необходимо:
Создать класс справочник сотрудников, который содержит внутри
коллекцию сотрудников - каждый сотрудник должен иметь следующие атрибуты:
Табельный номер
Номер телефона
Имя
Стаж
Добавить метод, который ищет сотрудника по стажу (может быть список)
Добавить метод, который возвращает номер телефона сотрудника по имени (может быть список)
Добавить метод, который ищет сотрудника по табельному номеру
Добавить метод добавления нового сотрудника в справочник
 */


import java.util.List;

public class Main {
    public static void main(String[] args) {
        EmployeeDirectory directory = new EmployeeDirectory();

        // Добавление сотрудников
        directory.addEmployee(new Employee(1, "123-456-7890", "Иван", 5));
        directory.addEmployee(new Employee(2, "098-765-4321", "Анна", 3));
        directory.addEmployee(new Employee(3, "234-567-8901", "Никита", 5));
        directory.addEmployee(new Employee(4, "345-678-9012", "Колумбина", 2));


        List<Employee> employeesWithFiveYears = directory.findEmployeesByExperience(5);
        System.out.println("Сотрудники с 5 годами стажа: " + employeesWithFiveYears);

        List<String> phoneNumbers = directory.findPhoneNumbersByName("Анна");
        System.out.println("Номера телефонов Анны: " + phoneNumbers);

        Employee employeeById = directory.findEmployeeById(3);
        System.out.println("Сотрудник с табельным номером 3: " + employeeById);
    }
}