import java.util.*;


class Student {
    private final String id;
    private String name;
    private int age;

    public Student(String id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }

    @Override
    public String toString() {
        return "Student{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", age=" + age + '}';
    }
}


interface StudentDao {
    void addStudent(Student student);
    void updateStudent(String id, String name, int age);
    void removeStudent(String id);
    List<Student> getAllStudents();
}


class StudentDaoImpl implements StudentDao {
    private final Map<String, Student> studentMap = new HashMap<>();

    @Override
    public void addStudent(Student student) {
        studentMap.put(student.getId(), student);
    }

    @Override
    public void updateStudent(String id, String name, int age) {
        Student student = studentMap.get(id);
        if (student != null) {
            student.setName(name);
            student.setAge(age);
        }
    }

    @Override
    public void removeStudent(String id) {
        studentMap.remove(id);
    }

    @Override
    public List<Student> getAllStudents() {
        return new ArrayList<>(studentMap.values());
    }
}


class StudentService {
    private final StudentDao studentDao;

    public StudentService(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    public void addStudent(String id, String name, int age) {
        studentDao.addStudent(new Student(id, name, age));
    }

    public void updateStudent(String id, String name, int age) {
        studentDao.updateStudent(id, name, age);
    }

    public void removeStudent(String id) {
        studentDao.removeStudent(id);
    }

    public List<Student> getAllStudents() {
        return studentDao.getAllStudents();
    }
}


abstract class MenuItem {
    protected final StudentService studentService;
    public MenuItem(StudentService studentService) {
        this.studentService = studentService;
    }
    public abstract void execute(Scanner scanner);
    public abstract String getLabel();
}

class AddStudentMenuItem extends MenuItem {
    public AddStudentMenuItem(StudentService studentService) { super(studentService); }
    @Override
    public void execute(Scanner scanner) {
        System.out.print("Enter ID: ");
        String id = scanner.next();
        System.out.print("Enter Name: ");
        String name = scanner.next();
        System.out.print("Enter Age: ");
        int age = scanner.nextInt();
        studentService.addStudent(id, name, age);
        System.out.println("Student added successfully.");
    }
    @Override
    public String getLabel() { return "Add Student"; }
}

class UpdateStudentMenuItem extends MenuItem {
    public UpdateStudentMenuItem(StudentService studentService) { super(studentService); }
    @Override
    public void execute(Scanner scanner) {
        System.out.print("Enter Student ID to Update: ");
        String id = scanner.next();
        System.out.print("Enter New Name: ");
        String name = scanner.next();
        System.out.print("Enter New Age: ");
        int age = scanner.nextInt();
        studentService.updateStudent(id, name, age);
        System.out.println("Student updated successfully.");
    }
    @Override
    public String getLabel() { return "Update Student"; }
}

class RemoveStudentMenuItem extends MenuItem {
    public RemoveStudentMenuItem(StudentService studentService) { super(studentService); }
    @Override
    public void execute(Scanner scanner) {
        System.out.print("Enter Student ID to Remove: ");
        String id = scanner.next();
        studentService.removeStudent(id);
        System.out.println("Student removed successfully.");
    }
    @Override
    public String getLabel() { return "Remove Student"; }
}

class ShowStudentsMenuItem extends MenuItem {
    public ShowStudentsMenuItem(StudentService studentService) { super(studentService); }
    @Override
    public void execute(Scanner scanner) {
        List<Student> students = studentService.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No students available.");
        } else {
            students.forEach(System.out::println);
        }
    }
    @Override
    public String getLabel() { return "Show Students"; }
}

// Main 
public class StudentApplication {
    public static void main(String[] args) {
        StudentDao studentDao = new StudentDaoImpl();
        StudentService studentService = new StudentService(studentDao);
        List<MenuItem> menuItems = List.of(
                new AddStudentMenuItem(studentService),
                new UpdateStudentMenuItem(studentService),
                new RemoveStudentMenuItem(studentService),
                new ShowStudentsMenuItem(studentService)
        );
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nStudent Management System");
            for (int i = 0; i < menuItems.size(); i++) {
                System.out.println((i + 1) + ". " + menuItems.get(i).getLabel());
            }
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            if (choice == 0) break;
            if (choice >= 1 && choice <= menuItems.size()) {
                menuItems.get(choice - 1).execute(scanner);
            } else {
                System.out.println("Invalid choice. Try again.");
            }
        }
        scanner.close();
        System.out.println("Goodbye!");
    }
}
