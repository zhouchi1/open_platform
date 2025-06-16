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
      command: ["sh", "-c", "sleep infinity"]
      tty: true
      volumeMounts:
        - name: maven-cache
          mountPath: /root/.m2
    - name: docker
      image: docker:24.0.5-dind
      securityContext:
        privileged: true
      command: ["sh", "-c", "sleep infinity"]
      tty: true
      volumeMounts:
        - name: docker-sock
          mountPath: /var/run/docker.sock
    - name: kubectl
      image: bitnami/kubectl:latest
      command: ["sh", "-c", "sleep infinity"]
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
        DOCKER_CREDENTIALS = credentials('dockerhub-credentials') // Jenkins凭据ID
    }

    stages {
        stage('Checkout Code') {
            steps {
                echo "📦 正在拉取代码..."
                git url: 'https://github.com/zhouchi1/open_platform.git', branch: 'master'
            }
        }

        stage('Build Services') {
            steps {
                container('jdk') {
                    sh '''
                    echo "🔨 开始构建各服务..."
                    for service in auth order pay email gateway message task user websocket; do
                      echo "➡️ 构建服务: $service"
                      if [ -d "$service" ]; then
                        cd $service
                        if ! mvn clean package -DskipTests; then
                          echo "❌ 构建失败: $service"
                          exit 1
                        fi
                        cd ..
                      else
                        echo "⚠️ 跳过：未找到目录 $service"
                      fi
                    done
                    '''
                }
            }
        }

        stage('Docker Build & Push') {
            steps {
                container('docker') {
                    sh '''
                    echo "🐳 登录 DockerHub..."
                    echo "${DOCKER_CREDENTIALS_PSW}" | docker login -u "${DOCKER_CREDENTIALS_USR}" --password-stdin

                    for service in auth order pay email gateway message task user websocket; do
                      echo "📦 构建并推送镜像: $service"
                      docker build -f $service/Dockerfile -t ${DOCKER_CREDENTIALS_USR}/$service:${BUILD_NUMBER} $service
                      docker push ${DOCKER_CREDENTIALS_USR}/$service:${BUILD_NUMBER}
                    done
                    '''
                }
            }
        }

        stage('Deploy to Minikube') {
            steps {
                container('kubectl') {
                    sh '''
                    echo "🚀 开始部署到 Minikube..."
                    for service in auth order pay email gateway message task user websocket; do
                      if [ -f k8s/${service}-deployment.yaml ]; then
                        echo "📄 应用部署文件: $service"
                        export DOCKER_USER=${DOCKER_CREDENTIALS_USR}
                        export BUILD_NUMBER=${BUILD_NUMBER}
                        envsubst < k8s/${service}-deployment.yaml | kubectl apply -f -
                      else
                        echo "⚠️ 跳过部署: 找不到 k8s/${service}-deployment.yaml"
                      fi
                    done
                    '''
                }
            }
        }
    }
}
