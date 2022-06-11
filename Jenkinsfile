pipeline{
    agent any

    tools {
        maven 'Maven'
    }
    stages {
        stage("build") {
            steps {
                echo 'building the application'
            }
        }

        stage("test") {
                    steps {
                        echo 'testing the application'
                        sh "mvn clean test"
                    }
                }

        stage("deploy") {
                    steps {
                        echo 'deploying the application'
                    }
                }
    }
}