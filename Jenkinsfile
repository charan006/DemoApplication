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

//         stage('Maven-build') {
//             agent {
//                 docker {
//                     image 'maven:3.9.11-eclipse-temurin-21'
//                     reuseNode true
//                 }
//             }
//
//             steps {
//                 sh '''
//                     echo "=== JAVA ==="
//                     java -version
//
//                     echo "=== MAVEN ==="
//                     mvn -version
//
//                     echo "=== MAVEN CACHE ==="
//                     mkdir -p "$WORKSPACE/.m2/repository"
//                     ls -ld "$WORKSPACE/.m2/repository"
//
//                     echo "=== BUILD ==="
//                     mvn -B \
//                         -Dmaven.repo.local="$WORKSPACE/.m2/repository" \
//                         clean package -DskipTests
//                 '''
//             }
//         }
//
//         stage('Test') {
//             agent {
//                 docker {
//                     image 'maven:3.9.11-eclipse-temurin-21'
//                     reuseNode true
//                 }
//             }
//
//             steps {
//                 sh '''
//                     mvn -B \
//                         -Dmaven.repo.local="$WORKSPACE/.m2/repository" \
//                         test
//                 '''
//             }
//         }

stage('Maven-build') {
    steps {
        sh '''
            echo "=== TARGET ==="
            ls -lh target/

            echo "=== JAVA ==="
            java -version
        '''
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
            tail -30 spring-boot.log

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