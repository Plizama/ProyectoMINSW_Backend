pipeline {
    agent any
    environment {
        DB_HOST = '192.168.1.86'
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
                    withCredentials([string(credentialsId: 'dockerhub-password', variable: 'dhpsw')]) {
                        bat 'docker login -u plizama -p %dhpsw%'
                    }
                    bat 'docker push plizama/proyecto-misw:latest'
                }
            }
        }
    }
}

