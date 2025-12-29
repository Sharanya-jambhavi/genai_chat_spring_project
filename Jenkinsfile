pipeline {
    agent any

    tools {
        maven 'maven'
    }

    environment {
        DOCKER_IMAGE = "sky1912/genai-chat-app"
    }

    stages {

        stage('Build JAR') {
            steps {
                sh '''
                echo "🔹 Java Version"
                java -version

                echo "🔹 Building Spring Boot JAR"
                mvn clean package -DskipTests
                '''
            }
        }

        stage('Build Docker Image') {
            steps {
                sh '''
                echo "🔹 Building Docker image"
                docker build -t $DOCKER_IMAGE:latest .
                '''
            }
        }

        stage('Push Docker Image') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-creds',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh '''
                    echo "🔹 Logging into Docker Hub"
                    echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin

                    echo "🔹 Pushing Docker image"
                    docker push $DOCKER_IMAGE:latest

                    docker logout
                    '''
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                sh '''
                echo "🔹 Creating namespace"
                kubectl apply -f k8s/namespace.yaml
       
                echo "🔹 Deploying MySQL"
                kubectl apply -f k8s/mysql-service.yml
                kubectl apply -f k8s/mysql-statefulset.yml

                echo "🔹 Deploying Application"
                kubectl apply -f k8s/app-deployment.yml
                kubectl apply -f k8s/app-service.yml
                '''
            }
        }
    }

    post {
        success {
            echo '🎉 FULL CI/CD PIPELINE COMPLETED SUCCESSFULLY'
        }
        failure {
            echo '❌ PIPELINE FAILED — CHECK LOGS'
        }
    }
}
