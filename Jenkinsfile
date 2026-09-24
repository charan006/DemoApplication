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
            agent {
                docker {
                    image 'maven:3.9.11-eclipse-temurin-21'
                    reuseNode true
                    args '-v /var/lib/jenkins/.m2:/root/.m2'
                }
            }

            steps {
                sh '''
                    echo "HOME=$HOME"
                    echo "USER=$USER"

                    java -version
                    mvn -version

                    mvn -B \
                        -Dmaven.repo.local=/root/.m2/repository \
                        clean package -DskipTests
                '''
            }
        }

        stage('Test') {
            agent {
                docker {
                    image 'maven:3.9.11-eclipse-temurin-21'
                    reuseNode true
                    args '-v /var/lib/jenkins/.m2:/root/.m2'
                }
            }

            steps {
                sh '''
                    mvn -B \
                        -Dmaven.repo.local=/root/.m2/repository \
                        test
                '''
            }
        }

        stage('Docker-image-build') {
            steps {
                sh '''
                    echo "=== TARGET ==="
                    ls -lh target/

                    echo "=== DOCKER ==="
                    docker --version

                    docker build \
                        -t ${IMAGE_NAME}:${BUILD_NUMBER} .
                '''
            }
        }

        stage('Run Container') {
            steps {
                sh '''
                    echo "=== DEPLOYING ==="

                    docker rm -f spring-boot-demo || true

                    docker run -d \
                        --name spring-boot-demo \
                        -p 8000:8080 \
                        -e APP_ENV=jenkins \
                        ${IMAGE_NAME}:${BUILD_NUMBER}

                    docker ps
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

    post {
        always {
            sh '''
                echo "=== CONTAINER LOGS ==="
                docker logs spring-boot-demo || true
            '''
        }
    }
}