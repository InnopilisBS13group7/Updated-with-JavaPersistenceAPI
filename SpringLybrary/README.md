

 **DeepLib** 
 ====================
#### Innovation in Libraray Managment System

------------------------------
### There are 4 contributors in our Team (DeepLib Team)
1. Maksimychev Evgenij - Test Cases and Java Classes that are connected with Objects such as Librarian, Patron and etc.
2. Uzbekova Ekaterina - Frontend (HTML, CSS, JS, Bootstrap and etc.)
3. Yudinskikh Yaroslav - Database and Backend (Java, MySQL, Spring and etc.)
4. Vakhula Igor - Database and Backend (Java, MySQL, Spring and etc.)


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
But you need to do one more important step. I can explain why. Our database now in the Internet server and it is a bit difficult to work without it. So, you need to open in our project: main -> resources -> database.properties and replace text there with:
""
jdbc.driverClassName=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/spring_security_app
jdbc.username=root
jdbc.password=root
"
and do same things in main -> webapp -> WEB-INF -> views -> (listOfArticles, listOfBooks, listOfUsers, listOfAudioVideoMaterial)
replace this 
"
 dataSource.setDriverClassName("com.mysql.jdbc.Driver");
                dataSource.setUrl("jdbc:mysql://eu-cdbr-west-02.cleardb.net:3306/heroku_f76d6fb9e659782");
                dataSource.setUsername("baff532465d8d9");
                dataSource.setPassword("ffa9cd9f");
"
to this
"
 dataSource.setDriverClassName("com.mysql.jdbc.Driver");
                dataSource.setUrl("jdbc:mysql://localhost:3306/spring_security_app");
                dataSource.setUsername("root");
                dataSource.setPassword("root");
"
Please if you have some problems with database, return every database changes to previous and try one more time with Internet Connection.



 ### To start WEB-application:
  1. **View - > Tool Windows -> Maven Project - > click on "Spring SecurityApp Maven Webapp" -> Plugins -> jetty -> jetty:run**
  2. At Web-browser put in address line "[localhost:8080](http://localhost:8080)"
  3. Some account for **Sing In**:
  in the internet
   * Account of Librarian:
      + username: DamskiyUgodnik
      + password: adminadmin
    * Account of Patron:
      + username: i.vakhula
      + password: adminadmin
      
    
 **in local
 * Account of Librarian:
      + username: DamskiyUgodnik
      + password: adminadmin
    * Account of Patron:
      + username: i.vakhula_user
      + password: useruser
 
 ### For check TEST CASES:
 
 1. Go to /src/main/java/net/proselyte/springsecurityapp/model/Tests/
 2. And compile and run file file with title "TestCases_<dd>_<mm>" where dd_mm is date of delivery
 
 


