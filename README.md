# File Downloader Tool
This is project is a file download tool, it allows users from the same network to share files (default: up to 10MB) between users and over networks. The project uses Spring Boot TCP-based protocol and creates a server running on port (USER_STATIC_IP:6060)

![image](https://github.com/user-attachments/assets/cd5293c5-8803-4aaf-858a-bba38054a885)


## How does it work:

The user must first fill the [fileID] parameter first. This is because this application uses this parameter as a way of looking for the desired file. Meaning we will need it both to upload and to download files.

### Uploading Files
1. Insert the desired [fileID], this will be the file identifier

2. Next the User must click the [UPLOAD] button, this will prompt the user with a file selection menu

3. Select the file and done, it has been uploaded.

Note: The user may or may not fill in the [userID] label, if this is filled the user can then look for the files it uploaded by clicking on [Check User Files]

### Downloading Files
1. Insert the desired [fileID], this is the file identifier

2. Click the [DOWNLOAD] button and then the file will be downloaded
   
## Instructions:

Here’s the version with unformatted links so you can copy and paste it directly into GitHub:  

---

### 📌 Build & Run Instructions for File Transfer Tool (Spring Boot)

#### 🛠️ Prerequisites  
Before you build and run this project, ensure you have:  
- **[Java 17+](https://adoptium.net/)** installed  
- **[Maven 3.8+](https://maven.apache.org/download.cgi)** installed  
- **[Git](https://git-scm.com/downloads)** installed

#### 📥 Clone the Repository  
```sh
git clone https://github.com/aobutakuki/fileDownloader
cd fileDownloader
```

#### 🔨 Build the Project  
Use Maven to package the application:  
```sh
mvn clean package
```
This generates a `.jar` file in the `target/` directory.

#### ⚙️ Configuration  
Before running be sure to change the ip to your ip.
To change the port, modify `application.properties` or run:  
```sh
java -jar target/*.jar --server.address=YOUR_LOCAL_IP
java -jar target/*.jar --server.port=9090

```

#### 🚀 Run the Application  
Run the Spring Boot application with:  
```sh
java -jar target/*.jar
```
By default, the app runs on **http://YOUR_LOCAL_IP:6060**.
Which means you most likely need to change the ip through the configuration step or through the application.propertities file

#### 🔥 Allow Network Access (Windows Firewall)  
To allow other devices on your LAN to access the server:  
1. Open **Windows Defender Firewall** → **Advanced Settings**  
2. Go to **Inbound Rules** → **New Rule**  
3. Select **Port** → **TCP** → **6060 (or your custom port)**  
4. Allow **All connections**  
5. Name it and **Save**  

#### 📡 Access from Other Devices on the Network  
Find your **local IP** with:  
```sh
ipconfig
```
Then, access the app from other devices using:  
```
http://YOUR_LOCAL_IP:8080
```

#### 🛑 Stopping the Server  
To stop the server, press:  
```sh
CTRL + C
```


## NOTE:
The file CSS/HTML and the project properties/backend maybe changed from the resources folder, it is recommended to open the project on an IDEA like IntelliJ Community before editting the code/files, to edit HTML/CSS/JavaScript it is recommended to use VS Code.
