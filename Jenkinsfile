pipeline {
    agent any
    tools{
        maven 'maven_3_9_9'
    }
    stages{
        stage('Build maven'){
            steps{
                checkout scmGit(branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/Plizama/ProyectoMINSW_Backend']])
                bat 'mvn clean package'
            }
        }

        stage('Unit Tests') {
            steps {
                // Run Maven 'test' phase. It compiles the test sources and runs the unit tests
                bat 'mvn test' // Use 'bat' for Windows agents or 'sh' for Unix/Linux agents
            }
        }

        stage('Build docker image'){
            steps{
                script{
                    bat 'docker build -t plizama/proyecto-misw:latest .'
                }
            }
        }
        stage('Push image to Docker Hub'){
            steps{
                script {
                    withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                        bat 'docker login -u %DOCKER_USERNAME% -p %DOCKER_PASSWORD%'
                    }
                    bat 'docker push plizama/proyecto-misw:latest'
                }
            }
        }
    }
}