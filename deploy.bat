rem C:\apache\apache-tomcat-9.0.33\bin\shutdown.bat
call mvn clean
call mvn package
del  C:\apache\apache-tomcat-9.0.33\webapps\remuneracion-1.0-SNAPSHOT.war /Q
rem rmdir C:\apache\apache-tomcat-9.0.33\webapps\remuneracion-1.0-SNAPSHOT /S /Q
copy C:\ahl\des\wk3\remun\finalize\target\remuneracion-1.0-SNAPSHOT.war  C:\apache\apache-tomcat-9.0.33\webapps
