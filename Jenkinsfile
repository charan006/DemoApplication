pipeline {
    agent any

//      environment {
//             IMAGE_NAME = 'spring-boot-jenkins-demo'
//         }

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
                }
            }
            steps {
                sh '''
                    mkdir -p "$WORKSPACE/.m2/repository"
                    mvn -Dmaven.repo.local="$WORKSPACE/.m2/repository" \
                        clean package -DskipTests
                '''
            }
        }

        stage('Test') {
            agent {
                docker {
                    image 'maven:3.9.11-eclipse-temurin-21'
                }
            }
            steps {
                sh '''
                    mvn -Dmaven.repo.local="$WORKSPACE/.m2/repository" test
                '''
            }
        }

        stage('Docker-image-build') {
            steps {
                sh 'docker build -t ${IMAGE_NAME}:${BUILD_NUMBER} .'
            }
        }

        stage('Run Container') {
            steps {
                sh '''
                    docker rm -f spring-boot-demo || true

                    docker run -d \
                        --name spring-boot-demo \
                        -p 8000:8000 \
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