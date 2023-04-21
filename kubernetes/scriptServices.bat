minikube image load dockerfiles-profile_app
minikube image load dockerfiles-follow_app
minikube image load dockerfiles-register_app
minikube image load dockerfiles-post_message_app
minikube image load dockerfiles-search_app
minikube image load dockerfiles-directmessageproducer_app
minikube image load dockerfiles-directmessageconsumer_app
minikube image load dockerfiles-timeline_app
minikube image load svelte-docker

for /r %%i in (yaml\*) do (
    kubectl apply -f %%i
)

