pipeline {
    agent {
        kubernetes {
            yaml """
apiVersion: v1
kind: Pod
spec:
  containers:
    - name: jdk
      image: maven:3.9.6-eclipse-temurin-17
      command:
        - cat
      tty: true
      volumeMounts:
        - name: maven-cache
          mountPath: /root/.m2
    - name: docker
      image: docker:24.0.5
      command:
        - cat
      tty: true
      volumeMounts:
        - name: docker-sock
          mountPath: /var/run/docker.sock
    - name: kubectl
      image: bitnami/kubectl:latest
      command:
        - cat
      tty: true
  volumes:
    - name: maven-cache
      emptyDir: {}
    - name: docker-sock
      hostPath:
        path: /var/run/docker.sock
"""
        }
    }
    environment {
        DOCKER_USERNAME = 'cuiqiuyue'
        DOCKER_PASSWORD = 'zhouchi2001'
    }
    stages {
        stage('Checkout Code') {
            steps {
                git url: 'https://github.com/zhouchi1/open_platform.git', branch: 'master'
            }
        }
        stage('Build Services') {
            steps {
                container('jdk') {
                    sh '''
                    for service in auth-service order-service pay-service; do
                      echo "Building $service"
                      cd $service
                      mvn clean package -DskipTests
                      cd ..
                    done
                    '''
                }
            }
        }
        stage('Docker Build & Push') {
            steps {
                container('docker') {
                    sh '''
                    echo "${DOCKER_PASSWORD}" | docker login -u "${DOCKER_USERNAME}" --password-stdin
                    for service in auth-service order-service pay-service email-service gateway-service message-service task-service user-service websocket-service; do
                      echo "Building docker image for $service"
                      docker build -t ${DOCKER_USERNAME}/${service}:${BUILD_NUMBER} $service
                      docker push ${DOCKER_USERNAME}/${service}:${BUILD_NUMBER}
                    done
                    '''
                }
            }
        }
        stage('Deploy to Minikube') {
            steps {
                container('kubectl') {
                    sh '''
                    for service in auth-service order-service pay-service; do
                      sed "s|<DOCKER_USER>|${DOCKER_USERNAME}|g; s|\\${BUILD_NUMBER}|${BUILD_NUMBER}|g" k8s/${service}-deployment.yaml | kubectl apply -f -
                    done
                    '''
                }
            }
        }
    }
}
