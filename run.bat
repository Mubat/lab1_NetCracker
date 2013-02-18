javac  -Xlint -d bin -classpath src;lib\jcalendar-1.4.jar -sourcepath src src/Sergi/MVC/Controller/Controller.java 
jar cmf res/manifest.mf build/program.jar -C bin .
java -classpath bin;lib\jcalendar-1.4.jar Sergi.MVC.Controller.Controller
@pause