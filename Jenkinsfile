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
                    mvn clean package -DskipTests
                '''
            }
        }

        stage('Test') {
            steps {
                sh '''
                    mvn test
                '''
            }
        }

        stage('Docker-image-build') {
            steps {
                sh '''
                    echo "=== TARGET ==="
                    ls -lh target/

                    docker build -t ${IMAGE_NAME}:${BUILD_NUMBER} .
                '''
            }
        }

        stage('Run Container') {
            steps {
                sh '''
                    docker rm -f spring-boot-demo || true

                    docker run -d \
                        --name spring-boot-demo \
                        -p 8000:8080 \
                        -e APP_ENV=jenkins \
                        ${IMAGE_NAME}:${BUILD_NUMBER}
                '''
            }
        }

        stage('Health Check') {
            steps {
                sh 'sleep 5'
                sh 'curl --fail http://localhost:8000/actuator/health'
            }
        }
    }

    post {
        always {
            sh 'docker logs spring-boot-demo || true'
        }
    }
}