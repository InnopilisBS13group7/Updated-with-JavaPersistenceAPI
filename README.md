

 **BookBooking** 
 ====================
#### Broaden your horizonts

------------------------------
### There are 3 contributors in our Team (DeepLib Team)
1. Kiselyov Semyon - Backend
2. Ilia Prokopev - Frontend (HTML, CSS, JS, JQuery and etc.)
3. Alia Zagidulina - Database and testing (Java, JUnit and etc.)


### **How to install our project on your PC** 

+ Install:
  - Java 1.8.
  - Intelliji IDEA Ultimate.
  - Internet Connection.
         

1. Download from this repository .zip and unarchive
2. Open **Intellij IDEA -> "Open" -> "Choose unarchived project-folder"**.
3. Setup Java SDK from your location.
4. In the lower right corner of IDEA windows should opens message of Maven, click on "AutoImport"
5. If this exist skip this point, else you should do this by hands in IDEA:
      **View - > Tool Windows -> Maven Project -> Above the inscription "Spring SecurityApp Maven Webapp" press on button "Reimport All Maven Projects"**
6. Make sure that port '8080' of your 'localhost' not busy. 

### Database setting:
We have all tables in our database.sql file:
1. Open MySQL Workbench, setup new connection (Name: root, Password: root)
2. Please create a new schema in the connected server (Name: spring_library_app, Collation: utf-8 general ci).
3. Please return to our project in Intellij IDEA: View -> Tool Windows -> Database -> New (green plus) -> Data Source -> MySQL.
Now you need to input data about Database
Name: spring_library_app ||| user: root ||| password: root
Make test connection.
If successful you can start to use our web app if something went wrong please try one more time.
**
Also, you need to open in our project: main -> resources -> database.properties and replace text there with:
""
jdbc.driverClassName=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/itpdb
jdbc.username=root
jdbc.password=root
"
Please if you have some problems with database, return every database changes to previous and try one more time with Internet Connection.



 ### To start WEB-application:
  1. **View - > Tool Windows -> Maven Project - > click on "Spring SecurityApp Maven Webapp" -> Plugins -> jetty -> jetty:run**
  2. At Web-browser put in address line "[localhost:8080](http://localhost:8080)"
  3. Some account for **Sing In**:
   * Account of Librarian:
      + username: Admin
      + password: adminadmin
 
 


