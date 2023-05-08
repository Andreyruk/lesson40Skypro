import dao.CityDao;
import dao.Dao;
import dao.EmployeeDao;
import model.City;
import model.Employee;

import java.util.List;

public class Application {
    private static Dao<City> cityDao;
    private static Dao<Employee> employeeDao;

    public static void main(String[] args) {
        cityDao = new CityDao();
        employeeDao = new EmployeeDao();

        City city = new City();
        city.setCityName("Москва");
        cityDao.save(city);
        city = cityDao.getAll().stream().filter(i -> i.getCityName().equals("Москва")).findFirst().orElseThrow(() -> new RuntimeException("Город не найден"));
        System.out.println(city.getCityName());

        Employee employee = new Employee();
        employee.setFirstName("Вовка");
        employee.setLastName("Вовкин");
        employee.setGender("male");
        employee.setAge(70);
        employee.setCity(city);

        employeeDao.save(employee);
        List<Employee> employees = employeeDao.getAll();
        System.out.println(employees);
        Employee createdEmployee = employees.stream().filter(i -> i.getFirstName().equals("Вовка") && i.getLastName().equals("Вовкин")).findFirst().orElseThrow(() -> new RuntimeException("Сотрудник не найден"));
        System.out.println(employeeDao.get(createdEmployee.getId()).getFirstName());
        //employeeDao.delete(createdEmployee.getId());
        System.out.println(employeeDao.getAll());
        cityDao.delete(city.getCityId());
    }
}
