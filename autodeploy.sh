./gitpull.sh
kill $(cat ./bin/shutdown.pid)
./gradlew bootJar
kill $(ps -aux | grep gradle |grep -v grep | awk '{print $2}')
sleep 5
nohup java -Xss256m -Xmx512m -jar ./build/libs/loan24-0.0.1.jar &
