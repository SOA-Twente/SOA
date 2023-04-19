# SOA

Software developed for the course of Service-Oriented-Architecture of the University of Twente.

# Goal

Make a Twitter clone based on MicroServices called QuackAttack

# The different apps

- SearchApp: Responsible for searching users and messages
- ProfileApp: Keep track of personal information of profile page
- TimelineApp: To return personalized timelines
- FollowApp: To (un)follow users and get follow information
- PostmessageApp: To pose messages and get messages by username, content or ID
- DirectMessageConsumer and DirectMessageProducer: Create conversation, get conversation, send messages
- RegisterApp: Keep track of the registered users and sign-up for users.
- Client: Our feign client implementation for a few services.

# Important considerations

The google login will only function with @student.utwente.nl accounts as google otherwise needs to check your implementation.
Please make sure to give the deployments enough time to start. Running this many deployments locally on one machine tend to slow everything down.
Make sure to use `localhost:5173` and not `127.0.0.1:5173` to access the client as the latter has not been whitelisted for the google authentication system.

# Install Windows

First open the main SOA project and use the command `gradle buildDockerImages`, this will compile all the JARs and create local images of your services with docker on windows. Please make sure docker is installed.

Make sure to either make an docker image of the front-end as described in the svelte project or run it locally as described in the svelte app

Make sure you have minikube installed. Start minikube with `minikube start`.

Now go in the folder ./kubernetes and execute the `scriptDB.bat`, this will create all the database deployments necessary together with their storage claims and services. Once all the DB's are running, use `createTable.bat` in order to create all the tables in the specific pods automatically. You can make sure the tables exist by running the `getInfoScript.bat` which should print the tables of every DB.

Now run the `scriptServices.bat` to create deployments for all the services. This will take quite some time as it will also copy the docker images to a local storage element for minikube. You can use `minikube dashboard --url` in the command line to show the url of the kubernetes cluster dashboard where you can monitor any problems.

Now install rabbitMQ operator with `kubectl apply -f "https://github.com/rabbitmq/cluster-operator/releases/latest/download/cluster-operator.yml"` and then the cluster with the two commands `kubectl config set-context --current --namespace=production` which will set our current namespace in the command line and `kubectl apply -f https://raw.githubusercontent.com/rabbitmq/cluster-operator/main/docs/examples/hello-world/rabbitmq.yaml`. Please use the RabbitMQ tutorial for any extra information as to how to login and show the dashboard (https://www.rabbitmq.com/kubernetes/operator/quickstart-operator.html).

now use the command `minikube tunnel` in order to be able to use the front-end. No matter if it is run locally with `npm run dev` or on the kubernetes cluster. Go to `localhost:5173` to get to the client.

# Install on other systems

Use the Windows guide, but every command in the .bat files need to be done manually in the correct language for the system



