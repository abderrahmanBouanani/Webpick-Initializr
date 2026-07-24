pipeline {
    agent any

    environment {
        APP_NAME = '${projectName}'
        DOCKER_REGISTRY = 'registry.webpick.com'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                sh 'echo "Construction du code source pour ${projectName}..."'
                // Commandes dynamiques selon le framework
            }
        }

        stage('Docker Build & Push') {
            steps {
                sh 'docker build -t $DOCKER_REGISTRY/$APP_NAME:latest .'
                sh 'docker push $DOCKER_REGISTRY/$APP_NAME:latest'
            }
        }
    }
}
