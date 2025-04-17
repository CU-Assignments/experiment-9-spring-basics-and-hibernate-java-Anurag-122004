public class Course {
    private String courseName;
    private int duration;

    public Course(String courseName, int duration) {
        this.courseName = courseName;
        this.duration = duration;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return "Course: " + courseName + ", Duration: " + duration + " weeks";
    }
}

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public Course course() {
        return new Course("Java Spring", 6);
    }

    @Bean
    public Student student() {
        return new Student("Alice", course());
    }
}

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainApp {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        Student student = context.getBean(Student.class);
        student.displayInfo();
    }
}

<hibernate-configuration>
 <session-factory>
   <property name="hibernate.connection.driver_class">com.mysql.cj.jdbc.Driver</property>
   <property name="hibernate.connection.url">jdbc:mysql://localhost:3306/your_database</property>
   <property name="hibernate.connection.username">root</property>
   <property name="hibernate.connection.password">your_password</property>
   <property name="hibernate.dialect">org.hibernate.dialect.MySQL5Dialect</property>
   <property name="hibernate.hbm2ddl.auto">update</property>
   <property name="show_sql">true</property>
   <mapping class="Student"/>
 </session-factory>
</hibernate-configuration>


import javax.persistence.*;

@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int age;

    public Student() {}

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // Getters and Setters
}


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class StudentDAO {
    SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

    public void saveStudent(Student student) {
        Session session = factory.openSession();
        session.beginTransaction();
        session.save(student);
        session.getTransaction().commit();
        session.close();
    }

    public Student getStudent(int id) {
        Session session = factory.openSession();
        Student student = session.get(Student.class, id);
        session.close();
        return student;
    }

    // update, delete, listAll etc.
}

public class MainApp {
    public static void main(String[] args) {
        StudentDAO dao = new StudentDAO();
        dao.saveStudent(new Student("John", 22));
        Student s = dao.getStudent(1);
        System.out.println("Fetched: " + s.getName());
    }
}

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import javax.persistence.*;
import java.util.Date;

@Entity
class Account {
    @Id private int accountId;
    private String name;
    private double balance;

    public Account() {}
    public Account(int id, String name, double balance) {
        this.accountId = id; this.name = name; this.balance = balance;
    }
    public int getAccountId() { return accountId; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
}

@Entity
class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int transactionId;
    private int fromAccount, toAccount;
    private double amount;
    private Date timestamp;

    public Transfer() {}
    public Transfer(int from, int to, double amount) {
        this.fromAccount = from;
        this.toAccount = to;
        this.amount = amount;
        this.timestamp = new Date();
    }
}

public class App {
    public static void main(String[] args) {
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Account.class)
                .addAnnotatedClass(Transfer.class)
                .buildSessionFactory();

        Session session = factory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            Account from = session.get(Account.class, 101);
            Account to = session.get(Account.class, 102);
            double amount = 300;

            if (from.getBalance() < amount) {
                throw new RuntimeException("Insufficient balance");
            }

            from.setBalance(from.getBalance() - amount);
            to.setBalance(to.getBalance() + amount);

            session.update(from);
            session.update(to);
            session.save(new Transfer(from.getAccountId(), to.getAccountId(), amount));

            tx.commit();
            System.out.println("Transfer successful");

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.out.println("Transfer failed: " + e.getMessage());
        } finally {
            session.close();
            factory.close();
        }
    }
}


<hibernate-configuration>
 <session-factory>
   <property name="hibernate.connection.driver_class">com.mysql.cj.jdbc.Driver</property>
   <property name="hibernate.connection.url">jdbc:mysql://localhost:3306/bankdb</property>
   <property name="hibernate.connection.username">root</property>
   <property name="hibernate.connection.password">your_password</property>
   <property name="hibernate.dialect">org.hibernate.dialect.MySQL5Dialect</property>
   <property name="hibernate.hbm2ddl.auto">update</property>
   <property name="show_sql">true</property>
 </session-factory>
</hibernate-configuration>
