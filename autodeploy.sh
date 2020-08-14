./gitpull.sh
kill $(cat ./bin/shutdown.pid)
./gradlew bootJar
nohup java -XX:InitialRAMPercentage=70 -XX:MaxRAMPercentage=70 -jar ./build/libs/loan24-0.0.1.jar &
