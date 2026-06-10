pipeline {

    agent any

    tools {
        jdk 'JDK21'
        maven 'Maven3'
    }

    parameters {

        choice(
            name: 'SUITE',
            choices: [
                'ui-testng.xml',
                'testng.xml'
            ],
            description: 'Select TestNG Suite'
        )

        choice(
            name: 'BROWSER',
            choices: [
                'chrome',
                'edge',
                'firefox'
            ],
            description: 'BrowserStack Browser'
        )
    }

    environment {

        BROWSERSTACK_USERNAME =
            credentials('BROWSERSTACK_USERNAME')

        BROWSERSTACK_ACCESS_KEY =
            credentials('BROWSERSTACK_ACCESS_KEY')
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean compile'
            }
        }

        stage('Execute Tests') {

            steps {

                bat """
                mvn test ^
                -Dbrowser=${params.BROWSER} ^
                -DsuiteXmlFile=src/test/resources/suites/${params.SUITE}
                """
            }
        }
    }

    post {

        always {

            echo 'Publishing Reports...'

            junit '**/surefire-reports/*.xml'

            archiveArtifacts(
                artifacts: '**/screenshots/**/*',
                allowEmptyArchive: true
            )

            allure(
                includeProperties: false,
                results: [[path: 'target/allure-results']]
            )
        }

        success {

                    echo '====================================='
                    echo 'BUILD SUCCESSFUL'
                    echo "Build Number : ${env.BUILD_NUMBER}"
                    echo "Job Name     : ${env.JOB_NAME}"
                    echo '====================================='
                }

        failure {

                    echo '====================================='
                    echo 'BUILD FAILED'
                    echo "Build Number : ${env.BUILD_NUMBER}"
                    echo "Job Name     : ${env.JOB_NAME}"
                    echo 'Check Console Output and Allure Report'
                    echo '====================================='
                }

        unstable {

                    echo '====================================='
                    echo 'BUILD UNSTABLE'
                    echo 'Some test cases failed'
                    echo '====================================='
                }

                aborted {

                    echo '====================================='
                    echo 'BUILD ABORTED'
                    echo '====================================='
                }
    }
}