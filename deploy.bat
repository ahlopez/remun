call C:\apache\apache-tomcat-9.0.33\bin\shutdown.bat
call mvn clean
call mvn package
call del  C:\apache\apache-tomcat-9.0.33\webapps\remuneracion-1.0-SNAPSHOT.war /Q
call rmdir C:\apache\apache-tomcat-9.0.33\webapps\remuneracion-1.0-SNAPSHOT /S /Q
call copy C:\ahl\des\remun\finalize\target\remuneracion-1.0-SNAPSHOT.war  C:\apache\apache-tomcat-9.0.33\webapps
call C:\apache\apache-tomcat-9.0.33\bin\startup.bat
