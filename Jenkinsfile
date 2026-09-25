pipeline {
    agent any

    environment {
        IMAGE_NAME = 'spring-boot-jenkins-demo'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }


        stage('Maven-build') {
            steps {
                sh '''
                    mvn -B \
                        -Dmaven.repo.local="$WORKSPACE/.m2/repository" \
                        clean package -DskipTests
                '''
            }
        }

        stage('Run Application') {
            steps {
                sh '''
                    echo "=== DEPLOYING SPRING BOOT APP ==="

                    # Stop previous application if running
                    pkill -f 'spring-boot-jenkins-demo.*\\.jar' || true

                    # Start application in background
                    nohup java -jar target/*.jar \
                        --server.port=8000 \
                        > spring-boot.log 2>&1 &

                    echo "=== APPLICATION STARTED ==="
                    sleep 5

                    echo "=== APPLICATION LOG ==="
                    tail -50 spring-boot.log

                    echo "=== RUNNING PROCESS ==="
                    ps aux | grep '[s]pring-boot'
                '''
            }
        }

        stage('Health Check') {
            steps {
                sh '''
                    echo "=== WAITING FOR APPLICATION ==="
                    sleep 5

                    echo "=== HEALTH CHECK ==="
                    curl --fail --retry 5 --retry-delay 2 \
                        http://localhost:8000/actuator/health
                '''
            }
        }
    }

}
