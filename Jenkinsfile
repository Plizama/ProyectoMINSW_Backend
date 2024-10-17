pipeline {
    agent any
    environment {
        DB_HOST = '192.168.1.86' // Aquí defines la variable de entorno DB_HOST con la IP de la base de datos
    }
    tools {
        maven 'maven_3_9_9'
    }
    stages {
        stage('Build maven') {
            steps {
                checkout scmGit(branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/Plizama/ProyectoMINSW_Backend']])
                bat 'mvn clean package'
            }
        }

        stage('Unit Tests') {
            steps {
                // Ejecuta la fase de pruebas con Maven
                bat 'mvn test'
            }
        }

        stage('Build docker image') {
            steps {
                script {
                    bat 'docker build -t plizama/proyecto-misw:latest .'
                }
            }
        }

        stage('Push image to Docker Hub') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                        bat 'echo %DOCKER_PASSWORD% | docker login -u %DOCKER_USERNAME% --password-stdin'
                    }
                    bat 'docker push plizama/proyecto-misw:latest'
                }
            }
        }
    }
}
