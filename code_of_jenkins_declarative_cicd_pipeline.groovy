pipeline {
			agents any
			
			stages{
				stage("code"){
					steps {
						echo "Cloning the code"
						git url:"https://github.com/ab-1111/notes-app.git", branch:"main"
					}	
				}
				stage("build"){
					steps {
						echo "Building the code"
						sh "docker build -t my-note-app ."
					}
				}
				stage("push to docker hub"){
					steps {
						echo "pushing the image to dockerhub"
						withCredentials([usernamePassword(credentialsId:"dockerHub",passwordVariable:"dockerHubPass",usernameVariable:"dockerHubUser")])
						sh "docker tag my-note-app ${env.dockerHubUser}/my-note-app:latest"
						sh "docker login -u ${env.dockerHubUser} -p ${env.dockerHubPass}"
						sh "docker push ${env.dockerHubUser}/my-note-app:latest"
					}
				}
				stage("deploy"){
					steps {
						echo "Deploying the container"
						sh "docker-compose down && docker-compose up -d"
						
					}
				}
			}
		}