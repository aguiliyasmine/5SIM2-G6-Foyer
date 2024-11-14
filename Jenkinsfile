pipeline {
    agent any

    environment {
        MYSQL_DATABASE = 'foyer'
        DOCKER_HUB_REPO = 'yasmine064/aguiliyasmine_5sim2_g6_foyer'
        NEXUS_CREDENTIALS_ID = 'nexus-credentials'
    }

    triggers {
        githubPush()
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'aguiliyasmine-5SIM2-G6', url: 'https://github.com/aguiliyasmine/5SIM2-G6-Foyer.git'
            }
        }

        stage('Clean') {
            steps {
                sh 'mvn clean'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn sonar:sonar'
                }
            }
        }

        stage('Build') {
            steps {
                sh 'mvn package -DskipTests'
            }
        }

        stage('Deploy to Nexus') {
            steps {
                script {
                    echo "Deploying to Nexus Snapshot Repository..."

                    nexusArtifactUploader(
                        nexusVersion: 'nexus3',
                        protocol: 'http',
                        nexusUrl: '192.168.1.26:8081',
                        repository: 'maven-snapshots', 
                        credentialsId: 'nexus-credentials',
                        groupId: 'tn.esprit.spring',
                        artifactId: 'Foyer',
                        version: '0.0.1-SNAPSHOT',
                        artifacts: [
                            [
                                artifactId: 'Foyer',
                                classifier: '',
                                file: 'target/Foyer-0.0.1-SNAPSHOT.jar',
                                type: 'jar'
                            ]
                        ]
                    )

                    echo "Deployment to Nexus Snapshot Repository completed!"
                }
            }
        }




        stage('Docker Build and Push') {
            steps {
                script {
                    docker.build("${DOCKER_HUB_REPO}:${env.BUILD_NUMBER}")
                    docker.withRegistry('https://index.docker.io/v1/', 'dockerhub-credentials-id') {
                        docker.image("${DOCKER_HUB_REPO}:${env.BUILD_NUMBER}").push()
                    }
                }
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                sh "docker-compose down && docker-compose up -d"
            }
        }
    }

    post {
        success {
            echo 'Build and deployment successful!'
        }
        failure {
            echo 'Build failed.'
        }
    }
}
