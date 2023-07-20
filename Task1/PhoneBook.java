package Task1;

import java.io.*;
import java.util.*;

public class PhoneBook {
  static class Contact {
    private String name;
    private ArrayList<String> phoneNumbers;
    private String address;
    private String email;

    public Contact(String name) {
      this.name = name;
      phoneNumbers = new ArrayList<>();
    }

    public String getName() {
      return name;
    }

    public ArrayList<String> getPhoneNumbers() {
      return phoneNumbers;
    }

    public String getAddress() {
      return address;
    }

    public void setAddress(String address) {
      this.address = address;
    }

    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("Name: " + name + "\n");
      sb.append("Phone Numbers: ").append(phoneNumbers).append("\n");
      sb.append("Address: ").append(address).append("\n");
      sb.append("Email: ").append(email).append("\n");
      return sb.toString();
    }
  }

  public static void main(String[] args) {

    TreeMap<String, Contact> phoneBook = loadPhoneBook();
    Scanner scanner = new Scanner(System.in);

    while (true) {
      System.out.println("Введите цифру 1, чтобы показать элемент:");
      System.out.println("Введите цифру 2, чтобы добавить элемент:");
      System.out.println("Введите цифру 3, чтобы удалить элемент:");
      System.out.println("Введите цифру 4, чтобы искать контакт по номеру телефона:");
      System.out.println("Введите цифру 5, чтобы искать контакт по части имени:");
      System.out.println("Введите цифру 6, выход из программы:\n");
      System.out
          .println("Наша программа предназначена для иностранной компании.Используйте при вводе английский язык\n");
      System.out.print("Введите цифру: ");

      int choice = scanner.nextInt();
      scanner.nextLine();

      switch (choice) {
        case 1:
          showPhoneBook(phoneBook);
          break;
        case 2:
          addContact(phoneBook, scanner);
          break;
        case 3:
          removeContact(phoneBook, scanner);
          break;
        case 4:
          searchByPhoneNumber(phoneBook, scanner);
          break;
        case 5:
          searchByPartialName(phoneBook, scanner);
          break;
        case 6:
          savePhoneBook(phoneBook);
          System.out.println("Выход из программы...");
          return;
        default:
          System.out.println("Неверный выбор, попробуйте снова.\n");
      }
    }
  }

  private static void showPhoneBook(TreeMap<String, Contact> phoneBook) {
    if (phoneBook.isEmpty()) {
      System.out.println("Телефонная книга пуста.\n");
      return;
    }

    for (Contact contact : phoneBook.values()) {
      System.out.println(contact);
    }
  }

  private static void addContact(TreeMap<String, Contact> phoneBook, Scanner scanner) {
    System.out.print("Введите имя контакта: ");
    String name = scanner.nextLine();

    Contact contact = phoneBook.get(name);
    if (contact == null) {
      contact = new Contact(name);
      phoneBook.put(name, contact);
    }

    System.out.print("Введите номер телефона: ");
    String phoneNumber = scanner.nextLine();
    contact.getPhoneNumbers().add(phoneNumber);

    System.out.print("Введите адрес: ");
    String address = scanner.nextLine();
    contact.setAddress(address);

    System.out.print("Введите email: ");
    String email = scanner.nextLine();
    contact.setEmail(email);

    System.out.println("Контакт добавлен.\n");
  }

  private static void removeContact(TreeMap<String, Contact> phoneBook, Scanner scanner) {
    System.out.print("Введите имя контакта, которого хотите удалить: ");
    String name = scanner.next();

    if (phoneBook.containsKey(name)) {
      phoneBook.remove(name);
      System.out.println("Контакт удален.\n");
    } else {
      System.out.println("Контакт не найден в телефонной книге.\n");
    }
  }

  private static void searchByPhoneNumber(TreeMap<String, Contact> phoneBook, Scanner scanner) {
    System.out.print("Введите номер телефона для поиска: ");
    String phoneNumber = scanner.next();

    boolean found = false;
    for (Contact contact : phoneBook.values()) {
      if (contact.getPhoneNumbers().contains(phoneNumber)) {
        System.out.println("Найден контакт для номера " + phoneNumber + ":\n" + contact);
        found = true;
      }
    }
    if (!found) {
      System.out.println("Контакт не найден для номера " + phoneNumber);
    }
  }

  private static void searchByPartialName(TreeMap<String, Contact> phoneBook, Scanner scanner) {
    System.out.print("Введите часть имени для поиска: ");
    String partialName = scanner.next().toLowerCase();

    boolean found = false;
    for (Contact contact : phoneBook.values()) {
      if (contact.getName().toLowerCase().contains(partialName)) {
        System.out.println("Найден контакт по части имени " + partialName + ":\n" + contact);
        found = true;
      }
    }
    if (!found) {
      System.out.println("Контакт не найден по части имени " + partialName);
    }
  }

  private static TreeMap<String, Contact> loadPhoneBook() {
    TreeMap<String, Contact> phoneBook = new TreeMap<>();
    try (BufferedReader reader = new BufferedReader(new FileReader("phonebook.csv"))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] parts = line.split(";");
        if (parts.length >= 4) {
          String name = parts[0];
          String phoneNumber = parts[1];
          String address = parts[2];
          String email = parts[3];
          Contact contact = new Contact(name);
          contact.getPhoneNumbers().add(phoneNumber);
          contact.setAddress(address);
          contact.setEmail(email);
          phoneBook.put(name, contact);
        }
      }
    } catch (IOException e) {
      System.out.println("Ошибка загрузки данных: " + e.getMessage());
    }
    return phoneBook;
  }

  private static void savePhoneBook(TreeMap<String, Contact> phoneBook) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter("phonebook.csv"))) {
      for (Contact contact : phoneBook.values()) {
        String name = contact.getName();
        ArrayList<String> phoneNumbers = contact.getPhoneNumbers();
        String address = contact.getAddress();
        String email = contact.getEmail();
        StringBuilder line = new StringBuilder(name);
        for (String phoneNumber : phoneNumbers) {
          line.append(";").append(phoneNumber);
        }
        line.append(";").append(address).append(";").append(email);
        writer.write(line.toString());
        writer.newLine();
      }
      System.out.println("Телефонная книга успешно сохранена.\n");
    } catch (IOException e) {
      System.out.println("Ошибка сохранения данных: " + e.getMessage());
    }
  }
}
