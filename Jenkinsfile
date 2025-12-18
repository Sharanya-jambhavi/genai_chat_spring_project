pipeline {
    agent any

    tools {
        maven 'maven'
        jdk 'jdk17'
    }

    environment {
        DOCKER_IMAGE = "sky1912/genai-chat-app"
    }

    stages {

        stage('Build JAR') {
            steps {
                sh '''
                mvn -version
                mvn clean package -DskipTests
                '''
            }
        }

        stage('Build Docker Image') {
            steps {
                sh '''
                docker build -t $DOCKER_IMAGE:latest .
                '''
            }
        }

        stage('Push Docker Image') {
            steps {
                withDockerRegistry(credentialsId: 'dockerhub-creds') {
                    sh '''
                    docker push $DOCKER_IMAGE:latest
                    '''
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                sh '''
                kubectl apply -f k8s/namespace.yaml
                kubectl apply -f k8s/mysql-service.yaml
                kubectl apply -f k8s/mysql-statefulset.yaml
                kubectl apply -f k8s/app-deployment.yaml
                kubectl apply -f k8s/app-service.yaml
                '''
            }
        }
    }

    post {
        success {
            echo '✅ CI/CD Pipeline executed successfully'
        }
        failure {
            echo '❌ Pipeline failed – check logs'
        }
    }
}
